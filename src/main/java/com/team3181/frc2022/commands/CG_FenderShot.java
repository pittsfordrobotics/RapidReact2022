package com.team3181.frc2022.commands;

import com.team3181.frc2022.Constants;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_FenderShot extends SequentialCommandGroup {
    public CG_FenderShot() {
        super(
                new ShooterHoodSet(Constants.SHOOTER_FENDER_SPEED, Constants.HOOD_FENDER_ANGLE),
                new ShooterHoodPrimed(),
                new IndexerShoot(),
                new WaitCommand(Constants.SHOOTER_SHOT_CALM_DELAY),
                new ShooterHoodZero()
        );
    }
}