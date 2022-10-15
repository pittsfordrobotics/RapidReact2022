package frc.robot.subsystems.compressor7;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface CompressorIO {
    /** Contains all the input data received from hardware. */
    class CompressorIOInputs implements LoggableInputs {
        public boolean compressorActive = false;
        public double compressorCurrentAmps = 0.0;
        public boolean lowPressure = false;

        public void toLog(LogTable table) {
            table.put("LowPressure", lowPressure);
            table.put("CompressorActive", compressorActive);
            table.put("CompressorCurrentAmps", compressorCurrentAmps);
        }

        public void fromLog(LogTable table) {
            lowPressure = table.getBoolean("LowPressure", lowPressure);
            compressorActive = table.getBoolean("CompressorActive", compressorActive);
            compressorCurrentAmps = table.getDouble("CompressorCurrentAmps", compressorCurrentAmps);
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(CompressorIOInputs inputs) {}

    /** Enable compressor */
    default void enable(boolean enabled) {}
}