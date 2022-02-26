package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ClimberReverseSmart extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberReverseSmart() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (climber.getEncoder() > -75) {
            climber.setSpeed(-1);
        }
        else {
            climber.setSpeed(-0.6);
        }
    }

    @Override
    public boolean isFinished() {
        return climber.reverseAtLimit();
    }

    @Override
    public void end(boolean interrupted) {

    }
}