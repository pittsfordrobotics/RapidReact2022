package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ClimberCalibrateForward extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberCalibrateForward() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        climber.calibrateFront();
    }

    @Override
    public boolean isFinished() {
        return climber.forwardAtHardLimit();
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopAll();
    }
}