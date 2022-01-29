// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

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
    public static final int DriveCANRightLeader = 1;
    public static final int DriveCANRightFollower = 2;
    public static final int DriveCANLeftLeader = 3;
    public static final int DriveCANLeftFollower = 4;

    public static final double DriveP = Double.NaN;
    public static final double DriveI = Double.NaN;
    public static final double DriveD = Double.NaN;

    public static final double DriveS = Double.NaN;
    public static final double DriveV = Double.NaN;
    public static final double DriveA = Double.NaN;

//    measured in meters
    public static final double DriveWheelDiameter = 0.1524;
    public static final double DriveGearRatio = Double.NaN;

    public static final DifferentialDriveKinematics DriveKinematics = new DifferentialDriveKinematics(0.644);

/**
 *
 * LIMELIGHT
 * all distances measured in inches
 *
 **/

//    104 inches to top of goal
//    101.625 inches to bottom of vision target
//    middle is 102.8125 inches from field
    public static final double LimelightTargetHeight = 102.8125;
    public static final double LimelightRobotHeight = Double.NaN;
    public static final double LimelightAngle = Double.NaN;
}