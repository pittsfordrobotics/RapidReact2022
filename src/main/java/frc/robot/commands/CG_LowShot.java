package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_LowShot extends SequentialCommandGroup {
    public CG_LowShot() {
        super(
                new ShooterLow(),
                new ShooterPrimed(),
                new IndexerShoot(),
                new ShooterZero()
        );
    }
}