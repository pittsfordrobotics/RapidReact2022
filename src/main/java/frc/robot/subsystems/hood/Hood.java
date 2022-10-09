package frc.robot.subsystems.hood;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DriverStation;
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
        hoodTab.addNumber("Absolute with Offset Angle Rad", this::getAbsoluteWithOffset);
        hoodTab.addNumber("Absolute Encoder Angle Rad", () -> inputs.absolutePositionRad);
        io.updateInputs(inputs);
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
        if (RobotState.getInstance().isClimbing()) {
            moveHood(0);
        }
        else if (forcedPosition != -1) {
            moveHood(forcedPosition);
        }
        else if (position != -1) {
            moveHood(position);
        }
        else if (Constants.ROBOT_IDLE_SHOOTER_ENABLED && !DriverStation.isAutonomous()) {
            moveHood(Constants.HOOD_ANGLE_MAP.lookup(RobotState.getInstance().getDistanceToHub()));
        }
//        TODO: this
//        io.setVoltage(pid.calculate(getAbsoluteWithOffset()));
    }

    private double getAbsoluteWithOffset() {
        return Constants.HOOD_ANGLE_OFFSET_RAD - inputs.absolutePositionRad;
    }

    private void moveHood(double targetPosition) {
        pid.setGoal(MathUtil.clamp(targetPosition, Constants.HOOD_ANGLE_MIN_RAD, Constants.HOOD_ANGLE_MAX_RAD));
    }

    public void setVoltage(double voltage) {
//        + is up
//        - is down
        if (getAbsoluteWithOffset() <= 0) {
            io.setVoltage(voltage < 0 ? 0 : voltage);
        }
        else if (getAbsoluteWithOffset() >= Constants.HOOD_ANGLE_OFFSET_RAD - Constants.HOOD_ANGLE_MAX_RAD) {
            io.setVoltage(voltage > 0 ? 0 : voltage);
        }
        else {
            io.setVoltage(voltage);
        }
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
        return (pid.getGoal().position - pid.getSetpoint().position < Constants.HOOD_TOLERANCE) || Constants.ROBOT_DEMO_MODE;
    }
}