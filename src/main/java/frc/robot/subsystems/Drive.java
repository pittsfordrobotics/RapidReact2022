/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;
import frc.robot.util.LazySparkMax.Motor;

import java.sql.Array;

public class Drive extends SubsystemBase {
    private final CANSparkMax leftPrimary = new LazySparkMax(Constants.DRIVE_CAN_LEFT_LEADER, IdleMode.kBrake, Motor.NEO_MAX,true);
    private final CANSparkMax leftFollower = new LazySparkMax(Constants.DRIVE_CAN_LEFT_FOLLOWER, IdleMode.kBrake, Motor.NEO_MAX, leftPrimary);
    private final CANSparkMax rightPrimary = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_LEADER, IdleMode.kBrake, Motor.NEO_MAX, false);
    private final CANSparkMax rightFollower = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_FOLLOWER, IdleMode.kBrake, Motor.NEO_MAX, rightPrimary);

    private final RelativeEncoder leftEncoder = leftPrimary.getEncoder();
    private final RelativeEncoder rightEncoder = rightPrimary.getEncoder();

    private final AHRS ahrs = new AHRS(Port.kMXP);

    private final SlewRateLimiter rateLimit = new SlewRateLimiter(2);
    private final DifferentialDrive differentialDrive;
    private final DifferentialDriveOdometry odometry;
    private DifferentialDriveWheelSpeeds wheelSpeeds;
    private double throttle;
    private Pose2d odometryPose; //dead reckoning pose estimate from odometry
    private DifferentialDrivePoseEstimator poseEstimator; //UKF pose estimation from sensor fusion, uses odometry + IMU + camera

    {
        poseEstimator = new DifferentialDrivePoseEstimator(new Rotation2d(),
                new Pose2d(),
                //Example standard deviations from WPILib; should be adjusted for the reliability of each sensor; all units in metres/radians
                //Vision pose estimate is optional - addVisionMeasurement is not called by update() by default and global measurement stdev can be increased
                new MatBuilder<>(Nat.N5(), Nat.N1()).fill(0.02, 0.02, 0.01, 0.02, 0.02), //State measurement stdev: [x, y, theta, dist_l, dist_r]ᵀ
                new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0.02, 0.02, 0.01), //Local measurement stdev:  [dist_l, dist_r, theta]ᵀ
                new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0.1, 0.1, 0.01)); //Global (vision) measurement stdev: [x, y, theta]ᵀ
    }

    private static final Drive INSTANCE = new Drive();
    public static Drive getInstance() {
        return INSTANCE;
    }

    private Drive() {
        resetEncoders();

        differentialDrive = new DifferentialDrive(leftPrimary, rightPrimary);
        differentialDrive.setDeadband(0.2);

        ahrs.reset();

        odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getAngle()));

        odometryPose = new Pose2d(0, 0, Rotation2d.fromDegrees(getAngle()));
        wheelSpeeds = new DifferentialDriveWheelSpeeds(0, 0);

        leftEncoder.setPositionConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER / Constants.DRIVE_GEAR_RATIO);
        rightEncoder.setPositionConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER / Constants.DRIVE_GEAR_RATIO);
        leftEncoder.setVelocityConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER / Constants.DRIVE_GEAR_RATIO / 60);
        rightEncoder.setVelocityConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER / Constants.DRIVE_GEAR_RATIO / 60);

        setThrottle(0.6);

        enableRateLimit();
    }

    @Override
    public void periodic() {
        double leftMeters = leftEncoder.getPosition();
        double rightMeters = rightEncoder.getPosition();

        odometryPose = odometry.update(
                Rotation2d.fromDegrees(getAngle()),
                leftMeters,
                rightMeters);

        poseEstimator.updateWithTime(Timer.getFPGATimestamp(),
                Rotation2d.fromDegrees(getAngle()),
                new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity()),
                leftMeters,
                rightMeters);

        wheelSpeeds = new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());

        SmartDashboard.putNumberArray("Odometry Pose", new double[]{odometryPose.getX(), odometryPose.getY()});
        SmartDashboard.putNumberArray("UKF Pose", new double[]{poseEstimator.getEstimatedPosition().getX(), poseEstimator.getEstimatedPosition().getY()});
    }

    public void drive(double speed, double rotation) {
        differentialDrive.curvatureDrive(speed, rotation, speed < 0.15);
    }

    public void setThrottle(double throttle) {
        differentialDrive.setMaxOutput(throttle);
        this.throttle = throttle;
    }

    public double getThrottle() {
        return throttle;
    }

    public void driveVolts(double left, double right) {
        leftPrimary.setVoltage(left);
        rightPrimary.setVoltage(right);

        differentialDrive.feed();
    }

    public void resetOdometry(Pose2d pose) {
        poseEstimator.resetPosition(pose, Rotation2d.fromDegrees(getAngle()));
        odometry.resetPosition(pose, Rotation2d.fromDegrees(getAngle()));
        resetEncoders();
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public double getLeftVelocity() {
        return leftEncoder.getVelocity();
    }

    public double getRightVelocity() {
        return rightEncoder.getVelocity();
    }

    public PIDController getLeftController() {
        return new PIDController(Constants.DRIVE_POSITION_GAIN, Constants.DRIVE_INTEGRAL_GAIN, Constants.DRIVE_DERIVATIVE_GAIN);
    }

    public PIDController getRightController() {
        return new PIDController(Constants.DRIVE_POSITION_GAIN, Constants.DRIVE_INTEGRAL_GAIN, Constants.DRIVE_DERIVATIVE_GAIN);
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return wheelSpeeds;
    }

    public Pose2d getOdometryPose() {
        return odometryPose;
    }

    public Pose2d getEstimatedPose() {
        return poseEstimator.getEstimatedPosition();
    }

    private double getAngle() {
        return -ahrs.getAngle(); //NavX angle does not drift: compass + IMU complementary filter
    }

    public SlewRateLimiter getRateLimit() {
        return rateLimit;
    }

    public void enableRateLimit() {
        rateLimit.reset(2);
    }

    public void disableRateLimit() {
        rateLimit.reset(Double.POSITIVE_INFINITY);
    }

    public void setRateLimit(double rateLimit) {
        this.rateLimit.reset(rateLimit);
    }

}