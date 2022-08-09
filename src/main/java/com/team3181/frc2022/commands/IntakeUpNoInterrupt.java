package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.indexer.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import com.team3181.frc2022.subsystems.intake.Intake;


public class IntakeUpNoInterrupt extends CommandBase {
    private final Indexer indexer = Indexer.getInstance();
    private final Intake intake = Intake.getInstance();

    public IntakeUpNoInterrupt() {
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