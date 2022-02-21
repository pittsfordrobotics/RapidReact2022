package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ClimberHasBeenZeroed extends CommandBase {
    private final Climber climber = Climber.getInstance();

    public ClimberHasBeenZeroed() {
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
        return climber.hasBeenZeroed();
    }

    @Override
    public void end(boolean interrupted) {
    }
}