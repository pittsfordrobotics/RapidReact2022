package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.indexer.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class IndexerWaitForArmed extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();

    public IndexerWaitForArmed() {
        addRequirements(this.indexer);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return indexer.ableToShoot();
    }

    @Override
    public void end(boolean interrupted) {

    }
}