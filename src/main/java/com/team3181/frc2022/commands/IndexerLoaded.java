package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.indexer.Indexer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class IndexerLoaded extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final Timer timer = new Timer();
    private final int num;
    private final double waitSec;

    /** Check if indexed num amount of balls */
    public IndexerLoaded(int num, double waitSec) {
        this.num = num;
        this.waitSec = waitSec;
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
        return indexer.getBallCount() == num || timer.advanceIfElapsed(waitSec);
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }
}