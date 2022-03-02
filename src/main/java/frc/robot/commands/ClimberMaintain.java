package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ClimberMaintain extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberMaintain() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
        climber.enableSoftLimit();
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