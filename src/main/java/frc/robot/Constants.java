// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;

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
 * TRA JECTORY
 * 
 * 
 */
    public static final Trajectory simpleForward = TrajectoryGenerator.generateTrajectory(
        List.of(
            new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
            new Pose2d(1, -1, Rotation2d.fromDegrees(-90)),
            new Pose2d(0, -2, Rotation2d.fromDegrees(-180)),
            new Pose2d(-1, -1, Rotation2d.fromDegrees(-270)),
            new Pose2d(0, 0, Rotation2d.fromDegrees(0))
        ),
        new TrajectoryConfig(1.5, 1)
        .setKinematics(new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH))
        .addConstraint(
            new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(DRIVE_S, DRIVE_V, DRIVE_A),
            new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH),
            10)
        )
    );
}