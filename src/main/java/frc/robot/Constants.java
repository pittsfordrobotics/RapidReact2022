// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

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

    public static final double DRIVE_P = Double.NaN;
    public static final double DRIVE_I = Double.NaN;
    public static final double DRIVE_D = Double.NaN;

    public static final double DRIVE_S = Double.NaN;
    public static final double DRIVE_V = Double.NaN;
    public static final double DRIVE_A = Double.NaN;

//    measured in meters
    public static final double DRIVE_WHEEL_DIAMETER = 0.1524;
    public static final double DRIVE_GEAR_RATIO = Double.NaN;

    public static final DifferentialDriveKinematics DRIVE_KINEMATICS = new DifferentialDriveKinematics(0.644);

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
    public static final double LIMELIGHT_ROBOT_HEIGHT = Double.NaN;
    public static final double LIMELIGHT_ANGLE = Double.NaN;
}