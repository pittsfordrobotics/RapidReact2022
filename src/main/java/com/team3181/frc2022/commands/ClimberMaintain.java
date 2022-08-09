package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.climber.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ClimberMaintain extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberMaintain() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        climber.front();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}