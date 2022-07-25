package frc.robot.subsystems.compressor7;

import edu.wpi.first.wpilibj.PneumaticHub;
import frc.robot.Constants;

public class CompressorIORev implements CompressorIO {
    private final PneumaticHub pneumatics = new PneumaticHub(Constants.ROBOT_PNEUMATIC_HUB_CAN);

        public CompressorIORev() {}

        @Override
        public void updateInputs(CompressorIOInputs inputs) {
            inputs.pressurePsi = pneumatics.getPressure(0);
            inputs.compressorActive = pneumatics.getCompressor();
            inputs.compressorCurrentAmps = pneumatics.getCompressorCurrent();
        }

    @Override
    public void enable(boolean enabled) {
        if (enabled) {
            pneumatics.enableCompressorDigital();
        }
        else {
            pneumatics.disableCompressor();
        }
    }
}