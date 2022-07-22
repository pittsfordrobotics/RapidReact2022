package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface ClimberIO {
    /** Contains all of the input data received from hardware. */
    class ClimberIOInputs implements LoggableInputs {
        public boolean limitSwitchLeft = false;
        public boolean limitSwitchRight = false;
        public double positionRad = 0.0;
        public double velocityRadPerSec = 0.0;
        public double appliedVolts = 0.0;
        public double[] currentAmps = new double[] {};
        public double[] tempCelcius = new double[] {};

        public void toLog(LogTable table) {
            table.put("LimitLeft", limitSwitchLeft);
            table.put("LimitRight", limitSwitchRight);
            table.put("PositionRad", positionRad);
            table.put("VelocityRadPerSec", velocityRadPerSec);
            table.put("AppliedVolts", appliedVolts);
            table.put("CurrentAmps", currentAmps);
            table.put("TempCelcius", tempCelcius);
        }

        public void fromLog(LogTable table) {
            limitSwitchLeft = table.getBoolean("LimitActiveLeft", limitSwitchLeft);
            limitSwitchRight = table.getBoolean("LimitActiveRight", limitSwitchRight);
            positionRad = table.getDouble("PositionRad", positionRad);
            velocityRadPerSec =
                    table.getDouble("VelocityRadPerSec", velocityRadPerSec);
            appliedVolts = table.getDouble("AppliedVolts", appliedVolts);
            currentAmps = table.getDoubleArray("CurrentAmps", currentAmps);
            tempCelcius = table.getDoubleArray("TempCelcius", tempCelcius);
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(ClimberIOInputs inputs) {}

    /** Run open loop at the specified voltage. */
    default void setVoltage(double volts) {}

    /** Enable or disable brake mode. */
    default void setBrakeMode(boolean enable) {}

    /** Lock or unlock pistons. */
    default void setUnlocked(boolean unlocked) {}
}