package frc.robot;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.util.PathPlannerHelper;

import java.util.List;

public final class Trajectories {
    /**
     *
     * TRAJECTORY:
     * x represents forward backward
     * y represents right left
     * ALL IN METERS
     *
     */
    private static final TrajectoryConfig CONFIG_FORWARD = new TrajectoryConfig(Constants.DRIVE_MAX_VELOCITY_METERS_PER_SECOND, Constants.DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
            .setKinematics(new DifferentialDriveKinematics(Constants.DRIVE_TRACK_WIDTH_METERS))
            .addConstraint(
                    new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(Constants.DRIVE_STATIC_GAIN, Constants.DRIVE_VELOCITY_GAIN, Constants.DRIVE_ACCELERATION_GAIN),
                            new DifferentialDriveKinematics(Constants.DRIVE_TRACK_WIDTH_METERS),
                            10)
            );

    private static final TrajectoryConfig CONFIG_REVERSED = new TrajectoryConfig(Constants.DRIVE_MAX_VELOCITY_METERS_PER_SECOND, Constants.DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
            .setKinematics(new DifferentialDriveKinematics(Constants.DRIVE_TRACK_WIDTH_METERS))
            .setReversed(true)
            .addConstraint(
                    new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(Constants.DRIVE_STATIC_GAIN, Constants.DRIVE_VELOCITY_GAIN, Constants.DRIVE_ACCELERATION_GAIN),
                            new DifferentialDriveKinematics(Constants.DRIVE_TRACK_WIDTH_METERS),
                            10)
            );

    public static final Trajectory THREE_METERS_BACKWARD = TrajectoryGenerator.generateTrajectory(
            List.of(
                    new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
                    new Pose2d(-3, 0, Rotation2d.fromDegrees(0))
            ),
            CONFIG_REVERSED
    );

    public static final Trajectory ONE_METER_BACKWARD = TrajectoryGenerator.generateTrajectory(
            List.of(
                    new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
                    new Pose2d(-1, 0, Rotation2d.fromDegrees(0))
            ),
            CONFIG_REVERSED
    );

    public static final Trajectory PP_TOP_BALL2_REJECT1_NUMBER1 = PathPlannerHelper.loadPath("TopBall2Reject1N1");
    public static final Trajectory PP_TOP_BALL2_REJECT1_NUMBER2 = PathPlannerHelper.loadPath("TopBall2Reject1N2");

    public static final Trajectory PP_TOP_LEFT_BALL2_REJECT2_NUMBER1 = PathPlannerHelper.loadPath("TopLeftBall2Reject2N1");
    public static final Trajectory PP_TOP_LEFT_BALL2_REJECT2_NUMBER2 = PathPlannerHelper.loadPath("TopLeftBall2Reject2N2");
    public static final Trajectory PP_TOP_LEFT_BALL2_REJECT2_NUMBER3 = PathPlannerHelper.loadPath("TopLeftBall2Reject2N3");

    public static final Trajectory PP_BALL5_NUMBER1 = PathPlannerHelper.loadPath("Ball5N1");
    public static final Trajectory PP_BALL5_NUMBER2 = PathPlannerHelper.loadPath("Ball5N2");
    public static final Trajectory PP_BALL5_NUMBER3 = PathPlannerHelper.loadPath("Ball5N3");
    public static final Trajectory PP_BALL5_NUMBER4 = PathPlannerHelper.loadPath("Ball5N4");
    public static final Trajectory PP_BALL5_NUMBER5 = PathPlanner.loadPath("Ball5N5", 0.3, Constants.DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED, true);
    public static final Trajectory PP_BALL5_NUMBER6 = PathPlannerHelper.loadPath("Ball5N6", true);

}