package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;


public class IntakeRetract extends CommandBase {
    private final Intake intake = Intake.getInstance();

    public IntakeRetract() {
        addRequirements(this.intake);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        intake.retract();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}