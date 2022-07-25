//// Copyright (c) FIRST and other WPILib contributors.
//// Open Source Software; you can modify and/or share it under the terms of
//// the WPILib BSD license file in the root directory of this project.

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
import frc.robot.subsystems.climber.ClimberIO;
import frc.robot.subsystems.climber.ClimberIOSparkMax;
import frc.robot.subsystems.compressor7.CompressorIO;
import frc.robot.subsystems.compressor7.CompressorIORev;
import frc.robot.subsystems.drive.DriveIO;
import frc.robot.subsystems.drive.DriveIOSparkMax;
import frc.robot.subsystems.hood.HoodIO;
import frc.robot.subsystems.indexer.IndexerIO;
import frc.robot.subsystems.indexer.IndexerIOSparkMax;
import frc.robot.subsystems.intake.IntakeIO;
import frc.robot.subsystems.intake.IntakeIOSparkMax;
import frc.robot.subsystems.shooter.ShooterIO;
import frc.robot.subsystems.shooter.ShooterIOSparkMax;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.util.InterpolatingTreeMap;

import java.util.HashMap;
import java.util.List;

// NEW ROBOT
public final class Constants {
    /**
     *
     * ROBOT: General Constants
     *
     */
    public static final ClimberIO ROBOT_CLIMBER_IO = new ClimberIOSparkMax();
    public static final CompressorIO ROBOT_COMPRESSOR_IO = new CompressorIORev();
    public static final DriveIO ROBOT_DRIVE_IO = new DriveIOSparkMax();
    public static final HoodIO ROBOT_HOOD_IO = new HoodIO(){};
    public static final IndexerIO ROBOT_INDEXER_IO = new IndexerIOSparkMax();
    public static final IntakeIO ROBOT_INTAKE_IO = new IntakeIOSparkMax();
    public static final ShooterIO ROBOT_SHOOTER_IO = new ShooterIOSparkMax();
    public static final VisionIO ROBOT_VISION_IO = new VisionIOLimelight();

    public static final boolean ROBOT_PID_TUNER_ENABLED = true;
    public static final boolean ROBOT_LOGGING_ENABLED = true;
    public static final String ROBOT_PROJECT_NAME = "RapidReact2022";
    public static final String ROBOT_LOGGING_PATH = "/media/sda1/";

    public static final int ROBOT_PDP_CAN = 1;
    public static final int ROBOT_PNEUMATIC_HUB_CAN = 1;

    public static final HashMap<Integer, String> ROBOT_SPARKMAX_HASHMAP = new HashMap<Integer, String>();
    static {
        ROBOT_SPARKMAX_HASHMAP.put(1, "Left Drive Leader");
        ROBOT_SPARKMAX_HASHMAP.put(2, "Left Drive Follower");
        ROBOT_SPARKMAX_HASHMAP.put(3, "Right Drive Leader");
        ROBOT_SPARKMAX_HASHMAP.put(4, "Right Drive Follower");
        ROBOT_SPARKMAX_HASHMAP.put(5, "Climber Left");
        ROBOT_SPARKMAX_HASHMAP.put(6, "Climber Right");
        ROBOT_SPARKMAX_HASHMAP.put(7, "Stomach Right");
        ROBOT_SPARKMAX_HASHMAP.put(8, "Stomach Left");
        ROBOT_SPARKMAX_HASHMAP.put(9, "Tower");
        ROBOT_SPARKMAX_HASHMAP.put(10, "Intake");
        ROBOT_SPARKMAX_HASHMAP.put(11, "Shooter Left");
        ROBOT_SPARKMAX_HASHMAP.put(12, "Shooter Right");
        ROBOT_SPARKMAX_HASHMAP.put(13, "Hood");
    }

    /**
     *
     * DRIVE
     *
     */
    public static final int DRIVE_CAN_PIGEON = 0;
    public static final int DRIVE_CAN_RIGHT_LEADER = 3;
    public static final int DRIVE_CAN_RIGHT_FOLLOWER = 4;
    public static final int DRIVE_CAN_LEFT_LEADER = 1;
    public static final int DRIVE_CAN_LEFT_FOLLOWER = 2;

    public static final Pigeon2Configuration DRIVE_PIGEON_CONFIG = new Pigeon2Configuration();
    static {
        DRIVE_PIGEON_CONFIG.EnableCompass = false;
    }

    public static final double DRIVE_GEAR_RATIO = 6.818;
    public static final double DRIVE_WHEEL_DIAMETER_METERS = Units.inchesToMeters(6);

    public static final double DRIVE_POSITION_GAIN = 2.3546;
    public static final double DRIVE_INTEGRAL_GAIN = 0;
    public static final double DRIVE_DERIVATIVE_GAIN = 0;

    public static final double DRIVE_STATIC_GAIN = 0.26981;
    public static final double DRIVE_VELOCITY_GAIN = 0.046502;
    public static final double DRIVE_ACCELERATION_GAIN = 0.0093369;

    public static final double DRIVE_MAX_VELOCITY_METERS_PER_SECOND = 5;
    public static final double DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED = 3;

    public static final double DRIVE_TRACK_WIDTH_METERS = 0.644;

    /**
     *
     * INTAKE
     *
     */
    public static final int INTAKE_CAN_MAIN = 10;

