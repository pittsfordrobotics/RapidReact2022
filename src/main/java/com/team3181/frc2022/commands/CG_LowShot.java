package com.team3181.frc2022.commands;


import com.team3181.frc2022.Constants;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_LowShot extends SequentialCommandGroup {
    public CG_LowShot() {
        super(
                new ShooterHoodLow(),
                new ShooterHoodPrimed(),
                new IndexerShoot(),
                new WaitCommand(Constants.SHOOTER_SHOT_CALM_DELAY),
                new ShooterHoodZero()
        );
    }
}