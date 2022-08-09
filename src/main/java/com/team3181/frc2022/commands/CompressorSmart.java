package com.team3181.frc2022.commands;

import com.team3181.frc2022.RobotState;
import com.team3181.frc2022.subsystems.compressor7.Compressor7;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;


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
        if (DriverStation.isAutonomous() || RobotState.getInstance().isClimbing()) {
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