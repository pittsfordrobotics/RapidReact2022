package com.team3181.frc2022.subsystems.drive;

import com.team3181.frc2022.Constants;
import com.team3181.frc2022.RobotState;
import com.team3181.lib.io2022.DriveIO;
import com.team3181.lib.io2022.DriveIO.DriveIOInputs;
import com.team3181.lib.util.Alert;
import com.team3181.lib.util.Alert.AlertType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Drive extends SubsystemBase {
    private final DriveIO io;
    private final DriveIOInputs inputs = new DriveIOInputs();

    private double throttle;
    private double tempThrottle;
    private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));
    private DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds(0, 0);
    private Pose2d pose = new Pose2d(0, 0, Rotation2d.fromDegrees(getAngle()));

    private double lastLeftPositionMeters = 0.0;
    private double lastRightPositionMeters = 0.0;
    private boolean lastGyroConnected = false;
    private Rotation2d lastGyroRotation = new Rotation2d();

    private final Alert pigeonAlert = new Alert("Pigeon not detected! Many functions of the robot will FAIL!", AlertType.ERROR);

    private static final Drive INSTANCE = new Drive(Constants.ROBOT_DRIVE_IO);
    public static Drive getInstance() {
        return INSTANCE;
    }

    private Drive(DriveIO io) {
        this.io = io;

        setThrottle(0.7);

        ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
        driveTab.addNumber("Pigeon", this::getAngle);
        driveTab.addNumber("Throttle", this::getThrottle);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Drive", inputs);

        pigeonAlert.set(!inputs.gyroConnected);

        if (DriverStation.isDisabled() && getAverageVelocity() == 0) {
            coastMode();
        }
        else {
            brakeMode();
        }

        Logger.getInstance().recordOutput("Drive/Throttle", throttle);

        pose = odometry.update(
                Rotation2d.fromDegrees(getAngle()),
                inputs.leftPositionMeters,
                inputs.rightPositionMeters);

        Rotation2d currentGyroRotation =
                new Rotation2d(inputs.gyroYawPositionRad * -1);
        double leftPositionMetersDelta =
                inputs.leftPositionMeters - lastLeftPositionMeters;
        double rightPositionMetersDelta =
                inputs.rightPositionMeters - lastRightPositionMeters;
        double avgPositionMetersDelta =
                (leftPositionMetersDelta + rightPositionMetersDelta) / 2.0;
        Rotation2d gyroRotationDelta =
                (inputs.gyroConnected && !lastGyroConnected) ? new Rotation2d()
                        : currentGyroRotation.minus(lastGyroRotation);

        if (inputs.gyroConnected) {
            RobotState.getInstance().addDriveData(Timer.getFPGATimestamp(), new Twist2d(
                    avgPositionMetersDelta, 0.0, gyroRotationDelta.getRadians()));
        } else {
            RobotState.getInstance().addDriveData(Timer.getFPGATimestamp(),
                    new Twist2d(avgPositionMetersDelta, 0.0,
                            (rightPositionMetersDelta - leftPositionMetersDelta)
                                    / Constants.DRIVE_TRACK_WIDTH_METERS));
        }

        lastLeftPositionMeters = inputs.leftPositionMeters;
        lastRightPositionMeters = inputs.rightPositionMeters;
        lastGyroConnected = inputs.gyroConnected;
        lastGyroRotation = currentGyroRotation;


        Logger.getInstance().recordOutput("Drive/Pose",
                new double[] {pose.getX(), pose.getY(), pose.getRotation().getRadians()});

        wheelSpeeds = new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
    }

    public void driveArcade(double speed, double rotation, boolean squared) {
        WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(MathUtil.applyDeadband(speed,0.05), MathUtil.applyDeadband(rotation,0.05), squared);
        io.set(speeds.left * throttle, speeds.right * throttle);
    }

    public void driveCurve(double speed, double rotation) {
        WheelSpeeds speeds = DifferentialDrive.curvatureDriveIK(MathUtil.applyDeadband(speed,0.05), MathUtil.applyDeadband(rotation,0.05), Math.abs(speed) < 0.15);
        io.set(speeds.left * throttle, speeds.right * throttle);
    }

    public void setVolts(double left, double right) {
        io.setVoltage(left, right);
    }

    public void setThrottle(double throttle) {
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
        RobotState.getInstance().resetPose(pose);
        resetEncoders();
    }

    public void resetEncoders() {
        io.resetEncoders();
    }

    public double getLeftVelocity() {
        return inputs.leftVelocityMetersPerSec;
    }

    public double getRightVelocity() {
        return inputs.rightVelocityMetersPerSec;
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
        io.setBrakeMode(false);
    }

    public void brakeMode() {
        io.setBrakeMode(true);
    }

    public Pose2d getPose() {
        return pose;
//        return RobotState.getInstance().getLatestPose();
    }

    /**
     * Gets the pigeon's angle
     * @return current angle; positive = clockwise
     */
    public double getAngle() {
        return -Units.radiansToDegrees(inputs.gyroYawPositionRad);
    }

}