package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotState;
import frc.robot.subsystems.shooter.ShooterIO.ShooterIOInputs;
import frc.robot.util.BetterMath;
import org.littletonrobotics.junction.Logger;

public class Shooter extends SubsystemBase {
    private final ShooterIO io;
    private final ShooterIOInputs inputs = new ShooterIOInputs();

    private double setpoint = 0;
    private double forcedSetpoint = -1;

    private final static Shooter INSTANCE = new Shooter(Constants.ROBOT_SHOOTER_IO);
    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter(ShooterIO io) {
        this.io = io;
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

        if (RobotState.getInstance().isClimbing()) {
            io.setVelocity(0, 0);
        }
        else if (forcedSetpoint != -1) {
            io.setVelocity(forcedSetpoint, 0);
        }
        else if (setpoint != 0) {
            io.setVelocity(setpoint, 0);
        }
        else if (Constants.SHOOTER_IDLE_ENABLED) {
//            use robot pose to estimate speed -200
            io.setVelocity(Constants.SHOOTER_SPEED_MAP.lookup(RobotState.getInstance().getDistanceToHub())-200, 0);
        }
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
        return BetterMath.epsilonEquals(getVelocity(), forcedSetpoint,50);
    }

}