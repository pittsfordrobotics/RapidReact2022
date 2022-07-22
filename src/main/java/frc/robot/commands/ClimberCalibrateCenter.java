package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.Climber;


public class ClimberCalibrateCenter extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberCalibrateCenter() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
        climber.saveHalfway();
    }

    @Override
    public void execute() {
        climber.calibrateFront();
    }

    @Override
    public boolean isFinished() {
        return climber.hasBeenCentered();
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopAll();
    }
}