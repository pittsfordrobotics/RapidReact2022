package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.climber.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ClimberForward extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberForward() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
//        climber.enableSoftLimit();
    }

    @Override
    public void execute() {
        climber.front();
    }

    @Override
    public boolean isFinished() {
        return climber.forwardAtSoftLimit();
    }

    @Override
    public void end(boolean interrupted) {
    }
}