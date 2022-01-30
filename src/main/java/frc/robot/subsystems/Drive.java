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
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LazySparkMax;

import static frc.robot.Constants.*;

public class Drive extends SubsystemBase {
    private final CANSparkMax mLeftPrimary = new LazySparkMax(DriveCANLeftLeader, IdleMode.kBrake, true);
    private final CANSparkMax mLeftFollower = new LazySparkMax(DriveCANLeftFollower, IdleMode.kBrake, mLeftPrimary);
    private final CANSparkMax mRightPrimary = new LazySparkMax(DriveCANRightLeader, IdleMode.kBrake, false);
    private final CANSparkMax mRightFollower = new LazySparkMax(DriveCANRightFollower, IdleMode.kBrake, mRightPrimary);

    private final RelativeEncoder mLeftEncoder = mLeftPrimary.getEncoder();
    private final RelativeEncoder mRightEncoder = mRightPrimary.getEncoder();

    private final AHRS mAhrs = new AHRS(Port.kMXP);

    private final SlewRateLimiter rateLimit = new SlewRateLimiter(2);
    private final DifferentialDrive mDifferentialDrive;
    private final DifferentialDriveOdometry mOdometry;
    private DifferentialDriveWheelSpeeds mWheelSpeeds;
    private Pose2d mPose;
    private double throttle;

    private static final Drive mDrive = new Drive();
    public static Drive getInstance() {
        return mDrive;
    }

    /**
     * Creates a new DriveTrain.
     */
    private Drive() {
        resetEncoders();

        mDifferentialDrive = new DifferentialDrive(mLeftPrimary, mRightPrimary);
        mDifferentialDrive.setDeadband(0.2);

        mAhrs.reset();

        mOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getAngle()));

        mPose = new Pose2d(0, 0, Rotation2d.fromDegrees(getAngle()));
        mWheelSpeeds = new DifferentialDriveWheelSpeeds(0, 0);

        mLeftEncoder.setPositionConversionFactor(Math.PI * DriveWheelDiameter / DriveGearRatio);
        mRightEncoder.setPositionConversionFactor(Math.PI * DriveWheelDiameter / DriveGearRatio);
        mLeftEncoder.setVelocityConversionFactor(Math.PI * DriveWheelDiameter / DriveGearRatio / 60);
        mRightEncoder.setVelocityConversionFactor(Math.PI * DriveWheelDiameter / DriveGearRatio / 60);

        setThrottle(0.6);

        enableRateLimit();
    }

    @Override
    public void periodic() {
        // Distance
        double leftMeters = mLeftEncoder.getPosition();
        double rightMeters = mRightEncoder.getPosition();

        mPose = mOdometry.update(
                Rotation2d.fromDegrees(getAngle()),
                leftMeters,
                rightMeters);

        mWheelSpeeds = new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
    }

    public void drive(double speed, double rotation) {
        mDifferentialDrive.curvatureDrive(speed, rotation, speed < 0.15);
    }

    public void setThrottle(double throttle) {
        mDifferentialDrive.setMaxOutput(throttle);
        this.throttle = throttle;
    }

    public double getThrottle() {
        return throttle;
    }

    public void driveVolts(double left, double right) {
        mLeftPrimary.setVoltage(left);
        mRightPrimary.setVoltage(right);

        mDifferentialDrive.feed();
    }

    public void resetOdometry(Pose2d pose) {
        mOdometry.resetPosition(pose, Rotation2d.fromDegrees(getAngle()));
        resetEncoders();
    }

    public void resetEncoders() {
        mLeftEncoder.setPosition(0);
        mRightEncoder.setPosition(0);
    }

    public double getLeftVelocity() {
        return mLeftEncoder.getVelocity();
    }

    public double getRightVelocity() {
        return mRightEncoder.getVelocity();
    }

    public PIDController getLeftController() {
        return new PIDController(DriveP, DriveI, DriveD);
    }

    public PIDController getRightController() {
        return new PIDController(DriveP, DriveI, DriveD);
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return mWheelSpeeds;
    }

    public Pose2d getPose() {
        return mPose;
    }

    private double getAngle() {
        return -mAhrs.getAngle();
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