package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;


public class IndexerLocate extends CommandBase {
    private final frc.robot.subsystems.Indexer indexer = frc.robot.subsystems.Indexer.getInstance();

    public IndexerLocate() {
        // each subsystem used by the command must be passed into the
        // addRequirements() method (which takes a vararg of Subsystem)
        addRequirements(this.indexer);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (indexer.ballAtIntake()) {
            indexer.intakeBall();
        }
        if (indexer.ballAtTower()) {
            indexer.advanceToTower();
        }
        if (indexer.ballAtShooter()) {
            indexer.advanceToShooter();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}