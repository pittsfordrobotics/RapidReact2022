package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_HighShot extends SequentialCommandGroup {
    public CG_HighShot() {
        super(
                new ShooterLime(),
                new ShooterPrimed(),
                new IndexerShoot(),
                new ShooterZero()
        );
    }
}