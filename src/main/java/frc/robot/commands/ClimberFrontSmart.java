package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ClimberFrontSmart extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberFrontSmart() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
        climber.enableSoftLimit();
    }

    @Override
    public void execute() {
        if (climber.getEncoder() < 75) {
            climber.setSpeed(1);
        }
        else {
            climber.setSpeed(0.6);
        }
    }

    @Override
    public boolean isFinished() {
        return climber.forwardAtLimit();
    }

    @Override
    public void end(boolean interrupted) {

    }
}