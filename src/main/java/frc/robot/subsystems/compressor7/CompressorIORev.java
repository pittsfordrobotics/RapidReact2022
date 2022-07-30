package frc.robot.subsystems.compressor7;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class CompressorIORev implements CompressorIO {
//    private final PneumaticHub pneumatics = new PneumaticHub(Constants.ROBOT_PNEUMATIC_HUB_CAN);
    private final Compressor compressor = new Compressor(PneumaticsModuleType.REVPH);

        public CompressorIORev() {}

        @Override
        public void updateInputs(CompressorIOInputs inputs) {
            inputs.pressurePsi = compressor.getPressure();
            inputs.compressorActive = compressor.enabled();
            inputs.compressorCurrentAmps = compressor.getCurrent();
        }

    @Override
    public void enable(boolean enabled) {
        if (enabled) {
            compressor.enableDigital();
        }
        else {
            compressor.disable();
        }
    }
}