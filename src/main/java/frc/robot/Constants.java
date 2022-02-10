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
    public static final class Drive {
        public static final int DRIVE_CAN_RIGHT_LEADER = 1;
        public static final int DRIVE_CAN_RIGHT_FOLLOWER = 2;
        public static final int DRIVE_CAN_LEFT_LEADER = 3;
        public static final int DRIVE_CAN_LEFT_FOLLOWER = 4;

        //these should be ideally set from Shuffleboard for ease of PID tuning; can keep constants for initial values
        public static final double DRIVE_kP = Double.NaN;
        public static final double DRIVE_kI = Double.NaN;
        public static final double DRIVE_kD = Double.NaN;

        public static final double DRIVE_S = Double.NaN; // static gain
        public static final double DRIVE_V = Double.NaN; // velocity gain
        public static final double DRIVE_A = Double.NaN; // acceleration gain

        public static final double DRIVE_WHEEL_DIAMETER = 0.1524; // meters
        public static final double DRIVE_GEAR_RATIO = Double.NaN;

        public static final double DRIVE_TRACK_WIDTH = 0.644; // meters
    }

/**
 *
 * INTAKE
 *
 */
    public static final class Intake {
        public static final int INTAKE_CAN_MAIN = 0; // choose CAN

        public static final int INTAKE_PNEUMATIC_LEFT_FORWARD = 0;
        public static final int INTAKE_PNEUMATIC_LEFT_REVERSE = 1;

        public static final int INTAKE_PNEUMATIC_RIGHT_FORWARD = 2;
        public static final int INTAKE_PNEUMATIC_RIGHT_REVERSE = 3;
        public static final double INTAKE_MAIN_SPEED = 1; // figure out speed
    }

/**
 *
 * LIMELIGHT
 * all distances measured in inches
 *
 **/
    public static final class Limelight {
        //    104 inches to top of goal
        //    101.625 inches to bottom of vision target
        //    middle is 102.8125 inches from field
        public static final double LIMELIGHT_TARGET_HEIGHT = 102.8125;
        public static final double LIMELIGHT_MOUNTING_HEIGHT = Double.NaN;
        public static final double LIMELIGHT_ANGLE = Double.NaN;
    }
}