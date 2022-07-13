package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;

public class Compressor7 extends SubsystemBase {
    private final Compressor compressor = new Compressor(PneumaticsModuleType.REVPH);

    private final Timer pressureTimer = new Timer();

    private final Alert notPressurized = new Alert("Air tanks are not fully pressurized!", AlertType.INFO);
    private final Alert pressureNotIncreasing = new Alert("Air pressure isn't increasing! Check if quick release value is open!", AlertType.WARNING);

    private final static Compressor7 INSTANCE = new Compressor7();
    public static Compressor7 getInstance() {
        return INSTANCE;
    }

    private Compressor7(){}

    @Override
    public void periodic() {
        notPressurized.set(!(compressor.getPressure() > 120));
        if (compressor.enabled()) {
            pressureTimer.reset();
            pressureTimer.start();
        }
        else {
            pressureTimer.stop();
        }
        if (pressureTimer.advanceIfElapsed(5)) {
            pressureNotIncreasing.set(compressor.getPressure() < 5);
        }
    }

    public void enable() {
        compressor.enableDigital();
    }

    public void disable() {
        compressor.disable();
    }

}