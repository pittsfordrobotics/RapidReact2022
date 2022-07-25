package frc.robot.subsystems.shooter;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.shooter.ShooterIO.ShooterIOInputs;
import org.littletonrobotics.junction.Logger;

public class Shooter extends SubsystemBase {
    private final ShooterIO io;
    private final ShooterIOInputs inputs = new ShooterIOInputs();

    private double speed = 0;
    private double setpoint = 0;
    private double forcedSetpoint = -1;

    private final static Shooter INSTANCE = new Shooter(new ShooterIOSparkMax());
    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter(ShooterIO io) {
        this.io = io;
        ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
        shooterTab.addNumber("Shooter Target RPM", () -> speed);
        shooterTab.addNumber("Shooter Actual", this::getVelocity);
        shooterTab.addBoolean("Shooter up to Speed", this::isAtSpeed);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Shooter", inputs);
        Logger.getInstance().recordOutput("Shooter/TargetRMP", setpoint);
        Logger.getInstance().recordOutput("Shooter/ForcedRMP", forcedSetpoint);
        Logger.getInstance().recordOutput("Shooter/ActualRMP", getVelocity());
        if (forcedSetpoint == -1) {
            io.setVelocity(Units.rotationsPerMinuteToRadiansPerSecond(setpoint), 0);
        }
        else {
            io.setVelocity(Units.rotationsPerMinuteToRadiansPerSecond(forcedSetpoint), 0);
        }
        io.set(0.0002 * speed);
    }

    public void updateSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @param setpoint -1 is disabled for forced
     */
    public void updateSetpoint(double setpoint, boolean forced) {
        if (!forced) {
            this.setpoint = setpoint;
        }
        else {
            forcedSetpoint = setpoint;
        }
    }

    public double getVelocity() {
        return Units.radiansPerSecondToRotationsPerMinute(inputs.velocityRadPerSec);
    }

    public void shootLow() {
        this.speed = Constants.SHOOTER_LOW_SPEED;
    }

    public void shootStop() {
        this.speed = 0;
    }

    public boolean isAtSpeed() {
        return getVelocity() > 0.9 * speed;
    }

    public boolean isAtSetpoint() {
        return Math.abs(getVelocity()-(forcedSetpoint != -1 ? forcedSetpoint : setpoint)) < 100;
    }

}