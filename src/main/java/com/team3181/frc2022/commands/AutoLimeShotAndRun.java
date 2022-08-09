package com.team3181.frc2022.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import com.team3181.frc2022.Trajectories;

public class AutoLimeShotAndRun extends SequentialCommandGroup {
    public AutoLimeShotAndRun() {
        super(
                new IndexerWaitForArmed(),
                new CG_LimeShot(),
                new DrivePathing(Trajectories.THREE_METERS_BACKWARD, false)
        );
    }
}