package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ClimberAtFrontLimit extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberAtFrontLimit() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return climber.forwardAtLimit();
    }

    @Override
    public void end(boolean interrupted) {
    }
}