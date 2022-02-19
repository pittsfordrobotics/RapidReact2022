package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;

public class IntakeSmart extends CommandBase {
    private final Intake intake = Intake.getInstance();
    private final Indexer indexer = Indexer.getInstance();

    public IntakeSmart() {
        addRequirements(this.intake, this.indexer);
    }

    @Override
    public void initialize() {
        intake.toggleSolenoid();
    }

    @Override
    public void execute() {
        intake.smartMotor();
    }

    @Override
    public boolean isFinished() {
        return !intake.isExtended() || indexer.isFull();
    }

    @Override
    public void end(boolean interrupted) {
    }
}