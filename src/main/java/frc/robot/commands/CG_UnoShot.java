package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_UnoShot extends SequentialCommandGroup {
    public CG_UnoShot() {
        super(
                new ShooterLow(),
                new ShooterPrimed(),
                new IndexerUnoShoot(),
                new ShooterZero()
        );
    }
}