/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.drive.DriveIO.DriveIOInputs;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import org.littletonrobotics.junction.Logger;

public class Drive extends SubsystemBase {
    private final DriveIO io;
    private final DriveIOInputs inputs = new DriveIOInputs();

    private double throttle;
    private double tempThrottle;
    private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));
    private DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds(0, 0);
    private Pose2d pose = new Pose2d(0, 0, Rotation2d.fromDegrees(getAngle()));

    private final Alert pigeonAlert = new Alert("Pigeon not detected! Many functions of the robot will FAIL!", AlertType.ERROR);

    private static final Drive INSTANCE = new Drive(new DriveIOSparkMax());
    public static Drive getInstance() {
        return INSTANCE;
    }

    private Drive(DriveIO io) {
        this.io = io;

        setThrottle(0.7);

        ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
        driveTab.addNumber("Pigeon", () -> Units.radiansToDegrees(inputs.gyroYawPositionRad));
        driveTab.addNumber("Throttle", this::getThrottle);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Drive", inputs);
        pigeonAlert.set(!(inputs.gyroUpTime > 0));

        pose = odometry.update(
                Rotation2d.fromDegrees(getAngle()),
                inputs.leftPositionRad,
                inputs.rightPositionRad);

        wheelSpeeds = new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
    }

    public void driveArcade(double speed, double rotation, boolean squared) {
        WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(speed, rotation, squared);
        io.set(speeds.left * throttle, speeds.right * throttle);
    }

    public void driveCurve(double speed, double rotation) {
        WheelSpeeds speeds = DifferentialDrive.curvatureDriveIK(speed, rotation, Math.abs(speed) < 0.15);
        io.set(speeds.left * throttle, speeds.right * throttle);
    }

    public void driveVolts(double left, double right) {
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
        resetEncoders();
    }

    public void resetEncoders() {
        io.resetEncoders();
    }

    public double getLeftVelocity() {
        return inputs.leftVelocityRadPerSec;
    }

    public double getRightVelocity() {
        return inputs.rightVelocityRadPerSec;
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
    }

    /**
     * Gets the pigeon's angle
     * @return current angle; positive = clockwise
     */
    public double getAngle() {
        return Units.radiansToDegrees(inputs.gyroYawPositionRad);
    }

}