package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.indexer.Indexer;
import frc.robot.subsystems.intake.Intake;


public class IntakeUpNoInterupt extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final Intake intake = Intake.getInstance();

    public IntakeUpNoInterupt() {
        addRequirements(this.intake);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.retract();
        intake.motorOff();
    }

    @Override
    public boolean isFinished() {
        return !indexer.isFull();
    }

    @Override
    public void end(boolean interrupted) {

    }
}