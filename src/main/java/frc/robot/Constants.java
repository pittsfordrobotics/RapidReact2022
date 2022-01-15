// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
/**
 *
 * DRIVE
 *
 */
    public static final int kDriveCANRightLeader = 1;
    public static final int kDriveCANRightFollower = 2;
    public static final int kDriveCANLeftLeader = 3;
    public static final int kDriveCANLeftFollower = 4;

    public static final int kDriveP = (int) Double.NaN;
    public static final int kDriveI = (int) Double.NaN;
    public static final int kDriveD = (int) Double.NaN;

//    measured in meters
    public static final double kDriveWheelDiameter = Double.NaN;
    public static final double kDriveGearRatio = Double.NaN;

/**
 *
 * LIMELIGHT
 * all distances measured in inches
 *
 **/

//    104 inches to top of goal
//    101.625 inches to bottom of vision target
//    middle is 102.8125 inches from field
    public static final double kLimelightTargetHeight = 102.8125;
    public static final double kLimelightRobotHeight = Double.NaN;
    public static final double kLimelightAngle = Double.NaN;
}