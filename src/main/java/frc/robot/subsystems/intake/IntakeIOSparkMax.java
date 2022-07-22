// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class IntakeIOSparkMax implements IntakeIO {
    private final DoubleSolenoid solenoidLeft = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.INTAKE_PNEUMATIC_LEFT_FORWARD, Constants.INTAKE_PNEUMATIC_LEFT_REVERSE);
    private final DoubleSolenoid solenoidRight = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.INTAKE_PNEUMATIC_RIGHT_FORWARD, Constants.INTAKE_PNEUMATIC_RIGHT_REVERSE);

    private final LazySparkMax motor = new LazySparkMax(Constants.INTAKE_CAN_MAIN, IdleMode.kBrake, 30);
    private final RelativeEncoder encoder = motor.getEncoder();

    public IntakeIOSparkMax() {}

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.extended = solenoidRight.get() == Value.kForward;

        inputs.positionRad = Units.rotationsToRadians(encoder.getPosition()) / Constants.INTAKE_GEARING;
        inputs.velocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(encoder.getVelocity()) / Constants.INTAKE_GEARING;
        inputs.appliedVolts = motor.getAppliedOutput() * RobotController.getBatteryVoltage();
        inputs.currentAmps = new double[] {motor.getOutputCurrent()};
        inputs.tempCelcius = new double[] {motor.getMotorTemperature()};
    }

    @Override
    public void set(double percent) {
        setVoltage(percent * 12);
    }

    @Override
    public void setVoltage(double volts) {
        motor.setVoltage(volts);
    }

    @Override
    public void setBrakeMode(boolean enable) {
        motor.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
    }

    @Override
    public void setExtended(boolean extended) {
        solenoidLeft.set(extended ? Value.kForward : Value.kReverse);
        solenoidRight.set(extended ? Value.kForward : Value.kReverse);
    }
}