package frc.robot.subsystems.hood;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class HoodIOSparkMax implements HoodIO {
    private final LazySparkMax motor = new LazySparkMax(Constants.HOOD_LEFT_CAN, IdleMode.kBrake, 30, false);
    private final RelativeEncoder encoder = motor.getEncoder();
    private final DutyCycleEncoder throughBore = new DutyCycleEncoder(Constants.HOOD_REV_THROUGH_BORE_DIO_PORT);
    private double lastPos = 0;
    private double counter = 0;
    private double offset = 0;
    private final DigitalInput input = new DigitalInput(Constants.HOOD_DIO_PORT);
//    private double timer = Timer;

    public HoodIOSparkMax() {
    }

    @Override
    public void updateInputs(HoodIOInputs inputs) {
//        TODO: BUGGGGG Does this work or are we going to die
//        basically encoder sucks so once it gets to 1 it goes to 0
//        so we have to compensate bc it sucks
        if (lastPos < 0.1 && throughBore.getAbsolutePosition() > 0.9) {
            counter--;
        }
        else if (lastPos > 0.9 && throughBore.getAbsolutePosition() < 0.1) {
            counter++;
        }
        inputs.absolutePosition = throughBore.getAbsolutePosition() + counter;
        inputs.absoluteVelocity = (throughBore.getAbsolutePosition() - lastPos) / 0.02;
        lastPos = throughBore.getAbsolutePosition();
        inputs.positionRad = Units.rotationsToRadians(encoder.getPosition()) / Constants.HOOD_550_GEAR_RATIO - offset;
        inputs.velocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(encoder.getVelocity()) / Constants.HOOD_550_GEAR_RATIO;
        inputs.appliedVolts = motor.getAppliedOutput() * RobotController.getBatteryVoltage();
        inputs.limit = input.get();
        inputs.currentAmps = new double[] {motor.getOutputCurrent()};
        inputs.tempCelcius = new double[] {motor.getMotorTemperature()};

        motor.checkAlive();
    }

    @Override
    public void resetCounter() {
        counter = 0;
    }

    public void resetEncoder() {
        offset = encoder.getPosition();
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