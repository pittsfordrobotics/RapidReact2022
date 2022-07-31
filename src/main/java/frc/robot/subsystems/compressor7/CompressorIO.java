package frc.robot.subsystems.compressor7;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface CompressorIO {
    /** Contains all of the input data received from hardware. */
    class CompressorIOInputs implements LoggableInputs {
        public double pressurePsi = 0.0;
        public boolean compressorActive = false;
        public double compressorCurrentAmps = 0.0;

        public void toLog(LogTable table) {
            table.put("PressurePsi", pressurePsi);
            table.put("CompressorActive", compressorActive);
            table.put("CompressorCurrentAmps", compressorCurrentAmps);
        }

        public void fromLog(LogTable table) {
            pressurePsi = table.getDouble("PressurePsi", pressurePsi);
            compressorActive = table.getBoolean("CompressorActive", compressorActive);
            compressorCurrentAmps =
                    table.getDouble("CompressorCurrentAmps", compressorCurrentAmps);
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(CompressorIOInputs inputs) {}

    /** Enable compressor */
    default void enable(boolean enabled) {}
}