    public static final double INTAKE_MAIN_SPEED = 0.9;
    public static final double INTAKE_GEARING = 18;

    public static final int INTAKE_PNEUMATIC_LEFT_FORWARD = 2;
    public static final int INTAKE_PNEUMATIC_LEFT_REVERSE = 3;

    public static final int INTAKE_PNEUMATIC_RIGHT_FORWARD = 0;
    public static final int INTAKE_PNEUMATIC_RIGHT_REVERSE = 1;

    /**
     *
     * INDEXER
     *
     */
    public static final int INDEXER_CAN_STOMACH_LEFT = 7; // conveyor
    public static final int INDEXER_CAN_STOMACH_RIGHT = 8;
    public static final int INDEXER_CAN_TOWER = 9; // elevator

    public static final double INDEXER_STOMACH_SPEED = 0.6;
    public static final double INDEXER_TOWER_SPEED = 0.9;
    public static final double INDEXER_REJECTION_TIME = 1;

    public static final I2C.Port INDEXER_COLOR = I2C.Port.kMXP;
    public static final int INDEXER_COLOR_PROXIMITY = 200;

    public static final int INDEXER_SENSOR_TOWER_DIO_PORT = 9;
    public static final int INDEXER_SENSOR_SHOOTER_DIO_PORT = 8;

    /**
     *
     * SHOOTER
     *
     */
    public static final int SHOOTER_CAN_LEFT = 11;
    public static final int SHOOTER_CAN_RIGHT = 12;

    public static final double SHOOTER_STATIC_GAIN = 0.15345;
    public static final double SHOOTER_VELOCITY_GAIN = 0.12404;
    public static final double SHOOTER_ACCELERATION_GAIN = 0.0072228;

    public static final int SHOOTER_LOW_SPEED = 1800;
    public static final int SHOOTER_TARMAC_SPEED = 2200;
    public static final int SHOOTER_REJECT_SPEED = 1000;

    public static final InterpolatingTreeMap SHOOTER_SPEED_MAP = new InterpolatingTreeMap();
    static {
        SHOOTER_SPEED_MAP.put(0, 3000);
    }

    /**
     *
     * HOOD
     *
     */
    public static final int HOOD_CAN = 13;
    public static final int HOOD_REV_THROUGH_BORE_DIO_PORT = 0;
    public static final double HOOD_POSITION_MAX = 0;

    public static final double HOOD_550_GEAR_RATIO =  (1.0 / 3.0) * 24.0 / 18.0;
    public static final double HOOD_REV_THROUGH_BORE_GEAR_RATIO = 18.0;
    public static final double HOOD_REV_THROUGH_BORE_OFFSET = 0;

    public static final InterpolatingTreeMap HOOD_POSITION_MAP = new InterpolatingTreeMap();

    static {
        SHOOTER_SPEED_MAP.put(0, 0);
    }

    /**
     *
     * CLIMBER
     *
     */
    public static final int CLIMBER_CAN_LEFT = 5;
    public static final int CLIMBER_CAN_RIGHT = 6;

    public static final int CLIMBER_SENSOR_LEFT_DIO_PORT = 2;
    public static final int CLIMBER_SENSOR_RIGHT_DIO_PORT = 3;

    /**
     *
     * LIMELIGHT / VISION
     * all distances measured in meters
     *
     **/
//    104 inches to top of goal
//    101.625 inches to bottom of vision target
//    middle is 102.8125 inches from field
    public static final double LIMELIGHT_MOUNTING_HEIGHT = Units.inchesToMeters(24);
    public static final double LIMELIGHT_ANGLE = 55;


    /**
     *
     * TRAJECTORY:
     * x represents forward backward
     * y represents right left
     * ALL IN METERS
     *
     */
    private static final TrajectoryConfig TRAJECTORY_CONFIG_FORWARD = new TrajectoryConfig(DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
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

    public static final Trajectory TRAJECTORY_THREE_METER_BACKWARD = TrajectoryGenerator.generateTrajectory(
            List.of(
                    new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
                    new Pose2d(-3, 0, Rotation2d.fromDegrees(0))
            ),
            TRAJECTORY_CONFIG_REVERSED
    );

    public static final Trajectory TRAJECTORY_ONE_METER_BACKWARD = TrajectoryGenerator.generateTrajectory(
            List.of(
                    new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
                    new Pose2d(-1, 0, Rotation2d.fromDegrees(0))
            ),
            TRAJECTORY_CONFIG_REVERSED
    );

    public static final Trajectory TRAJECTORY_PATHPLANNER_LEFT_BALL2_NUMBER1 = PathPlanner.loadPath("LeftBall2N1", DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED);
    public static final Trajectory TRAJECTORY_PATHPLANNER_LEFT_BALL2_NUMBER2 = PathPlanner.loadPath("LeftBall2N2", DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED, true);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_NUMBER1 = PathPlanner.loadPath("BottomBall2N1", DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_NUMBER2 = PathPlanner.loadPath("BottomBall2N2", DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED, true);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL3_NUMBER3 = PathPlanner.loadPath("Ball3N3", DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED);
    public static final Trajectory TRAJECTORY_PATHPLANNER_BALL3_NUMBER4 = PathPlanner.loadPath("Ball3N4", DRIVE_MAX_VELOCITY_METERS_PER_SECOND, DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED);
}