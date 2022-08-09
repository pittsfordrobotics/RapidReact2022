package com.team3181.frc2022.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import com.team3181.frc2022.subsystems.drive.Drive;


public class DriveSetPose extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final Pose2d pose;

    public DriveSetPose(Pose2d pose) {
        this.pose = pose;
        addRequirements(this.drive);
    }

    @Override
    public void initialize() {
        drive.resetOdometry(pose);
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