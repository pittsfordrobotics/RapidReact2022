package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class IndexerIOSim implements IndexerIO {
    private final DigitalInput sensorTower = new DigitalInput(Constants.INDEXER_SENSOR_TOWER_DIO_PORT);
    private final DigitalInput sensorShooter = new DigitalInput(Constants.INDEXER_SENSOR_SHOOTER_DIO_PORT);

    public IndexerIOSim() {
        SmartDashboard.putNumber("Indexer/ColorSensorProx", 0);
        SmartDashboard.putNumber("Indexer/ColorSensorRed", 0);
        SmartDashboard.putNumber("Indexer/ColorSensorGreen", 0);
        SmartDashboard.putNumber("Indexer/ColorSensorBlue", 0);
    }

    @Override
    public void updateInputs(IndexerIOInputs inputs) {
        inputs.leftStomachPositionRad = 0;
        inputs.leftStomachVelocityRadPerSec = 0;
        inputs.leftStomachAppliedVolts = 0;
        inputs.leftStomachCurrentAmps = new double[] {0};
        inputs.leftStomachTempCelcius = new double[] {0};

        inputs.rightStomachPositionRad = 0;
        inputs.rightStomachVelocityRadPerSec = 0;
        inputs.rightStomachAppliedVolts = 0;
        inputs.rightStomachCurrentAmps = new double[] {0};
        inputs.rightStomachTempCelcius = new double[] {0};

        inputs.towerPositionRad = 0;
        inputs.towerVelocityRadPerSec = 0;
        inputs.towerAppliedVolts = 0;
        inputs.towerCurrentAmps = new double[] {0};
        inputs.towerTempCelcius = new double[] {0};

        inputs.colorConnected = true;
        inputs.colorProximity = (int) SmartDashboard.getNumber("Indexer/ColorSensorProx", 0);
        inputs.colorRed = (int) SmartDashboard.getNumber("Indexer/ColorSensorRed", 0);
        inputs.colorGreen = (int) SmartDashboard.getNumber("Indexer/ColorSensorGreen", 0);
        inputs.colorBlue = (int) SmartDashboard.getNumber("Indexer/ColorSensorBlue", 0);

        inputs.towerDetected = sensorTower.get();
        inputs.shooterDetected = sensorShooter.get();
    }

    @Override
    public void setStomachLeft(double percentage) {

    }

    @Override
    public void setVoltageStomachLeft(double volts) {

    }

    @Override
    public void setStomachRight(double percentage) {

    }

    @Override
    public void setVoltageStomachRight(double volts) {

    }

    @Override
    public void setTower(double percentage) {

    }

    @Override
    public void setVoltageTower(double volts) {

    }
}