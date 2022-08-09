package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.climber.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ClimberCalibrateCenter extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberCalibrateCenter() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
        climber.saveHalfway();
    }

    @Override
    public void execute() {
        climber.calibrateFront();
    }

    @Override
    public boolean isFinished() {
        return climber.hasBeenCentered();
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopAll();
    }
}