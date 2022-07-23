package frc.robot.subsystems.hood;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.hood.HoodIO.HoodIOInputs;
import org.littletonrobotics.junction.Logger;

public class Hood extends SubsystemBase {
    private final HoodIO io;
    private final HoodIOInputs inputs = new HoodIOInputs();

    private boolean climbing = false;

    private final static Hood INSTANCE = new Hood(new HoodIOSparkMax());

    public static Hood getInstance() {
        return INSTANCE;
    }

    private Hood(HoodIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Hood", inputs);
        if (climbing) {
            setPosition(0);
        }
    }

    public void setPosition(int position) {

    }

    public void setClimbing(boolean climbing) {
        this.climbing = climbing;
    }
}