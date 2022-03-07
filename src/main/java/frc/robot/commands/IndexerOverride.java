package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;

public class IndexerOverride extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();

    public IndexerOverride() {
        addRequirements(this.indexer);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        indexer.override();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        indexer.resetEverything();
    }
}