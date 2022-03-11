/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.sensors.WPI_Pigeon2;
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
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

import java.util.LinkedList;

public class Drive extends SubsystemBase {
    private final LazySparkMax leftPrimary = new LazySparkMax(Constants.DRIVE_CAN_LEFT_LEADER, IdleMode.kBrake, 60,false);
    private final LazySparkMax leftFollower = new LazySparkMax(Constants.DRIVE_CAN_LEFT_FOLLOWER, IdleMode.kBrake, 60, leftPrimary);
    private final LazySparkMax rightPrimary = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_LEADER, IdleMode.kBrake, 60, true);
    private final LazySparkMax rightFollower = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_FOLLOWER, IdleMode.kBrake, 60, rightPrimary);

    private final RelativeEncoder leftEncoder = leftPrimary.getEncoder();
    private final RelativeEncoder rightEncoder = rightPrimary.getEncoder();

    private final WPI_Pigeon2 pigeon = new WPI_Pigeon2(Constants.DRIVE_CAN_PIGEON);

    private double throttle;
    private double tempThrottle;
    private final DifferentialDrive differentialDrive = new DifferentialDrive(leftPrimary, rightPrimary);
    private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));
    private DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds(0, 0);
    private LinkedList<Double> velocities = new LinkedList<>();
    private Pose2d pose = new Pose2d(0, 0, Rotation2d.fromDegrees(getAngle()));

    private final SlewRateLimiter rateLimit = new SlewRateLimiter(Constants.DRIVE_RATE_LIMIT);

    private static final Drive INSTANCE = new Drive();
    public static Drive getInstance() {
        return INSTANCE;
    }

    private Drive() {
        differentialDrive.setDeadband(0.2);

        pigeon.configAllSettings(Constants.DRIVE_PIGEON_CONFIG);
        pigeon.reset();

        leftEncoder.setPositionConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER_METERS / Constants.DRIVE_GEAR_RATIO);
        rightEncoder.setPositionConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER_METERS / Constants.DRIVE_GEAR_RATIO);
        leftEncoder.setVelocityConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER_METERS / Constants.DRIVE_GEAR_RATIO / 60);
        rightEncoder.setVelocityConversionFactor(Math.PI * Constants.DRIVE_WHEEL_DIAMETER_METERS / Constants.DRIVE_GEAR_RATIO / 60);

        setThrottle(0.5);

        ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
        driveTab.addNumber("Pigeon", pigeon::getAngle);
        driveTab.addNumber("Throttle", this::getThrottle);
    }

    @Override
    public void periodic() {
        pose = odometry.update(
                Rotation2d.fromDegrees(getAngle()),
                leftEncoder.getPosition(),
                rightEncoder.getPosition());

        wheelSpeeds = new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
        updateAverageVelocities();
    }

    public void driveArcade(double speed, double rotation, boolean squared) {
        differentialDrive.arcadeDrive(speed, rotation, squared);
    }

    public void driveCurve(double speed, double rotation, boolean rateLimited) {
        if (rateLimited) {
            if (MathUtil.applyDeadband(getOverallVelocity(), 0.2) == 0) rateLimit.reset(0);
            differentialDrive.curvatureDrive(speed, rotation, Math.abs(speed) < 0.15);
        }
        else {
            differentialDrive.curvatureDrive(speed, rotation, Math.abs(speed) < 0.15);
        }
    }

    public void driveVolts(double left, double right) {
        leftPrimary.setVoltage(left);
        rightPrimary.setVoltage(right);

        differentialDrive.feed();
    }

    public void setThrottle(double throttle) {
        differentialDrive.setMaxOutput(throttle);
        this.throttle = throttle;
    }

    public void setTempThrottle(double throttle) {
        tempThrottle = this.throttle;
        setThrottle(throttle);
    }

    public void setThrottleWithTemp() {
        setThrottle(tempThrottle);
    }

    public double getThrottle() {
        return throttle;
    }

    public void resetOdometry(Pose2d pose) {
        odometry.resetPosition(pose, Rotation2d.fromDegrees(getAngle()));
        resetEncoders();
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public void resetGyro() {
        pigeon.reset();
    }

    public double getLeftVelocity() {
        return leftEncoder.getVelocity();
    }

    public double getRightVelocity() {
        return rightEncoder.getVelocity();
    }

    public double getOverallVelocity() { return (getLeftVelocity() + getRightVelocity()) / 2; }

    /**
     * @return averaged velocity over past periodic cycles
     */
    public double getAverageVelocity() {
        double average = 0;
        for(double i : velocities){
            average += i;
        }
        return (average / velocities.size());
    }

    public void updateAverageVelocities() {
        velocities.addFirst(getOverallVelocity());
        if(velocities.size() > Constants.DRIVE_VELOCITY_LOG_SIZE){
            velocities.pop();
        }
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

    public void coastMode() {
        leftPrimary.setIdleMode(IdleMode.kCoast);
        leftFollower.setIdleMode(IdleMode.kCoast);
        rightPrimary.setIdleMode(IdleMode.kCoast);
        rightFollower.setIdleMode(IdleMode.kCoast);
    }

    public void brakeMode() {
        leftPrimary.setIdleMode(IdleMode.kBrake);
        leftFollower.setIdleMode(IdleMode.kBrake);
        rightPrimary.setIdleMode(IdleMode.kBrake);
        rightFollower.setIdleMode(IdleMode.kBrake);
    }

    public Pose2d getPose() {
        return pose;
    }

    /**
     * Gets the pigeon's angle
     * @return current angle; positive = clockwise from bird's eye view
     */
    public double getAngle() {
        return -pigeon.getAngle();
    }

    public SlewRateLimiter getRateLimit() {
        return rateLimit;
    }

}