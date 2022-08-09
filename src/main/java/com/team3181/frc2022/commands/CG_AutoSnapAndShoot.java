package com.team3181.frc2022.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import com.team3181.frc2022.Constants;

public class CG_AutoSnapAndShoot extends SequentialCommandGroup {
    public CG_AutoSnapAndShoot() {
        super(
                new DriveAutoSnap(),
                new ShooterHoodLime(),
                new ShooterHoodPrimed(),
                new IndexerShoot(),
                new WaitCommand(Constants.SHOOTER_SHOT_CALM_DELAY),
                new ShooterHoodZero()
        );
    }
}