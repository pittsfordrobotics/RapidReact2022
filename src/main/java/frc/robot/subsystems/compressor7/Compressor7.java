package frc.robot.subsystems.compressor7;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.compressor7.CompressorIO.CompressorIOInputs;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import org.littletonrobotics.junction.Logger;

public class Compressor7 extends SubsystemBase {
    private final CompressorIO io;
    private final CompressorIOInputs inputs = new CompressorIOInputs();

    private final Timer pressureTimer = new Timer();

    private final Alert notPressurized = new Alert("Air tanks are not fully pressurized!", AlertType.INFO);
    private final Alert pressureNotIncreasing = new Alert("Air pressure isn't increasing! Check if quick release value is open!", AlertType.WARNING);

    private final static Compressor7 INSTANCE = new Compressor7(Constants.ROBOT_COMPRESSOR_IO);
    public static Compressor7 getInstance() {
        return INSTANCE;
    }

    private Compressor7(CompressorIO io){
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Compressor", inputs);
        notPressurized.set(!(inputs.pressurePsi > 120));
        if (inputs.compressorActive) {
            pressureTimer.reset();
            pressureTimer.start();
        }
        else {
            pressureTimer.stop();
        }
        if (pressureTimer.advanceIfElapsed(5)) {
            pressureNotIncreasing.set(inputs.pressurePsi < 5);
        }
    }

    public void enable() {
        io.enable(true);
    }

    public void disable() {
        io.enable(false);
    }

}