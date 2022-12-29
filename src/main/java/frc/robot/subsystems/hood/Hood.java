package frc.robot.subsystems.hood;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotState;
import frc.robot.subsystems.hood.HoodIO.HoodIOInputs;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import org.jetbrains.annotations.NotNull;
import org.littletonrobotics.junction.Logger;

public class Hood extends SubsystemBase {
    private final HoodIO io;
    private final HoodIOInputs inputs = new HoodIOInputs();
    private double position = Constants.ROBOT_IDLE_SHOOTER_ENABLED ? -1 : 0;
    private double forcedPosition = -1;
    private boolean zeroed = false;
    private final PIDController pid = new PIDController(12, 0, 0);

    private final Alert zeroAlert = new Alert("Hood has NOT been zeroed!", AlertType.ERROR);

    private final static Hood INSTANCE = new Hood(Constants.ROBOT_HOOD_IO);

    public static Hood getInstance() {
        return INSTANCE;
    }

    private Hood(@NotNull HoodIO io) {
        this.io = io;
        ShuffleboardTab hoodTab = Shuffleboard.getTab("Hood");
        hoodTab.addNumber("Absolute with Offset Angle", this::getAbsoluteWithOffset);
        hoodTab.addNumber("Absolute Encoder Angle", () -> inputs.absolutePosition);
        hoodTab.addNumber("Absolute Velocity", () -> inputs.absoluteVelocity);
        hoodTab.addBoolean("Limit Switch", this::getLimit);
        io.updateInputs(inputs);
//        SmartDashboard.putNumber("Hood Angle", 0);
    }

    @Override
    public void periodic() {
        zeroAlert.set(!zeroed);

        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Hood", inputs);
        Logger.getInstance().recordOutput("Hood/Zeroed", zeroed);
        Logger.getInstance().recordOutput("Hood/Forced Angle", forcedPosition);
        Logger.getInstance().recordOutput("Hood/Set Angle", position);
        Logger.getInstance().recordOutput("Hood/Idle Position", Constants.HOOD_ANGLE_MAP.lookup(RobotState.getInstance().getDistanceToHub()));
        Logger.getInstance().recordOutput("Hood/At Goal", atGoal());

//        double targetAngle = SmartDashboard.getNumber("Hood Angle", 0);
//        moveHood(targetAngle);
        if (zeroed) {
            if (RobotState.getInstance().isClimbing()) {
                moveHood(0);
            } else if (forcedPosition != -1) {
                moveHood(forcedPosition);
            } else if (position != -1) {
                moveHood(position);
            }
        }

        if (getLimit()) {
            resetCounter();
        }
    }

    public void zeroed() {
        zeroed = true;
    }


    public boolean getLimit() {
        return !inputs.limit;
    }

    public void resetCounter() {
        io.resetCounter();
    }

    private double getAbsoluteWithOffset() {
        return Constants.HOOD_ANGLE_OFFSET - inputs.absolutePosition;
    }

    private void moveHood(double targetAngle) {
        double pidResult = pid.calculate(getAbsoluteWithOffset() - targetAngle);
        setVoltage(pidResult + (Math.abs(pidResult) < 0.05 ? 0 : Math.copySign(1.5, pidResult)), false);
    }

    /**
     * @param voltage (+) is up (-) is down
     * @param reset   ignores soft limits
     */
    public void setVoltage(double voltage, boolean reset) {
        if (reset) {
            io.setVoltage(voltage);
        } else if (getLimit() || getAbsoluteWithOffset() <= 0) {
            io.setVoltage(voltage < 0 ? 0 : voltage);
        } else if (getAbsoluteWithOffset() >= Constants.HOOD_ANGLE_MAX) {
            io.setVoltage(voltage > 0 ? 0 : voltage);
        } else {
            io.setVoltage(voltage);
        }

    }

    /**
     * @param angle  Min: 0, Max: 1.27 rad; this is clamped
     * @param forced this will have a higher priority
     */
    public void setAngle(double angle, boolean forced) {
        double clampedAngle = MathUtil.clamp(angle, Constants.HOOD_ANGLE_MIN, Constants.HOOD_ANGLE_MAX);
        if (forced) {
            this.forcedPosition = clampedAngle;
        }
        else {
            this.position = clampedAngle;
        }
    }

    public boolean atGoal() {
        return pid.atSetpoint();
    }
}