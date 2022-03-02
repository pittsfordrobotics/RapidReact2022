package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ClimberReverse extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberReverse() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
        climber.enableSoftLimit();
    }

    @Override
    public void execute() {
        climber.reverse();
    }

    @Override
    public boolean isFinished() {
        return climber.reverseAtHardLimit() || climber.reverseAtSoftLimit();
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopAll();
    }
}