// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.sensors.Pigeon2Configuration;
import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.util.LookupTable;

import java.util.List;

public final class Constants {
/**
*
* DRIVE
*
*/
    public static final int DRIVE_CAN_PIGEON = 0;
    public static final int DRIVE_CAN_RIGHT_LEADER = 1;
    public static final int DRIVE_CAN_RIGHT_FOLLOWER = 2;
    public static final int DRIVE_CAN_LEFT_LEADER = 3;
    public static final int DRIVE_CAN_LEFT_FOLLOWER = 4;

    public static final double DRIVE_RATE_LIMIT = 1;
    public static final SPI.Port DRIVE_NAVX = SPI.Port.kMXP;

    public static final Pigeon2Configuration DRIVE_PIGEON_CONFIG = new Pigeon2Configuration();

    static {
        DRIVE_PIGEON_CONFIG.EnableCompass = false;
    }

    public static final double DRIVE_GEAR_RATIO = 6.818;
    public static final double DRIVE_WHEEL_DIAMETER_METERS = Units.inchesToMeters(6);

    public static final double DRIVE_POSITION_GAIN = Double.NaN;
    public static final double DRIVE_INTEGRAL_GAIN = Double.NaN;
    public static final double DRIVE_DERIVATIVE_GAIN = Double.NaN;

    public static final double DRIVE_STATIC_GAIN = Double.NaN;
    public static final double DRIVE_VELOCITY_GAIN = Double.NaN;
    public static final double DRIVE_ACCELERATION_GAIN = Double.NaN;

    public static final double DRIVE_MAX_VELOCITY_METERS_PER_SECOND = Double.NaN;
    public static final double DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED = Double.NaN;

    public static final double DRIVE_TRACK_WIDTH_METERS = 0.644;

/**
 *
 * INTAKE
 *
 */
    public static final int INTAKE_CAN_MAIN = 5;

    public static final double INTAKE_MAIN_SPEED = 1; // figure out speed

    public static final int INTAKE_PNEUMATIC_LEFT_FORWARD = 0;
    public static final int INTAKE_PNEUMATIC_LEFT_REVERSE = 1;

    public static final int INTAKE_PNEUMATIC_RIGHT_FORWARD = 2;
    public static final int INTAKE_PNEUMATIC_RIGHT_REVERSE = 3;

/**
 *
 * HOOD
 *
 */
    public static final int HOOD_CAN_MAIN = 6;

    public static final double HOOD_POSITION_GAIN = Double.NaN;
    public static final double HOOD_INTEGRAL_GAIN = Double.NaN;
    public static final double HOOD_DERIVATIVE_GAIN = Double.NaN;
    public static final double HOOD_ROTATIONS_TO_DEGREES = Double.NaN;

    public static final float HOOD_POSITION_MAX = Float.NaN;
    public static final float HOOD_POSITION_LOW = Float.NaN;

    public static final LookupTable HOOD_TABLE = new LookupTable();

    static {
        HOOD_TABLE.put(0, 0);
    }

/**
 *
 * INDEXER
 *
 */
    public static final int INDEXER_CAN_STOMACH = 7;
    public static final int INDEXER_CAN_TOWER = 8;

    public static final double INDEXER_STOMACH_SPEED = 0.6;
    public static final double INDEXER_TOWER_SPEED = 0.6;

    public static final I2C.Port INDEXER_COLOR = I2C.Port.kOnboard;
    public static final int INDEXER_COLOR_PROXIMITY = 500; // from 0 to 2047

    public static final int INDEXER_SENSOR_TOWER = 0;
    public static final int INDEXER_SENSOR_SHOOTER = 1;

/**
 *
 * SHOOTER
 *
 */
    public static final int SHOOTER_CAN_MAIN = 9;

    public static final double SHOOTER_STATIC_GAIN = Double.NaN;
    public static final double SHOOTER_VELOCITY_GAIN = Double.NaN;
    public static final double SHOOTER_ACCELERATION_GAIN = Double.NaN;

