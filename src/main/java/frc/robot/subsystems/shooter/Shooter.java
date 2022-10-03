package frc.robot.subsystems.shooter;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotState;
import frc.robot.subsystems.shooter.ShooterIO.ShooterIOInputs;
import frc.robot.util.PIDTuner;
import org.littletonrobotics.junction.Logger;

public class Shooter extends SubsystemBase {
    private final ShooterIO io;
    private final ShooterIOInputs inputs = new ShooterIOInputs();

    private double setpoint = 0;
    private double forcedSetpoint = -1;

    private final PIDController pidController = new PIDController(0,0,0);
    private final PIDTuner tuner = new PIDTuner("Shooter", pidController);

    private final static Shooter INSTANCE = new Shooter(Constants.ROBOT_SHOOTER_IO);
    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter(ShooterIO io) {
        this.io = io;
        pidController.setTolerance(50);

        ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
        shooterTab.addNumber("Shooter Target RPM", () -> setpoint);
        shooterTab.addNumber("Shooter Actual", this::getVelocity);
        shooterTab.addBoolean("Shooter up to Speed", this::isAtSetpoint);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Shooter", inputs);
        Logger.getInstance().recordOutput("Shooter/TargetRMP", setpoint);
        Logger.getInstance().recordOutput("Shooter/ForcedRMP", forcedSetpoint);
        Logger.getInstance().recordOutput("Shooter/ActualRMP", getVelocity());
        Logger.getInstance().recordOutput("Shooter/AtSetpoint", isAtSetpoint());

        if (RobotState.getInstance().isClimbing()) {
            io.setVelocity(0, 0);
        }
        else if (forcedSetpoint != -1) {
            io.setVelocity(forcedSetpoint, Constants.SHOOTER_FEEDFORWARD * forcedSetpoint);
        }
        else if (setpoint != 0) {
            io.setVelocity(setpoint, Constants.SHOOTER_FEEDFORWARD * setpoint);
        }
        else if (Constants.ROBOT_IDLE_SHOOTER_ENABLED) {
//            TODO: use robot pose to estimate speed
            io.setVelocity(Constants.SHOOTER_SPEED_MAP.lookup(RobotState.getInstance().getDistanceToHub()), Constants.SHOOTER_FEEDFORWARD * forcedSetpoint);
        }
    }

    public void setVoltage(double voltage) {
        io.setVoltage(voltage);
    }

    /**
     * @param setpoint -1 is disabled for forced
     */
    public void setSetpoint(double setpoint, boolean forced) {
        if (!forced) {
            this.setpoint = setpoint;
        }
        else {
            forcedSetpoint = setpoint;
        }
    }

    public double getVelocity() {
        return inputs.velocityRotPerMin;
    }

    public boolean isAtSetpoint() {
        return pidController.atSetpoint() || Constants.ROBOT_DEMO_MODE && RobotBase.isSimulation();
    }

}