package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.indexer.Indexer;


public class IndexerWaitForArmed extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final Timer timer = new Timer();

    public IndexerWaitForArmed() {
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
        return indexer.ableToShoot() || timer.get() > 2;
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }
}