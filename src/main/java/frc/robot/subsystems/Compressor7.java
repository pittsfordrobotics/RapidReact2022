package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CompressorConfigType;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Compressor7 extends SubsystemBase {
    private final Compressor compressor = new Compressor(PneumaticsModuleType.REVPH);

    private final static Compressor7 INSTANCE = new Compressor7();
    public static Compressor7 getInstance() {
        return INSTANCE;
    }

    private Compressor7(){}

    @Override
    public void periodic() {
//        TODO: add alert for not building pressure
    }

    public void enable() {
        compressor.enableDigital();
    }

    public void disable() {
        compressor.disable();
    }

    public boolean isEnabled() {
        return compressor.getConfigType() != CompressorConfigType.Disabled;
    }

}