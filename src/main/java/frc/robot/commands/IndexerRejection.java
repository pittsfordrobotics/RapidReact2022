package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.indexer.Indexer;


public class IndexerRejection extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final boolean enabled;

    public IndexerRejection(boolean enabled) {
        this.enabled = enabled;
        addRequirements(this.indexer);
    }

    @Override
    public void initialize() {
        indexer.setRejectionEnabled(enabled);
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