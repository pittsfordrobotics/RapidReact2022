// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

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
import frc.robot.util.Units;
import java.util.List;

public final class Constants {
/**
*
* DRIVE
*
*/
    public static final int DRIVE_CAN_RIGHT_LEADER = 1;
    public static final int DRIVE_CAN_RIGHT_FOLLOWER = 2;
    public static final int DRIVE_CAN_LEFT_LEADER = 3;
    public static final int DRIVE_CAN_LEFT_FOLLOWER = 4;

    public static final double DRIVE_POSITION_GAIN = Double.NaN;
    public static final double DRIVE_INTEGRAL_GAIN = Double.NaN;
    public static final double DRIVE_DERIVATIVE_GAIN = Double.NaN;

    public static final double DRIVE_STATIC_GAIN = Double.NaN;
    public static final double DRIVE_VELOCITY_GAIN = Double.NaN;
    public static final double DRIVE_ACCELERATION_GAIN = Double.NaN;

    public static final double DRIVE_WHEEL_DIAMETER = 0.1524; // meters
    public static final double DRIVE_GEAR_RATIO = Double.NaN;

    public static final double DRIVE_MAX_VELOCITY = Double.NaN; // um
    public static final double DRIVE_MAX_ACCELERATION = Double.NaN; // yeah idk

    public static final double DRIVE_TRACK_WIDTH = 0.644; // meters

/**
 *
 * LIMELIGHT
 * all distances measured in inches
 *
 **/
//    104 inches to top of goal
//    101.625 inches to bottom of vision target
//    middle is 102.8125 inches from field
    public static final double LIMELIGHT_TARGET_HEIGHT = 102.8125;
    public static final double LIMELIGHT_MOUNTING_HEIGHT = Double.NaN;
    public static final double LIMELIGHT_ANGLE = Double.NaN;

/**
 *
 * FIELD DIMENSIONS
 * https://firstfrc.blob.core.windows.net/frc2022/FieldAssets/2022LayoutMarkingDiagram.pdf
 *
 */
    public static final double FIELD_TARMAC_LINE_TO_BALL_METERS = Units.inches_to_meters(40.44);
    public static final double FIELD_BALL_TO_FENDER_METERS = Units.inches_to_meters(116.17);
    public static final double FIELD_TARMAC_OUTSIDE_TIP_TO_FENDER_METERS = Units.inches_to_meters(84.75);
    public static final double FIELD_TARMAC_OUTSIDE_EDGE_METERS = Units.inches_to_meters(82.83);

/**
 *
 * TRAJECTORY:
 * x represents forward backward
 * y represents right left
 *
 * ALL IN METERS
 *
 */
    private static final TrajectoryConfig TRAJECTORY_CONFIG = new TrajectoryConfig(DRIVE_MAX_VELOCITY, DRIVE_MAX_ACCELERATION)
            .setKinematics(new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH))
            .addConstraint(
                    new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(DRIVE_STATIC_GAIN, DRIVE_VELOCITY_GAIN, DRIVE_ACCELERATION_GAIN),
                            new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH),
                            10)
            );

    private static final TrajectoryConfig TRAJECTORY_CONFIG_REVERSED = new TrajectoryConfig(DRIVE_MAX_VELOCITY, DRIVE_MAX_ACCELERATION)
            .setKinematics(new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH))
            .setReversed(true)
            .addConstraint(
                new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(DRIVE_STATIC_GAIN, DRIVE_VELOCITY_GAIN, DRIVE_ACCELERATION_GAIN),
                            new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH),
                            10)
            );

    public static final Trajectory TRAJECTORY_FORWARD = TrajectoryGenerator.generateTrajectory(
            List.of(
                    new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
                    new Pose2d(1, 0, Rotation2d.fromDegrees(0))
            ),
            TRAJECTORY_CONFIG
    );

    public static final Trajectory TRAJECTORY_PATHPLANNER_TEST = PathPlanner.loadPath("Test", 10, 5);
}