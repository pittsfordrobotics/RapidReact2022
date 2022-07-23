package frc.robot.subsystems.hood;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class HoodIOSparkMax implements HoodIO {
    private final LazySparkMax motor = new LazySparkMax(Constants.HOOD_CAN, IdleMode.kBrake, 30, false);
    private final RelativeEncoder encoder = motor.getEncoder();
    private final DutyCycleEncoder throughBore = new DutyCycleEncoder(Constants.HOOD_REV_THROUGH_BORE_DIO_PORT);

    public HoodIOSparkMax() {
    }

    @Override
    public void updateInputs(HoodIOInputs inputs) {
        inputs.absolutePositionRad = Units.rotationsToRadians(throughBore.getAbsolutePosition()) * Constants.HOOD_REV_THROUGH_BORE_GEAR_RATIO;
        inputs.positionRad = Units.rotationsToRadians(encoder.getPosition()) / Constants.HOOD_550_GEAR_RATIO;
        inputs.velocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(encoder.getVelocity()) / Constants.HOOD_550_GEAR_RATIO;
        inputs.appliedVolts = motor.getAppliedOutput() * RobotController.getBatteryVoltage();
        inputs.currentAmps = new double[] {motor.getOutputCurrent()};
        inputs.tempCelcius = new double[] {motor.getMotorTemperature()};
    }

    @Override
    public void set(double percentage) {
        motor.setVoltage(MathUtil.clamp(percentage,-1, 1) * 12);
    }

    @Override
    public void setVoltage(double volts) {
        motor.setVoltage(volts);
    }

    @Override
    public void setBrakeMode(boolean enable) {
        motor.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
    }
}