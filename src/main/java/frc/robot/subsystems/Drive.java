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
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class Drive extends SubsystemBase {
    private final CANSparkMax leftPrimary = new LazySparkMax(Constants.DRIVE_CAN_LEFT_LEADER, IdleMode.kBrake, true);
    private final CANSparkMax leftFollower = new LazySparkMax(Constants.DRIVE_CAN_LEFT_FOLLOWER, IdleMode.kBrake, leftPrimary);
    private final CANSparkMax rightPrimary = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_LEADER, IdleMode.kBrake, false);
    private final CANSparkMax rightFollower = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_FOLLOWER, IdleMode.kBrake, rightPrimary);

    private final RelativeEncoder leftEncoder = leftPrimary.getEncoder();
    private final RelativeEncoder rightEncoder = rightPrimary.getEncoder();

    private final AHRS ahrs = new AHRS(Constants.DRIVE_NAVX);

    private final DifferentialDrive differentialDrive = new DifferentialDrive(leftPrimary, rightPrimary);;
    private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));;
    private DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds(0, 0);;
    private SlewRateLimiter rateLimit = new SlewRateLimiter(2);
    private Pose2d pose = new Pose2d(0, 0, Rotation2d.fromDegrees(getAngle()));;
    private double throttle = 0.6;

    private static final Drive INSTANCE = new Drive();
    public static Drive getInstance() {
        return INSTANCE;
    }

    private Drive() {
        resetEncoders();

        differentialDrive.setDeadband(0.2);

        ahrs.reset();

        leftEncoder.setPositionConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER_METERS / Constants.DRIVE_GEAR_RATIO);
        rightEncoder.setPositionConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER_METERS / Constants.DRIVE_GEAR_RATIO);
        leftEncoder.setVelocityConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER_METERS / Constants.DRIVE_GEAR_RATIO / 60);
        rightEncoder.setVelocityConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER_METERS / Constants.DRIVE_GEAR_RATIO / 60);

        setThrottle(throttle);
    }

    @Override
    public void periodic() {
        double leftMeters = leftEncoder.getPosition();
        double rightMeters = rightEncoder.getPosition();

        pose = odometry.update(
                Rotation2d.fromDegrees(getAngle()),
                leftMeters,
                rightMeters);

        wheelSpeeds = new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
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
        return new PIDController(Constants.DRIVE_P, Constants.DRIVE_I, Constants.DRIVE_D);
    }

    public PIDController getRightController() {
        return new PIDController(Constants.DRIVE_P, Constants.DRIVE_I, Constants.DRIVE_D);
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return wheelSpeeds;
    }

    public Pose2d getPose() {
        return pose;
    }

    private double getAngle() {
        return -ahrs.getAngle();
    }

    public SlewRateLimiter getRateLimit() {
        return rateLimit;
    }

    public void enableRateLimit() {
        rateLimit = new SlewRateLimiter(2);
    }

    public void disableRateLimit() {
        rateLimit = new SlewRateLimiter(Double.POSITIVE_INFINITY);
    }

    public void setRateLimit(double rateLimit) {
        this.rateLimit = new SlewRateLimiter(rateLimit);
    }

}