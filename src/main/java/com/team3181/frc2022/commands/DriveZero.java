package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.drive.Drive;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class DriveZero extends CommandBase {
    private final Drive drive = Drive.getInstance();

    public DriveZero() {
        addRequirements(this.drive);
    }

    @Override
    public void initialize() {
        drive.setVolts(0,0);
    }

    @Override
    public void execute() {}

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return true;
    }

}