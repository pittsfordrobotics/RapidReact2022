package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.indexer.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;


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