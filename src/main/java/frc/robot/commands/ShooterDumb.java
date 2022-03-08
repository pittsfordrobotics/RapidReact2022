package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;


public class ShooterDumb extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Indexer indexer = Indexer.getInstance();

    public ShooterDumb() {
        addRequirements(this.shooter);
    }

    @Override
    public void initialize() {
        indexer.setStateShoot();
    }

    @Override
    public void execute() {
        shooter.setDumbSpeed();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}