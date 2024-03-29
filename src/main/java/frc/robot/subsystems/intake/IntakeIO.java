package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

/** Intake subsystem hardware interface. */
public interface IntakeIO {
    /** Contains all the input data received from hardware. */
    class IntakeIOInputs implements LoggableInputs {
        public boolean extended = false;

        public double positionRad = 0.0;
        public double velocityRadPerSec = 0.0;
        public double appliedVolts = 0.0;
        public double[] currentAmps = new double[] {};
        public double[] tempCelcius = new double[] {};

        public void toLog(LogTable table) {
            table.put("Extended", extended);

            table.put("PositionRad", positionRad);
            table.put("VelocityRadPerSec", velocityRadPerSec);
            table.put("AppliedVolts", appliedVolts);
            table.put("CurrentAmps", currentAmps);
            table.put("TempCelcius", tempCelcius);
        }

        public void fromLog(LogTable table) {
            extended = table.getBoolean("Extended", extended);

            positionRad = table.getDouble("PositionRad", positionRad);
            velocityRadPerSec = table.getDouble("VelocityRadPerSec", velocityRadPerSec);
            appliedVolts = table.getDouble("AppliedVolts", appliedVolts);
            currentAmps = table.getDoubleArray("CurrentAmps", currentAmps);
            tempCelcius = table.getDoubleArray("TempCelcius", tempCelcius);
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(IntakeIOInputs inputs) {}

    /** Run open loop at the percentage of 12V from -1.0 to 1.0. */
    default void set(double percent) {}

    /** Run the roller open loop at the specified voltage. */
    default void setVoltage(double volts) {}

    /** Enable or disable brake mode on the roller. */
    default void setBrakeMode(boolean enable) {}

    /** Set solenoid state. */
    default void setExtended(boolean extended) {}
}