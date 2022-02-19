package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;


public class IntakeDown extends CommandBase {
    private final Intake intake = Intake.getInstance();

    public IntakeDown() {
        addRequirements(this.intake);
    }

    @Override
    public void initialize() {
        intake.extend();
    }

    @Override
    public void execute() {
        intake.motorOn();
    }

    @Override
    public boolean isFinished() {
//        TODO: add code from indexer
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}