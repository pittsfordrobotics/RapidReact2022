package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface IndexerIO {
    /** Contains all the input data received from hardware. */
    class IndexerIOInputs implements LoggableInputs {
        public double leftStomachPositionRad = 0.0;
        public double leftStomachVelocityRadPerSec = 0.0;
        public double leftStomachAppliedVolts = 0.0;
        public double[] leftStomachCurrentAmps = new double[] {};
        public double[] leftStomachTempCelcius = new double[] {};

        public double rightStomachPositionRad = 0.0;
        public double rightStomachVelocityRadPerSec = 0.0;
        public double rightStomachAppliedVolts = 0.0;
        public double[] rightStomachCurrentAmps = new double[] {};
        public double[] rightStomachTempCelcius = new double[] {};

        public double towerPositionRad = 0.0;
        public double towerVelocityRadPerSec = 0.0;
        public double towerAppliedVolts = 0.0;
        public double[] towerCurrentAmps = new double[] {};
        public double[] towerTempCelcius = new double[] {};

        public boolean colorConnected = false;
        public int colorProximity = 0;
        public int colorRed = 0;
        public int colorGreen = 0;
        public int colorBlue = 0;

        public boolean towerDetected = false;
        public boolean shooterDetected = false;

        public void toLog(LogTable table) {
            table.put("LeftStomachPositionRad", leftStomachPositionRad);
            table.put("LeftStomachVelocityRadPerSec", leftStomachVelocityRadPerSec);
            table.put("LeftStomachAppliedVolts", leftStomachAppliedVolts);
            table.put("LeftStomachCurrentAmps", leftStomachCurrentAmps);
            table.put("LeftStomachTempCelcius", leftStomachTempCelcius);

            table.put("RightStomachPositionRad", rightStomachPositionRad);
            table.put("RightStomachVelocityRadPerSec", rightStomachVelocityRadPerSec);
            table.put("RightStomachAppliedVolts", rightStomachAppliedVolts);
            table.put("RightStomachCurrentAmps", rightStomachCurrentAmps);
            table.put("RightStomachTempCelcius", rightStomachTempCelcius);

            table.put("TowerPositionRad", towerPositionRad);
            table.put("TowerVelocityRadPerSec", towerVelocityRadPerSec);
            table.put("TowerAppliedVolts", towerAppliedVolts);
            table.put("TowerCurrentAmps", towerCurrentAmps);
            table.put("TowerTempCelcius", towerTempCelcius);

            table.put("ColorConnected", colorConnected);
            table.put("ColorProximity", colorProximity);
            table.put("ColorRed", colorRed);
            table.put("ColorGreen", colorGreen);
            table.put("ColorBlue", colorBlue);

            table.put("TowerDetected", towerDetected);
            table.put("ShooterDetected", shooterDetected);
        }

        public void fromLog(LogTable table) {
            leftStomachPositionRad = table.getDouble("LeftStomachPositionRad", leftStomachPositionRad);
            leftStomachVelocityRadPerSec = table.getDouble("LeftStomachVelocityRadPerSec", leftStomachVelocityRadPerSec);
            leftStomachAppliedVolts = table.getDouble("LeftStomachAppliedVolts", leftStomachAppliedVolts);
            leftStomachCurrentAmps = table.getDoubleArray("LeftStomachCurrentAmps", leftStomachCurrentAmps);
            leftStomachTempCelcius = table.getDoubleArray("LeftStomachTempCelcius", leftStomachTempCelcius);

            rightStomachPositionRad = table.getDouble("RightStomachPositionRad", rightStomachPositionRad);
            rightStomachVelocityRadPerSec = table.getDouble("RightStomachVelocityRadPerSec", rightStomachVelocityRadPerSec);
            rightStomachAppliedVolts = table.getDouble("RightStomachAppliedVolts", rightStomachAppliedVolts);
            rightStomachCurrentAmps = table.getDoubleArray("RightStomachCurrentAmps", rightStomachCurrentAmps);
            rightStomachTempCelcius = table.getDoubleArray("RightStomachTempCelcius", rightStomachTempCelcius);

            towerPositionRad = table.getDouble("TowerPositionRad", towerPositionRad);
            towerVelocityRadPerSec = table.getDouble("TowerVelocityRadPerSec", towerVelocityRadPerSec);
            towerAppliedVolts = table.getDouble("TowerAppliedVolts", towerAppliedVolts);
            towerCurrentAmps = table.getDoubleArray("TowerCurrentAmps", towerCurrentAmps);
            towerTempCelcius = table.getDoubleArray("TowerTempCelcius", towerTempCelcius);

            colorConnected = table.getBoolean("ColorConnected", colorConnected);
            colorProximity = table.getInteger("ColorProximity", colorProximity);
            colorRed = table.getInteger("ColorRed", colorRed);
            colorGreen = table.getInteger("ColorGreen", colorGreen);
            colorBlue = table.getInteger("ColorBlue", colorBlue);

            towerDetected = table.getBoolean("TowerDetected", towerDetected);
            shooterDetected = table.getBoolean("ShooterDetected", shooterDetected);
        }
    }

    /** Updates the set of loggable inputs. */
    public default void updateInputs(IndexerIOInputs inputs) {}

    /** Run open loop at the percentage of 12V from -1.0 to 1.0. */
    default void setStomachLeft(double percentage) {}

    /** Run open loop at the percentage of 12V from -1.0 to 1.0. */
    default void setStomachRight(double percentage) {}

    /** Run open loop at the percentage of 12V from -1.0 to 1.0. */
    default void setTower(double percentage) {}

    /** Run open loop at the specified voltage. */
    public default void setVoltageStomachLeft(double volts) {}

    /** Run open loop at the specified voltage. */
    public default void setVoltageStomachRight(double volts) {}

    /** Run open loop at the specified voltage. */
    public default void setVoltageTower(double volts) {}
}