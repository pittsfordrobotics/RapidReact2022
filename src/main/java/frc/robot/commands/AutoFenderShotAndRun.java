package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Trajectories;

public class AutoFenderShotAndRun extends SequentialCommandGroup {
    public AutoFenderShotAndRun() {
        super(
                new IndexerWaitForArmed(),
                new CG_FenderShot(),
                new DrivePathing(Trajectories.THREE_METERS_BACKWARD, false)
        );
    }
}