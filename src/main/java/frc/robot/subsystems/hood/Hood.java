package frc.robot.subsystems.hood;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
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

    private double position = 0;
    private double forcedPosition = -1;

    private final PIDController pid = new PIDController(0,0,0);
    private final PIDTuner tuner = new PIDTuner("Hood", pid);

    private final static Hood INSTANCE = new Hood(Constants.ROBOT_HOOD_IO);

    public static Hood getInstance() {
        return INSTANCE;
    }

    private Hood(HoodIO io) {
        this.io = io;
        ShuffleboardTab hoodTab = Shuffleboard.getTab("Hood");
        hoodTab.addNumber("Encoder Angle Rad", () -> inputs.positionRad);
        io.updateInputs(inputs);
        io.setSoftLimit(true, (float)(0 - getAbsoluteWithOffset()), (float)(Constants.HOOD_ANGLE_MAX_RAD - getAbsoluteWithOffset()));
        pid.setTolerance(3);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Hood", inputs);
        Logger.getInstance().recordOutput("Hood/At Setpoint", atSetpoint());

        tuner.setPID(); // tune hood
        if (RobotState.getInstance().isClimbing()) {
            moveHood(0);
        }
        else if (forcedPosition != -1) {
            moveHood(forcedPosition);
        }
        else if (position != 0) {
            moveHood(position);
        }
        else if (Constants.ROBOT_IDLE_SHOOTER_ENABLED) {
            moveHood(Constants.HOOD_ANGLE_MAP.lookup(RobotState.getInstance().getDistanceToHub()));
        }
    }

    private double getAbsoluteWithOffset() {
        return Constants.HOOD_ANGLE_OFFSET_RAD - inputs.absolutePositionRad;
    }

    private void moveHood(double targetPosition) {
        io.setVoltage(pid.calculate(MathUtil.clamp(targetPosition-getAbsoluteWithOffset(), Constants.HOOD_ANGLE_MIN_RAD, Constants.HOOD_ANGLE_MAX_RAD)));
    }

    public void setVoltage(double voltage) {
//        + is up
//        - is down
        io.setVoltage(voltage);
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

    public boolean atSetpoint() {
        return pid.atSetpoint() || Constants.ROBOT_DEMO_MODE;
    }
}