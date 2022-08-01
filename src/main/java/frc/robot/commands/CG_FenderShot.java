package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

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