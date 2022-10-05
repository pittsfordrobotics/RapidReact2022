//// Copyright (c) FIRST and other WPILib contributors.
//// Open Source Software; you can modify and/or share it under the terms of
//// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.sensors.Pigeon2Configuration;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.climber.ClimberIO;
import frc.robot.subsystems.climber.ClimberIOSparkMax;
import frc.robot.subsystems.compressor7.CompressorIO;
import frc.robot.subsystems.compressor7.CompressorIORev;
import frc.robot.subsystems.drive.DriveIO;
import frc.robot.subsystems.drive.DriveIOSim;
import frc.robot.subsystems.drive.DriveIOSparkMax;
import frc.robot.subsystems.hood.HoodIO;
import frc.robot.subsystems.hood.HoodIOSim;
import frc.robot.subsystems.hood.HoodIOSparkMax;
import frc.robot.subsystems.indexer.IndexerIO;
import frc.robot.subsystems.indexer.IndexerIOSim;
import frc.robot.subsystems.indexer.IndexerIOSparkMax;
import frc.robot.subsystems.intake.IntakeIO;
import frc.robot.subsystems.intake.IntakeIOSparkMax;
import frc.robot.subsystems.shooter.ShooterIO;
import frc.robot.subsystems.shooter.ShooterIOSim;
import frc.robot.subsystems.shooter.ShooterIOSparkMax;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.vision.VisionIOSim;
import frc.robot.util.InterpolatingTreeMap;

import java.util.HashMap;

public final class Constants {
    /**
     *
     * ROBOT: General Constants
     *
     */
    public static final ClimberIO ROBOT_CLIMBER_IO;
    public static final CompressorIO ROBOT_COMPRESSOR_IO;
    public static final DriveIO ROBOT_DRIVE_IO;
    public static final HoodIO ROBOT_HOOD_IO;
    public static final IndexerIO ROBOT_INDEXER_IO;
    public static final IntakeIO ROBOT_INTAKE_IO;
    public static final ShooterIO ROBOT_SHOOTER_IO;
    public static final VisionIO ROBOT_VISION_IO;

    public static final boolean ROBOT_LOGGING_ENABLED = true;
    public static final String ROBOT_LOGGING_PATH = "/media/sda2/";
    public static final boolean ROBOT_PID_TUNER_ENABLED = false;
    public static final boolean ROBOT_IDLE_SHOOTER_ENABLED = false;
    public static final boolean ROBOT_DEMO_MODE = true;

    public static final int ROBOT_PDP_CAN = 1;
    public static final int ROBOT_PNEUMATIC_HUB_CAN = 1;

    public static final double ROBOT_WEIGHT_KILO = Units.lbsToKilograms(120);

    public static final HashMap<Integer, String> ROBOT_SPARKMAX_HASHMAP = new HashMap<>();
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
        ROBOT_SPARKMAX_HASHMAP.put(13, "Hood Left");
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

    public static final TrapezoidProfile.Constraints DRIVE_TURNING_CONSTRAINTS = new Constraints(10, 5);

    public static final double DRIVE_TRACK_WIDTH_METERS = 0.644;

    public static final double DRIVE_MOI = 0.8501136363636363; // this was found in DifferentialDrivetrainSim.createKitbotSim() code

    public static final double DRIVE_MAX_VELOCITY_METERS_PER_SECOND = 10;
    public static final double DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED = 5;

    /**
     *
     * INTAKE
     *
     */
    public static final int INTAKE_CAN_MAIN = 10;

    public static final double INTAKE_MAIN_SPEED = 0.7;
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
    public static final double INDEXER_TOWER_SPEED = 0.6;
    public static final double INDEXER_LOADING_WAIT = 0.5;
    public static final double INDEXER_HUMAN_LOADING_WAIT = 1;

    public static final double INDEXER_SHOOTER_REJECTION_TIME = 1;
    public static final double INDEXER_INTAKE_REJECTION_TIME = 1;

    public static final I2C.Port INDEXER_COLOR = I2C.Port.kMXP;
    public static final int INDEXER_COLOR_PROXIMITY = 300;

    public static final int INDEXER_SENSOR_TOWER_DIO_PORT = 9;
    public static final int INDEXER_SENSOR_SHOOTER_DIO_PORT = 8;

    /**
     *
     * SHOOTER
     *
     */
    public static final int SHOOTER_CAN_LEFT = 11;
    public static final int SHOOTER_CAN_RIGHT = 12;

    public static final double SHOOTER_SHOT_CALM_DELAY = 0.5;

    public static final int SHOOTER_LOW_SPEED = 1500;
    public static final int SHOOTER_AUTO_REJECT_SPEED = 2200;
    public static final int SHOOTER_INDEXER_REJECT_SPEED = 1000;
    public static final double SHOOTER_FENDER_SPEED = 3000;
    public static final double SHOOTER_P = 0.00001;
    public static final double SHOOTER_FEEDFORWARD = 0.00209;

    public static final InterpolatingTreeMap SHOOTER_SPEED_MAP = new InterpolatingTreeMap();
    static {
        SHOOTER_SPEED_MAP.put(0, 3000);
    }

