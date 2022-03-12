package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;


public class IndexerFullyArmed extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final Timer timer = new Timer();

    public IndexerFullyArmed() {
        addRequirements(this.indexer);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return indexer.fullyLoaded() || timer.get() > 3;
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }
}