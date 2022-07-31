package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Trajectories;

public class AutoLimeShotAndRun extends SequentialCommandGroup {
    public AutoLimeShotAndRun() {
        super(
                new IndexerWaitForArmed(),
                new CG_LimeShot(),
                new DrivePathing(Trajectories.THREE_METERS_BACKWARD, false)
        );
    }
}