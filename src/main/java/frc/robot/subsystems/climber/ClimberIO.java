package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface ClimberIO {
    /** Contains all of the input data received from hardware. */
    class ClimberIOInputs implements LoggableInputs {
        public boolean enabled = false;
        public boolean leftForwardSwitch = false;
        public boolean leftReverseSwitch = false;
        public boolean rightForwardSwitch = false;
        public boolean rightReverseSwitch = false;
        public double positionRad = 0.0;
        public double velocityRadPerSec = 0.0;
        public double appliedVolts = 0.0;
        public double[] currentAmps = new double[] {};
        public double[] tempCelcius = new double[] {};

        public void toLog(LogTable table) {
            table.put("Enabled", enabled);
            table.put("LeftForwardSwitch",  leftForwardSwitch);
            table.put("LeftReverseSwitch",  leftReverseSwitch);
            table.put("RightForwardSwitch", rightForwardSwitch);
            table.put("RightReverseSwitch", rightReverseSwitch);
            table.put("PositionRad", positionRad);
            table.put("VelocityRadPerSec", velocityRadPerSec);
            table.put("AppliedVolts", appliedVolts);
            table.put("CurrentAmps", currentAmps);
            table.put("TempCelcius", tempCelcius);
        }

        public void fromLog(LogTable table) {
            enabled = table.getBoolean("Enabled", enabled);
            leftForwardSwitch = table.getBoolean("LeftForwardSwitch", leftForwardSwitch);
            leftReverseSwitch = table.getBoolean("LeftReverseSwitch", leftReverseSwitch);
            rightForwardSwitch = table.getBoolean("RightForwardSwitch", rightForwardSwitch);
            rightReverseSwitch = table.getBoolean("RightReverseSwitch", rightReverseSwitch);
            positionRad = table.getDouble("PositionRad", positionRad);
            velocityRadPerSec = table.getDouble("VelocityRadPerSec", velocityRadPerSec);
            appliedVolts = table.getDouble("AppliedVolts", appliedVolts);
            currentAmps = table.getDoubleArray("CurrentAmps", currentAmps);
            tempCelcius = table.getDoubleArray("TempCelcius", tempCelcius);
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(ClimberIOInputs inputs) {}

    /** Run open loop at the percentage of 12V from -1.0 to 1.0. */
    default void set(double percent) {}

    /** Run open loop at the specified voltage. */
    default void setVoltage(double volts) {}

    /** Enable or disable brake mode. */
    default void setBrakeMode(boolean enable) {}

    /** Enables or disables. */
    default void setEnabled(boolean enabled) {}

    /**
     * Resets encoders to 0
     */
    default void resetEncoders() {}

    /** Enables/Disables soft limits */
    default void enableSoftLimit(boolean enabled) {}
}