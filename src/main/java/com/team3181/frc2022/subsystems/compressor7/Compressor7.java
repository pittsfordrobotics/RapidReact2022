package com.team3181.frc2022.subsystems.compressor7;

import com.team3181.frc2022.Constants;
import com.team3181.lib.io2022.CompressorIO;
import com.team3181.lib.io2022.CompressorIO.CompressorIOInputs;
import com.team3181.lib.util.Alert;
import com.team3181.lib.util.Alert.AlertType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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