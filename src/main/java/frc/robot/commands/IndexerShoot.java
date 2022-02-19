package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;


public class IndexerShoot extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();

    public IndexerShoot() {
        addRequirements(this.indexer);
    }

    @Override
    public void initialize() {
        indexer.setStateShoot();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return indexer.isEmpty();
    }

    @Override
    public void end(boolean interrupted) {
    }
}