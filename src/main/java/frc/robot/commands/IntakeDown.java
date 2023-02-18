package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.indexer.Indexer;
import frc.robot.subsystems.intake.Intake;

public class IntakeDown extends CommandBase {
    private final Intake intake = Intake.getInstance();
    private final Indexer indexer = Indexer.getInstance();

    public IntakeDown() {
        addRequirements(this.intake);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.extend();
        intake.motorOn();
        indexer.override();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}