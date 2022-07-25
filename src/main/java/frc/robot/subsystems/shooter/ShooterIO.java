package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface ShooterIO {
    class ShooterIOInputs implements LoggableInputs {
        public double positionRad = 0.0;
        public double velocityRadPerSec = 0.0;
        public double appliedVolts = 0.0;
        public double[] currentAmps = new double[] {};
        public double[] tempCelcius = new double[] {};

        public void toLog(LogTable table) {
            table.put("PositionRad", positionRad);
            table.put("VelocityRadPerSec", velocityRadPerSec);
            table.put("AppliedVolts", appliedVolts);
            table.put("CurrentAmps", currentAmps);
            table.put("TempCelcius", tempCelcius);
        }

        public void fromLog(LogTable table) {
            positionRad = table.getDouble("PositionRad", positionRad);
            velocityRadPerSec = table.getDouble("VelocityRadPerSec", velocityRadPerSec);
            appliedVolts = table.getDouble("AppliedVolts", appliedVolts);
            currentAmps = table.getDoubleArray("CurrentAmps", currentAmps);
            tempCelcius = table.getDoubleArray("TempCelcius", tempCelcius);
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(ShooterIOInputs inputs) {}

    /** Run open loop at the percentage of 12V from -1.0 to 1.0. */
    default void set(double percent) {}

    /** Run open loop at the specified voltage. */
    default void setVoltage(double volts) {}

    /** Run closed loop at the specified velocity. */
    default void setVelocity(double velocityRadPerSec, double ffVolts) {}

    /** Enable or disable brake mode. */
    default void setBrakeMode(boolean enable) {}

    /** Set velocity PID constants. */
    default void configurePID(double kP, double kI, double kD, boolean PIDTuner) {}
}