package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

// TODO: write code to shoot out wrong ball, preferable with one button and rumble to operator controller
public class IntakeDown extends CommandBase {
    private final Intake intake = Intake.getInstance();

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
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}