    /**
     *
     * HOOD
     *
     */
    public static final int HOOD_LEFT_CAN = 13;
//    public static final int HOOD_RIGHT_CAN = 12;
    public static final int HOOD_REV_THROUGH_BORE_DIO_PORT = 0;
    public static final double HOOD_ANGLE_OFFSET_RAD = 110.7;
    public static final double HOOD_ANGLE_MIN_RAD = 0;
    public static final double HOOD_ANGLE_MAX_RAD = 75.7; // 36.6 after offset calibration

    public static final double HOOD_550_GEAR_RATIO =  (1.0 / 3.0) * 24.0 / 18.0;
    public static final double HOOD_REV_THROUGH_BORE_GEAR_RATIO = 18.0;

    public static final double HOOD_FENDER_ANGLE = 0;
    public static final double HOOD_AUTO_REJECT_ANGLE = 0;

    public static final InterpolatingTreeMap HOOD_ANGLE_MAP = new InterpolatingTreeMap();
    static {
        HOOD_ANGLE_MAP.put(0, 0);
    }

    /**
     *
     * CLIMBER
     *
     */
    public static final int CLIMBER_CAN_LEFT = 5;
    public static final int CLIMBER_CAN_RIGHT = 6;

    /**
     *
     * LIMELIGHT / VISION
     * all distances measured in meters
     *
     **/
    public static final int LIMELIGHT_WIDTH_PIXELS = 960;
    public static final int LIMELIGHT_HEIGHT_PIXELS = 720;
    public static final double LIMELIGHT_TARGET_FRAMERATE = 22.0;
    public static final double LIMELIGHT_CROSSHAIR_X = 14.4;
    public static final double LIMELIGHT_CROSSHAIR_Y = 0.0;
    public static final Rotation2d LIMELIGHT_FOV_HOR = Rotation2d.fromDegrees(59.6);
    public static final Rotation2d LIMELIGHT_FOV_VER = Rotation2d.fromDegrees(49.7);
    public static final double LIMELIGHT_VEHICLE_TO_CAMERA_Y = 0.0;
    public static final double LIMELIGHT_VEHICLE_TO_CAMERA_X = Units.inchesToMeters(6.1858655);
    public static final double LIMELIGHT_VEHICLE_TO_CAMERA_Z = Units.inchesToMeters(28.3559475);
//    if this no work use 35
    public static final Rotation2d LIMELIGHT_CAMERA_ANGLE = Rotation2d.fromDegrees(55); // Measured relative to the flat part of the hood
    public static final Rotation2d LIMELIGHT_CAMERA_VERT_ROT_FUDGE = Rotation2d.fromDegrees(-0.4); // Measured relative to the flat part of the hood
    public static final CameraPosition LIMELIGHT_CAMERA_POSITION = new CameraPosition(Constants.LIMELIGHT_VEHICLE_TO_CAMERA_Z, Constants.LIMELIGHT_CAMERA_ANGLE, new Transform2d(new Translation2d(LIMELIGHT_VEHICLE_TO_CAMERA_X, LIMELIGHT_VEHICLE_TO_CAMERA_Y), Rotation2d.fromDegrees(0)));

    public static final class CameraPosition {
        public final double cameraHeight;
        public final Rotation2d verticalRotation;
        public final Transform2d vehicleToCamera;

        public CameraPosition(double cameraHeight, Rotation2d verticalRotation, Transform2d vehicleToCamera) {
            this.cameraHeight = cameraHeight;
            this.verticalRotation = verticalRotation;
            this.vehicleToCamera = vehicleToCamera;
        }
    }

    static {
        if (RobotBase.isReal() && ROBOT_DEMO_MODE) {
            ROBOT_CLIMBER_IO = new ClimberIOSparkMax();
            ROBOT_COMPRESSOR_IO = new CompressorIORev();
            ROBOT_DRIVE_IO = new DriveIOSparkMax();
            ROBOT_HOOD_IO = new HoodIO() {};
            ROBOT_INDEXER_IO = new IndexerIOSparkMax();
            ROBOT_INTAKE_IO = new IntakeIOSparkMax();
            ROBOT_SHOOTER_IO = new ShooterIOSparkMax();
            ROBOT_VISION_IO = new VisionIO() {};
        }
        else if (RobotBase.isReal()) {
            ROBOT_CLIMBER_IO = new ClimberIOSparkMax();
            ROBOT_COMPRESSOR_IO = new CompressorIORev();
            ROBOT_DRIVE_IO = new DriveIOSparkMax();
            ROBOT_HOOD_IO = new HoodIOSparkMax();
            ROBOT_INDEXER_IO = new IndexerIOSparkMax();
            ROBOT_INTAKE_IO = new IntakeIOSparkMax();
            ROBOT_SHOOTER_IO = new ShooterIOSparkMax();
            ROBOT_VISION_IO = new VisionIOLimelight();
        }
        else {
            ROBOT_CLIMBER_IO = new ClimberIO() {};
            ROBOT_COMPRESSOR_IO = new CompressorIO(){};
            ROBOT_DRIVE_IO = new DriveIOSim();
            ROBOT_HOOD_IO = new HoodIOSim();
            ROBOT_INDEXER_IO = new IndexerIOSim();
            ROBOT_INTAKE_IO = new IntakeIO(){};
            ROBOT_SHOOTER_IO = new ShooterIOSim();
            ROBOT_VISION_IO = new VisionIOSim();
        }
    }
}