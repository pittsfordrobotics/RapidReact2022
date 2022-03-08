package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;

public class IndexerOverride extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final boolean reverse;

    public IndexerOverride(boolean reverse) {
        addRequirements(this.indexer);
        this.reverse = reverse;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        indexer.setReverse(reverse);
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