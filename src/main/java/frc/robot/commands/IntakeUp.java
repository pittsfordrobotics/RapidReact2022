package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;


public class IntakeUp extends CommandBase {
    private final Intake intake = Intake.getInstance();

    public IntakeUp() {
        addRequirements(this.intake);
    }

    @Override
    public void initialize() {
        intake.retract();
        intake.motorOff();
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}