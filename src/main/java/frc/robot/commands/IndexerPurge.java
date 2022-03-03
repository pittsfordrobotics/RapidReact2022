package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;

public class IndexerPurge extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final Intake intake = Intake.getInstance();

    public IndexerPurge() {
        addRequirements(this.indexer, this.intake);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        intake.extend();
        indexer.purge();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        intake.retract();
        indexer.stomachMotorOff();
        indexer.towerMotorOff();
        indexer.resetEverything();
    }
}