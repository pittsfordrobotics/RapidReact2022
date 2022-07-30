package frc.robot.subsystems.hood;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
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

    private final ProfiledPIDController pid = new ProfiledPIDController(0,0,0, new Constraints(10, 1));
    private final PIDTuner tuner = new PIDTuner("Hood", pid);

    private final static Hood INSTANCE = new Hood(Constants.ROBOT_HOOD_IO);

    public static Hood getInstance() {
        return INSTANCE;
    }

    private Hood(HoodIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Hood", inputs);
        tuner.setPID(true, true, true);
        if (RobotState.getInstance().isClimbing()) {
            moveHood(0);
        }
        else if (forcedPosition != -1) {
            moveHood(forcedPosition);
        }
        else if (position != 0) {
            moveHood(position);
        }
        else {
            moveHood(Constants.HOOD_ANGLE_MAP(RobotState.getInstance().getDistanceToHub()));
        }
    }

    private double getAbsoluteWithOffset() {
        return inputs.absolutePositionRad - Constants.HOOD_REV_THROUGH_BORE_OFFSET;
    }

    private void moveHood(double degrees) {
        io.setVoltage(pid.calculate(MathUtil.clamp(degrees-getAbsoluteWithOffset(), 0, Constants.HOOD_ANGLE_MAX)));
    }

    /** Min: 0, Max: UNKNOWN */
    public void setAngle(double angle, boolean forced) {
        if (!forced) {
            this.position = angle;
        }
        else {
            forcedPosition = angle;
        }
    }

    public boolean atAngle() {
        return Math.abs(getAbsoluteWithOffset()-(forcedPosition != -1 ? forcedPosition : position)) < 5;
    }
}