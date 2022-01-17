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
    private CANSparkMax mLeftPrimary = new LazySparkMax(kDriveCANLeftLeader, IdleMode.kBrake);
    private CANSparkMax mLeftFollower = new LazySparkMax(kDriveCANLeftFollower, IdleMode.kBrake, mLeftPrimary);
    private CANSparkMax mRightPrimary = new LazySparkMax(kDriveCANRightLeader, IdleMode.kBrake);
    private CANSparkMax mRightFollower = new LazySparkMax(kDriveCANRightFollower, IdleMode.kBrake, mRightPrimary);

    private RelativeEncoder mLeftEncoder = mLeftPrimary.getEncoder();
    private RelativeEncoder mRightEncoder = mRightPrimary.getEncoder();

    private AHRS mAhrs = new AHRS(Port.kMXP);

    private double throttle;
    private SlewRateLimiter rateLimit = new SlewRateLimiter(2);
    private DifferentialDrive mDifferentialDrive;
    private DifferentialDriveOdometry mOdometry;
    private DifferentialDriveWheelSpeeds mWheelSpeeds;
    private Pose2d mPose;

    private static Drive mDrive = new Drive();
    public static Drive getInstance() {
        return mDrive;
    }

    /**
     * Creates a new DriveTrain.
     */
    private Drive() {
        resetEncoders();

        mDifferentialDrive = new DifferentialDrive(mLeftPrimary, mRightPrimary);
        mDifferentialDrive.setDeadband(0.05);

        mAhrs.reset();

        mOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getAngle()));

        mPose = new Pose2d(0, 0, Rotation2d.fromDegrees(getAngle()));
        mWheelSpeeds = new DifferentialDriveWheelSpeeds(0, 0);

        mLeftEncoder.setPositionConversionFactor(Math.PI * kDriveWheelDiameter / kDriveGearRatio);
        mRightEncoder.setPositionConversionFactor(Math.PI * kDriveWheelDiameter / kDriveGearRatio);
        mLeftEncoder.setVelocityConversionFactor(Math.PI * kDriveWheelDiameter / kDriveGearRatio / 60);
        mRightEncoder.setVelocityConversionFactor(Math.PI * kDriveWheelDiameter / kDriveGearRatio / 60);

        setThrottle(0.6);

        enableRateLimit();
    }

    @Override
    public void periodic() {
        // Distance
        double leftMeters = mLeftEncoder.getPosition();
        double rightMeters = -mRightEncoder.getPosition();

        mPose = mOdometry.update(
                Rotation2d.fromDegrees(getAngle()),
                leftMeters,
                rightMeters);

        mWheelSpeeds = new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
    }

    public void drive(double speed, double rotation) {
        if (speed < 0.1) {
            mDifferentialDrive.curvatureDrive(speed, rotation, true);
        }
        else {
            mDifferentialDrive.curvatureDrive(speed, rotation, false);
        }
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
        mRightPrimary.setVoltage(-right);

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
        return -mRightEncoder.getVelocity();
    }

    public PIDController getLeftController() {
        return new PIDController(kDriveP, kDriveI, kDriveD);
    }

    public PIDController getRightController() {
        return new PIDController(kDriveP, kDriveI, kDriveD);
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