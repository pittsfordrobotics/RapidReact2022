package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;


public class DriveSetThrottle extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final double throttle;

    public DriveSetThrottle(double throttle) {
        this.throttle = throttle;
        addRequirements(this.drive);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        drive.setThrottle(throttle);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}