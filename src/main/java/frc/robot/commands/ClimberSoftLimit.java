package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.Climber;


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