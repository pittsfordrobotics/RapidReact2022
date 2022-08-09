package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.intake.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class IntakeToggle extends CommandBase {
    private final Intake intake = Intake.getInstance();

    public IntakeToggle() {
        addRequirements(this.intake);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.toggleSolenoid();
        intake.autoMotor();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}