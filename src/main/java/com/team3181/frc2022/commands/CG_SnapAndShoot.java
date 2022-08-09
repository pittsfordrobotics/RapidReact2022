package com.team3181.frc2022.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import com.team3181.frc2022.Constants;
import com.team3181.frc2022.commands.DriveSnap.SnapPosition;

public class CG_SnapAndShoot extends SequentialCommandGroup {
    public CG_SnapAndShoot(SnapPosition position) {
        super(
                new DriveSnap(position),
                new ShooterHoodLime(),
                new ShooterHoodPrimed(),
                new IndexerShoot(),
                new WaitCommand(Constants.SHOOTER_SHOT_CALM_DELAY),
                new ShooterHoodZero()
        );
    }
}