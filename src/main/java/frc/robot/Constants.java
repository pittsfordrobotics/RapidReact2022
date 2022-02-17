// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
 * SHOOTER
 *
 */
    public static final int SHOOTER_CAN_MAIN = 5;

    public static final double SHOOTER_STATIC_GAIN = Double.NaN;
    public static final double SHOOTER_VELOCITY_GAIN = Double.NaN;
    public static final double SHOOTER_ACCELERATION_GAIN = Double.NaN;

    public static final double SHOOTER_LOW_GOAL_SPEED = Double.NaN;

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
}