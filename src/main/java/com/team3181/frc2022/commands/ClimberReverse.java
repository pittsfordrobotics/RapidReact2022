package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.climber.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ClimberReverse extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberReverse() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
//        climber.enableSoftLimit();
    }

    @Override
    public void execute() {
        climber.reverse();
    }

    @Override
    public boolean isFinished() {
        return climber.reverseAtSoftLimit();
    }

    @Override
    public void end(boolean interrupted) {
    }
}