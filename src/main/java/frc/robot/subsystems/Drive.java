/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class Drive extends SubsystemBase {
    private final LazySparkMax leftPrimary = new LazySparkMax(Constants.DRIVE_CAN_LEFT_LEADER, IdleMode.kBrake, 60,true);
    private final LazySparkMax leftFollower = new LazySparkMax(Constants.DRIVE_CAN_LEFT_FOLLOWER, IdleMode.kBrake, 60, leftPrimary);
    private final LazySparkMax rightPrimary = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_LEADER, IdleMode.kBrake, 60, false);
    private final LazySparkMax rightFollower = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_FOLLOWER, IdleMode.kBrake, 60, rightPrimary);

    private final RelativeEncoder leftEncoder = leftPrimary.getEncoder();
    private final RelativeEncoder rightEncoder = rightPrimary.getEncoder();

    private final AHRS ahrs = new AHRS(Constants.DRIVE_NAVX);
//    private final AHRS ahrs = new AHRS(SPI.Port.kMXP);
//    private final WPI_Pigeon2 pigeon = new WPI_Pigeon2(Constants.DRIVE_CAN_PIGEON);

    private double throttle = 0.6;
    private final DifferentialDrive differentialDrive = new DifferentialDrive(leftPrimary, rightPrimary);
    private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));
    private DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds(0, 0);
    private Pose2d pose = new Pose2d(0, 0, Rotation2d.fromDegrees(getAngle()));

    private SlewRateLimiter rateLimit = new SlewRateLimiter(1);
    private boolean decelerate = false;
    private double pastInput = 0;

    private static final Drive INSTANCE = new Drive();
    public static Drive getInstance() {
        return INSTANCE;
    }

    private Drive() {
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

        SmartDashboard.putNumber("Throttle", throttle);
        SmartDashboard.putBoolean("Decelerating", decelerate);
    }

    public void driveArcade(double speed, double rotation) {
        differentialDrive.arcadeDrive(speed, rotation, true);
    }

    public void driveCurve(double speed, double rotation) {
        differentialDrive.curvatureDrive(speed, rotation, Math.abs(speed) < 0.15);
    }

    public void driveCurveRateLimited(double speed, double rotation) {
        if (Math.abs(MathUtil.applyDeadband(getAverageVelocity(),0.2)) == 0) {
            decelerate = false;
        }
        else if (!decelerate) {
            decelerate = speed != 0 && (speed > 0 ? speed - pastInput < 0 : speed - pastInput > 0);
        }
        pastInput = (speed + pastInput) / 2;

        double rateLimitedSpeed = getRateLimit().calculate(speed);
        driveCurve(decelerate ? rateLimitedSpeed : speed, -rotation * 0.5);
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

    public double getAverageVelocity() { return (getLeftVelocity() + getRightVelocity()) / 2; }

    public PIDController getLeftController() {
        return new PIDController(Constants.DRIVE_POSITION_GAIN, Constants.DRIVE_INTEGRAL_GAIN, Constants.DRIVE_DERIVATIVE_GAIN);
    }

    public PIDController getRightController() {
        return new PIDController(Constants.DRIVE_POSITION_GAIN, Constants.DRIVE_INTEGRAL_GAIN, Constants.DRIVE_DERIVATIVE_GAIN);
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return wheelSpeeds;
    }

    public void coastMode() {
        leftPrimary.setIdleMode(IdleMode.kCoast);
        leftFollower.setIdleMode(IdleMode.kCoast);
        rightPrimary.setIdleMode(IdleMode.kCoast);
        rightFollower.setIdleMode(IdleMode.kCoast);
    }

    public void breakMode() {
        leftPrimary.setIdleMode(IdleMode.kBrake);
        leftFollower.setIdleMode(IdleMode.kBrake);
        rightPrimary.setIdleMode(IdleMode.kBrake);
        rightFollower.setIdleMode(IdleMode.kBrake);
    }

    public Pose2d getPose() {
        return pose;
    }

    private double getAngle() {
        return -ahrs.getAngle();
//        return pigeon.getAngle();
    }

    public void setCoastMode() {
        rightPrimary.setIdleMode(IdleMode.kCoast);
        rightFollower.setIdleMode(IdleMode.kCoast);
        leftPrimary.setIdleMode(IdleMode.kCoast);
        leftPrimary.setIdleMode(IdleMode.kCoast);
    }

    public void setBrakeMode() {
        rightPrimary.setIdleMode(IdleMode.kBrake);
        rightFollower.setIdleMode(IdleMode.kBrake);
        leftPrimary.setIdleMode(IdleMode.kBrake);
        leftPrimary.setIdleMode(IdleMode.kBrake);
    }

    public void setCoastMode() {
        rightPrimary.setIdleMode(IdleMode.kCoast);
        rightFollower.setIdleMode(IdleMode.kCoast);
        leftPrimary.setIdleMode(IdleMode.kCoast);
        leftPrimary.setIdleMode(IdleMode.kCoast);
    }

    public void setBrakeMode() {
        rightPrimary.setIdleMode(IdleMode.kBrake);
        rightFollower.setIdleMode(IdleMode.kBrake);
        leftPrimary.setIdleMode(IdleMode.kBrake);
        leftPrimary.setIdleMode(IdleMode.kBrake);
    }

    public SlewRateLimiter getRateLimit() {
        return rateLimit;
    }

    public void enableRateLimit() {
        rateLimit = new SlewRateLimiter(1);
    }

    public void disableRateLimit() {
        rateLimit = new SlewRateLimiter(Double.POSITIVE_INFINITY);
    }

    public void setRateLimit(double rateLimit) {
        this.rateLimit = new SlewRateLimiter(rateLimit);
    }

}