    public static final double SHOOTER_LOW_SPEED = Double.NaN;

    public static final LookupTable SHOOTER_TABLE = new LookupTable();

    static {
        SHOOTER_TABLE.put(0,3000);
    }

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

/**
 *
 * CLIMBER
 * 
 */
    public static final int CLIMBER_CAN_LEFT = 10;
    public static final int CLIMBER_CAN_RIGHT = 11;

    public static final double CLIMBER_SPEED = Double.NaN;
    public static final double CLIMBER_GEAR_RATIO = 125;
    public static final double CLIMBER_ROTATIONS_FRONT_TO_CENTER = Double.NaN;

/**
 *
 * FIELD DIMENSIONS
 * https://firstfrc.blob.core.windows.net/frc2022/FieldAssets/2022LayoutMarkingDiagram.pdf
 *
 */
    public static final double FIELD_TARMAC_LINE_TO_BALL_METERS = Units.inchesToMeters(40.44);
    public static final double FIELD_BALL_TO_FENDER_METERS = Units.inchesToMeters(116.17);
    public static final double FIELD_TARMAC_OUTSIDE_TIP_TO_FENDER_METERS = Units.inchesToMeters(84.75);
    public static final double FIELD_TARMAC_OUTSIDE_EDGE_METERS = Units.inchesToMeters(82.83);

/**
 *
 * TRAJECTORY:
 * x represents forward backward
 * y represents right left
 *
 * ALL IN METERS
 *
 */
    private static final TrajectoryConfig TRAJECTORY_CONFIG = new TrajectoryConfig(DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
            .setKinematics(new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH_METERS))
            .addConstraint(
                    new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(DRIVE_STATIC_GAIN, DRIVE_VELOCITY_GAIN, DRIVE_ACCELERATION_GAIN),
                            new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH_METERS),
                            10)
            );

    private static final TrajectoryConfig TRAJECTORY_CONFIG_REVERSED = new TrajectoryConfig(DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
            .setKinematics(new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH_METERS))
            .setReversed(true)
            .addConstraint(
                new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(DRIVE_STATIC_GAIN, DRIVE_VELOCITY_GAIN, DRIVE_ACCELERATION_GAIN),
                            new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH_METERS),
                            10)
            );

    public static final Trajectory TRAJECTORY_BACKWARD = TrajectoryGenerator.generateTrajectory(
            List.of(
                    new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
                    new Pose2d(-1, 0, Rotation2d.fromDegrees(0))
            ),
            TRAJECTORY_CONFIG_REVERSED
    );

    public static final Trajectory TRAJECTORY_PATHPLANNER_LEFT_BALL2_ALL1 = PathPlanner.loadPath("LeftBall2All1", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_LEFT_BALL2_LOW2 = PathPlanner.loadPath("LeftBall2Low2", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_ALL1 = PathPlanner.loadPath("BottomBall2All1", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_LOW2 = PathPlanner.loadPath("BottomBall2Low2", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL3_LOW3 = PathPlanner.loadPath("Ball3Low3", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL3_LOW4 = PathPlanner.loadPath("Ball3Low4", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL5_LOW3 = PathPlanner.loadPath("Ball5Low3", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL5_LOW4 = PathPlanner.loadPath("Ball5Low4", 10, 3);

//    TODO: make these exist
    public static final Trajectory TRAJECTORY_PATHPLANNER_LEFT_BALL2_HIGH2 = PathPlanner.loadPath("LeftBall2High2", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_HIGH2 = PathPlanner.loadPath("BottomBall2High2", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL3_HIGH3 = PathPlanner.loadPath("Ball3High3", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL3_HIGH4 = PathPlanner.loadPath("Ball3High4", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL5_HIGH3 = PathPlanner.loadPath("Ball5High3", 10, 3);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL5_HIGH4 = PathPlanner.loadPath("Ball5High4", 10, 3);
}