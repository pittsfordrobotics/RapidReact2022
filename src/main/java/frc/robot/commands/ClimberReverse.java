package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.Climber;


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
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}