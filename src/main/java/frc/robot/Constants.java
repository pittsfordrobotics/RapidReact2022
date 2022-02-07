// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.SPI;
import frc.robot.util.Units;

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
    public static final SPI.Port DRIVE_NAVX = SPI.Port.kMXP;

    public static final double DRIVE_P = Double.NaN;
    public static final double DRIVE_I = Double.NaN;
    public static final double DRIVE_D = Double.NaN;

    public static final double DRIVE_S = Double.NaN; // static gain
    public static final double DRIVE_V = Double.NaN; // velocity gain
    public static final double DRIVE_A = Double.NaN; // acceleration gain

    public static final double DRIVE_WHEEL_DIAMETER_METERS = Units.inches_to_meters(6);
    public static final double DRIVE_GEAR_RATIO = 7.31;

    public static final double DRIVE_TRACK_WIDTH_METERS = 0.644;
/**
 *
 * LIMELIGHT
 * all distances measured in inches
 *
 **/

//    104 inches to top of goal
//    101.625 inches to bottom of vision target
//    middle is 102.8125 inches from field
    public static final double LIMELIGHT_TARGET_HEIGHT_INCHES = 102.8125;
    public static final double LIMELIGHT_MOUNTING_HEIGHT = Double.NaN;
    public static final double LIMELIGHT_ANGLE = Double.NaN;
}