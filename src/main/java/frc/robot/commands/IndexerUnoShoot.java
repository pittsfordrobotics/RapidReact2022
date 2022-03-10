package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;


public class IndexerUnoShoot extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private int ballCount;

    public IndexerUnoShoot() {
        addRequirements(this.indexer);
    }

    @Override
    public void initialize() {
        ballCount = indexer.getBallCount();
    }

    @Override
    public void execute() {
        if (indexer.isWrongColorBall()) {
            indexer.setStateShoot();
        }
    }

    @Override
    public boolean isFinished() {
        return !indexer.isWrongColorBall() || ballCount == 0 || indexer.getBallCount() == 0 || indexer.getBallCount() == ballCount - 1;
    }

    @Override
    public void end(boolean interrupted) {
        indexer.setStateStopShoot();
    }
}