package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;


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