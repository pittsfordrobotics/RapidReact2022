package frc.robot.subsystems.climber;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class ClimberIOSparkMax implements ClimberIO {
    private final LazySparkMax leftMotor = new LazySparkMax(Constants.CLIMBER_CAN_LEFT, IdleMode.kBrake, 60, true);
    private final LazySparkMax rightMotor = new LazySparkMax(Constants.CLIMBER_CAN_RIGHT, IdleMode.kBrake, 60, false);

    private final SparkMaxLimitSwitch leftForwardSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch leftReverseSwitch = leftMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch rightForwardSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch rightReverseSwitch = rightMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

    private final RelativeEncoder leftEncoder = leftMotor.getEncoder();
    private final RelativeEncoder rightEncoder = rightMotor.getEncoder();

    private boolean enabled = false;
//    private double gearRatio = 125;

    public ClimberIOSparkMax() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);

//        rightMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 90);
//        rightMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, -90);
//        leftMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 90);
//        leftMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, -90);

//        rightMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
//        rightMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
//        leftMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
//        leftMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);

        leftForwardSwitch.enableLimitSwitch(true);
        leftReverseSwitch.enableLimitSwitch(true);
        rightForwardSwitch.enableLimitSwitch(true);
        rightReverseSwitch.enableLimitSwitch(true);
    }

    @Override
    public void updateInputs(ClimberIOInputs inputs) {
        inputs.enabled = enabled;
        inputs.leftForwardSwitch = leftForwardSwitch.isPressed();
        inputs.leftReverseSwitch = leftReverseSwitch.isPressed();
        inputs.rightForwardSwitch = rightForwardSwitch.isPressed();
        inputs.rightReverseSwitch = rightReverseSwitch.isPressed();
        inputs.positionRad =
                Units.rotationsToRadians(leftEncoder.getPosition());
        inputs.velocityRadPerSec =
                Units.rotationsPerMinuteToRadiansPerSecond(leftEncoder.getVelocity());
        inputs.appliedVolts =
                leftMotor.getAppliedOutput() * RobotController.getBatteryVoltage();
        inputs.currentAmps =
                new double[] {leftMotor.getOutputCurrent(), rightMotor.getOutputCurrent()};
        inputs.tempCelcius = new double[] {leftMotor.getMotorTemperature(),
                rightMotor.getMotorTemperature()};
    }

    @Override
    public void set(double percent) {
        leftMotor.setVoltage(MathUtil.clamp(percent,-1, 1) * 12);
        rightMotor.setVoltage(MathUtil.clamp(percent,-1, 1) * 12);
    }

    @Override
    public void setVoltage(double volts) {
        leftMotor.setVoltage(volts);
        rightMotor.setVoltage(volts);
    }

    @Override
    public void setBrakeMode(boolean enable) {
        leftMotor.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
        rightMotor.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    @Override
    public void enableSoftLimit(boolean enabled) {
        rightMotor.enableSoftLimit(SoftLimitDirection.kForward, enabled);
        rightMotor.enableSoftLimit(SoftLimitDirection.kReverse, enabled);
        leftMotor.enableSoftLimit(SoftLimitDirection.kForward, enabled);
        leftMotor.enableSoftLimit(SoftLimitDirection.kReverse, enabled);
    }
}