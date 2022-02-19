package frc.robot.commands;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;


public class DriveResetOdometry extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private Trajectory trajectory;

    public DriveResetOdometry(Trajectory trajectory) {
        addRequirements(this.drive);
        this.trajectory = trajectory;
    }

    @Override
    public void initialize() {
        drive.resetOdometry(trajectory.getInitialPose());
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}