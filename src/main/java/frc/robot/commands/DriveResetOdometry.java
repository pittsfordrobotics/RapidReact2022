package frc.robot.commands;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;


public class DriveResetOdometry extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final Trajectory trajectory;
    private final boolean reset;

    public DriveResetOdometry(Trajectory trajectory, boolean reset) {
        this.trajectory = trajectory;
        this.reset = reset;
        addRequirements(this.drive);
    }

    @Override
    public void initialize() {
        if (reset) {
            drive.resetOdometry(trajectory.getInitialPose());
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}