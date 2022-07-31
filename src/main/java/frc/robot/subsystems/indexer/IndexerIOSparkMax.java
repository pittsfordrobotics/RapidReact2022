package frc.robot.subsystems.indexer;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class IndexerIOSparkMax implements IndexerIO {
    private final LazySparkMax motorLeft = new LazySparkMax(Constants.INDEXER_CAN_STOMACH_LEFT, IdleMode.kBrake, 30, false);
    private final LazySparkMax motorRight = new LazySparkMax(Constants.INDEXER_CAN_STOMACH_RIGHT, IdleMode.kBrake, 30, motorLeft, true);
    private final LazySparkMax motorTower = new LazySparkMax(Constants.INDEXER_CAN_TOWER, IdleMode.kBrake, 30, true);

    private final RelativeEncoder encoderLeft = motorLeft.getEncoder();
    private final RelativeEncoder encoderRight = motorRight.getEncoder();
    private final RelativeEncoder encoderTower = motorTower.getEncoder();

    private final ColorSensorV3 colorSensorIntake = new ColorSensorV3(Constants.INDEXER_COLOR);
    private final DigitalInput sensorTower = new DigitalInput(Constants.INDEXER_SENSOR_TOWER_DIO_PORT);
    private final DigitalInput sensorShooter = new DigitalInput(Constants.INDEXER_SENSOR_SHOOTER_DIO_PORT);

    public IndexerIOSparkMax() {
    }

    @Override
    public void updateInputs(IndexerIOInputs inputs) {
        inputs.leftStomachPositionRad = Units.rotationsToRadians(encoderLeft.getPosition());
        inputs.leftStomachVelocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(encoderLeft.getVelocity());
        inputs.leftStomachAppliedVolts = motorLeft.getAppliedOutput() * RobotController.getBatteryVoltage();
        inputs.leftStomachCurrentAmps = new double[] {motorLeft.getOutputCurrent()};
        inputs.leftStomachTempCelcius = new double[] {motorLeft.getMotorTemperature()};

        inputs.rightStomachPositionRad = Units.rotationsToRadians(encoderRight.getPosition());
        inputs.rightStomachVelocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(encoderRight.getVelocity());
        inputs.rightStomachAppliedVolts = motorRight.getAppliedOutput() * RobotController.getBatteryVoltage();
        inputs.rightStomachCurrentAmps = new double[] {motorRight.getOutputCurrent()};
        inputs.rightStomachTempCelcius = new double[] {motorRight.getMotorTemperature()};

        inputs.towerPositionRad = Units.rotationsToRadians(encoderTower.getPosition());
        inputs.towerVelocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(encoderTower.getVelocity());
        inputs.towerAppliedVolts = motorTower.getAppliedOutput() * RobotController.getBatteryVoltage();
        inputs.towerCurrentAmps = new double[] {motorTower.getOutputCurrent()};
        inputs.towerTempCelcius = new double[] {motorTower.getMotorTemperature()};

        inputs.colorConnected = colorSensorIntake.isConnected();
        inputs.colorProximity = colorSensorIntake.getProximity();
        inputs.colorRed = colorSensorIntake.getRed();
        inputs.colorGreen = colorSensorIntake.getGreen();
        inputs.colorBlue = colorSensorIntake.getBlue();

        inputs.towerDetected = sensorTower.get();
        inputs.shooterDetected = sensorShooter.get();
    }

    @Override
    public void setStomachLeft(double percentage) {
        setVoltageStomachLeft(percentage * 12);
    }

    @Override
    public void setVoltageStomachLeft(double volts) {
        motorLeft.setVoltage(volts);
    }

    @Override
    public void setStomachRight(double percentage) {
        setVoltageStomachRight(percentage * 12);
    }

    @Override
    public void setVoltageStomachRight(double volts) {
        motorRight.setVoltage(volts);
    }

    @Override
    public void setTower(double percentage) {
        setVoltageTower(percentage * 12);
    }

    @Override
    public void setVoltageTower(double volts) {
        motorTower.setVoltage(volts);
    }
}