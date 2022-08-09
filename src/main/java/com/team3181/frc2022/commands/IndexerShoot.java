package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.indexer.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class IndexerShoot extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();

    public IndexerShoot() {
        addRequirements(this.indexer);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        indexer.setStateShoot();
    }

    @Override
    public boolean isFinished() {
        return indexer.isEmpty();
    }

    @Override
    public void end(boolean interrupted) {
        indexer.setStateStopShoot();
    }
}