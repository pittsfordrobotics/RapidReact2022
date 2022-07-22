package frc.robot.subsystems.hood;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

//    TODO: make this exist
public interface HoodIO {
    class HoodIOInputs implements LoggableInputs {
        public double angle = 0.0;

        @Override
        public void toLog(LogTable table) {

        }

        @Override
        public void fromLog(LogTable table) {

        }
    }
}