package frc.robot.subsystems.hood;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotState;
import frc.robot.subsystems.hood.HoodIO.HoodIOInputs;
import frc.robot.util.PIDTuner;
import org.littletonrobotics.junction.Logger;

public class Hood extends SubsystemBase {
    private final HoodIO io;
    private final HoodIOInputs inputs = new HoodIOInputs();
    private final Timer timer = new Timer();

    private double position = Constants.ROBOT_IDLE_SHOOTER_ENABLED ? -1 : 0;
    private double forcedPosition = -1;

    private final ProfiledPIDController pid = new ProfiledPIDController(0,0,0, new TrapezoidProfile.Constraints(10,2));
    private final PIDTuner tuner = new PIDTuner("Hood", pid);

    private final static Hood INSTANCE = new Hood(Constants.ROBOT_HOOD_IO);

    public static Hood getInstance() {
        return INSTANCE;
    }

    private Hood(HoodIO io) {
        this.io = io;
        ShuffleboardTab hoodTab = Shuffleboard.getTab("Hood");
        hoodTab.addNumber("Absolute with Offset Angle", this::getAbsoluteWithOffset);
        hoodTab.addNumber("Absolute Encoder Angle", () -> inputs.absolutePosition);
        hoodTab.addNumber("Absolute Velocity", () -> inputs.absoluteVelocity);
        io.updateInputs(inputs);
//        SmartDashboard.putNumber("Hood Angle", 0);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Hood", inputs);
        Logger.getInstance().recordOutput("Hood/Forced Angle", forcedPosition);
        Logger.getInstance().recordOutput("Hood/Set Angle", position);
        Logger.getInstance().recordOutput("Hood/Idle Position", Constants.HOOD_ANGLE_MAP.lookup(RobotState.getInstance().getDistanceToHub()));
        Logger.getInstance().recordOutput("Hood/At Goal", atGoal());

        tuner.setPID(); // tune hood
//        moveHood(SmartDashboard.getNumber("Hood Angle", 0));
        if (RobotState.getInstance().isClimbing()) {
            moveHood(0);
        }
        else if (forcedPosition != -1) {
            moveHood(forcedPosition);
        }
        else if (position != -1) {
            moveHood(position);
        }
        else {
//            idle hood position is in center
//            moveHood(Constants.HOOD_ANGLE_MAX/2);
        }
//        TODO: this sucks
//        io.setVoltage(pid.calculate(getAbsoluteWithOffset()));
    }

    public double getAbsoluteVelocity() {
        return inputs.absoluteVelocity;
    }

    public void resetCounter() {
        io.resetCounter();
    }

    private double getAbsoluteWithOffset() {
        return inputs.absolutePosition - Constants.HOOD_ANGLE_OFFSET;
    }

    private void moveHood(double targetPosition) {
        pid.setGoal(MathUtil.clamp(targetPosition, Constants.HOOD_ANGLE_MIN, Constants.HOOD_ANGLE_MAX));
    }

    public void setVoltage(double voltage) {
//        + is up
//        - is down
//        if (getAbsoluteWithOffset() <= 0) {
//            io.setVoltage(voltage < 0 ? 0 : voltage);
//        }
//        else if (getAbsoluteWithOffset() >= Constants.HOOD_ANGLE_OFFSET - Constants.HOOD_ANGLE_MAX) {
//            io.setVoltage(voltage > 0 ? 0 : voltage);
//        }
//        else {
            io.setVoltage(voltage);
//        }
    }

    /** Min: 0, Max: 76.5 */
    public void setAngle(double angle, boolean forced) {
        if (!forced) {
            this.position = angle;
        }
        else {
            forcedPosition = angle;
        }
    }

    public boolean atGoal() {
        return (pid.getGoal().position - pid.getSetpoint().position < Constants.HOOD_TOLERANCE);
    }
}