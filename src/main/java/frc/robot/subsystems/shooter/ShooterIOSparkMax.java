// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxPIDController.ArbFFUnits;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class ShooterIOSparkMax implements ShooterIO {
    private final LazySparkMax motorLeft = new LazySparkMax(Constants.SHOOTER_CAN_LEFT, IdleMode.kCoast, 60, false);
    private final LazySparkMax motorRight = new LazySparkMax(Constants.SHOOTER_CAN_RIGHT, IdleMode.kCoast, 60, true, motorLeft);
    private final RelativeEncoder encoder = motorLeft.getEncoder();
    private final SparkMaxPIDController pid = motorLeft.getPIDController();

    public ShooterIOSparkMax() {}

    @Override
    public void updateInputs(ShooterIOInputs inputs) {
        inputs.positionRad =
                Units.rotationsToRadians(encoder.getPosition());
        inputs.velocityRadPerSec =
                Units.rotationsPerMinuteToRadiansPerSecond(encoder.getVelocity());
        inputs.appliedVolts =
                motorLeft.getAppliedOutput() * RobotController.getBatteryVoltage();
        inputs.currentAmps = new double[] {motorLeft.getOutputCurrent(),};
        inputs.tempCelcius = new double[] {motorLeft.getMotorTemperature()};
    }

    @Override
    public void set(double percent) {
        setVoltage(percent * 12);
    }

    @Override
    public void setVoltage(double volts) {
        motorLeft.setVoltage(volts);
    }

    @Override
    public void setVelocity(double velocityRadPerSec, double ffVolts) {
        pid.setReference(
                Units.radiansPerSecondToRotationsPerMinute(velocityRadPerSec),
                ControlType.kVelocity, 0, ffVolts, ArbFFUnits.kVoltage);
    }

    @Override
    public void setBrakeMode(boolean enable) {
        motorLeft.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
        motorRight.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
    }

    @Override
    public void configurePID(double kP, double kI, double kD) {
        pid.setP(kP, 0);
        pid.setI(kI, 0);
        pid.setD(kD, 0);
        pid.setFF(0, 0);
    }
}