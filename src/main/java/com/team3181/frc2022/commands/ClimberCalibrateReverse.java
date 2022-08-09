package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.climber.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ClimberCalibrateReverse extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberCalibrateReverse() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        climber.calibrateReverse();
    }

    @Override
    public boolean isFinished() {
        return climber.reverseAtHardLimit();
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopAll();
    }
}