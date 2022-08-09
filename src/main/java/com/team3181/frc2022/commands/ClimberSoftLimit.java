package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.climber.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ClimberSoftLimit extends CommandBase {
    private final Climber climber = Climber.getInstance();
    private final boolean state;

    public ClimberSoftLimit(boolean state) {
        addRequirements(this.climber);
        this.state = state;
    }

    @Override
    public void initialize() {
        climber.setSoftLimit(state);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}