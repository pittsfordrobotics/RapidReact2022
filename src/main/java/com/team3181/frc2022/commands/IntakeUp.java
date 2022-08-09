package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.intake.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class IntakeUp extends CommandBase {
    private final Intake intake = Intake.getInstance();

    public IntakeUp() {
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
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}