package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;


public class DriveZero extends CommandBase {
    private final Drive drive = Drive.getInstance();

    public DriveZero() {
        addRequirements(this.drive);
    }

    @Override
    public void initialize() {
        drive.driveVolts(0,0);
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