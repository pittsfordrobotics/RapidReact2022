package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Compressor7;


public class CompressorSmart extends CommandBase {
    private final Compressor7 compressor = Compressor7.getInstance();

    public CompressorSmart() {
        addRequirements(this.compressor);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        if (DriverStation.isAutonomous()) {
            compressor.disable();
        }
        else {
            compressor.enable();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}