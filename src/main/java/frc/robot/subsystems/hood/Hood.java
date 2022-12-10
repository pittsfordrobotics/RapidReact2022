package frc.robot.subsystems.hood;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotState;
import frc.robot.subsystems.hood.HoodIO.HoodIOInputs;
import org.jetbrains.annotations.NotNull;
import org.littletonrobotics.junction.Logger;

public class Hood extends SubsystemBase {
    private final HoodIO io;
    private final HoodIOInputs inputs = new HoodIOInputs();
    private double position = Constants.ROBOT_IDLE_SHOOTER_ENABLED ? -1 : 0;
    private double forcedPosition = -1;
    private final PIDController pid = new PIDController(12, 0, 0);
    private final static Hood INSTANCE = new Hood(Constants.ROBOT_HOOD_IO);

    public static Hood getInstance() {
        return INSTANCE;
    }

    private Hood(@NotNull HoodIO io) {
        this.io = io;
        pid.setTolerance(0.1);
        ShuffleboardTab hoodTab = Shuffleboard.getTab("Hood");
        hoodTab.addNumber("Absolute with Offset Angle", this::getAbsoluteWithOffset);
        hoodTab.addNumber("Absolute Encoder Angle", () -> inputs.absolutePosition);
        hoodTab.addNumber("Absolute Velocity", () -> inputs.absoluteVelocity);
        hoodTab.addBoolean("Limit Switch", this::getLimit);
        io.updateInputs(inputs);
        SmartDashboard.putNumber("Hood Angle", 0);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Hood", inputs);
        Logger.getInstance().recordOutput("Hood/Forced Angle", forcedPosition);
        Logger.getInstance().recordOutput("Hood/Set Angle", position);
        Logger.getInstance().recordOutput("Hood/Idle Position", Constants.HOOD_ANGLE_MAP.lookup(RobotState.getInstance().getDistanceToHub()));
        Logger.getInstance().recordOutput("Hood/At Goal", atGoal());

//        io.setPID(); // tune hood
//        moveHood(SmartDashboard.getNumber("Hood Angle", 0));
        double targetAngle = SmartDashboard.getNumber("Hood Angle", 0);
//        if (RobotState.getInstance().isClimbing()) {
//            moveHood(0);
//        }
//        else if (forcedPosition != -1) {
//            moveHood(forcedPosition);
//        }
//        else if (position != -1) {
//            moveHood(position);
//        }
//        if (RobotState.getInstance().isClimbing()) {
//            if (MathUtil.applyDeadband(getAbsoluteWithOffset(), 0.1) > Constants.HOOD_ANGLE_MIN) {
//                setVoltage(-2, false);
//            }
//        }
//        // TODO: implement PID instead of this stuff.
//        int error = 1;
//        if ((getAbsoluteWithOffset() < position) && BetterMath.epsilonEquals(getAbsoluteWithOffset(), position, error)) {
//            setVoltage(2, false);
//        }
//        else if ((getAbsoluteWithOffset() > position) && BetterMath.epsilonEquals(getAbsoluteWithOffset(), position, error)) {
//            setVoltage(-2, false);
//        }
//        else {
//            setVoltage(0, false);
//        }
        if (getLimit()) {
            io.resetCounter();
        }
        double number = pid.calculate(getAbsoluteWithOffset() - targetAngle);
        setVoltage(number + (Math.abs(number) < 0.1 ? 0 : Math.copySign(1.5, number)), false);
    }

    public double getAbsoluteVelocity() {
        return inputs.absoluteVelocity;
    }

    public double getAbsolutePosition() {
        return inputs.absolutePosition;
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

    private void moveHood(double targetPosition) {
        setAngle(targetPosition, false);
//        pid.setSetpoint(MathUtil.clamp(targetPosition, Constants.HOOD_ANGLE_MIN, Constants.HOOD_ANGLE_MAX));
    }

    /**
     * @param voltage (+) is up (-) is down
     * @param reset   idk
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
     * @param angle  Min: 0, Max: 76.5 degrees
     * @param forced if indexer is in the state where it should not happen force overrides this
     */
    public void setAngle(double angle, boolean forced) {
        if (forced) {
            this.forcedPosition = angle;
        }
        else {
            this.position = angle;
        }
    }

    /**
     * idk?
     *
     * @return always true for some reason because we never got to test it
     */
    public boolean atGoal() {
        return true;
//        return (pid.atSetpoint());
    }
}