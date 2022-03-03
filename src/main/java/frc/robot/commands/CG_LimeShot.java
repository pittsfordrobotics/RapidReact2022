package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_LimeShot extends SequentialCommandGroup {
    public CG_LimeShot() {
        super(
                new ShooterLime(),
                new ShooterPrimed(),
                new IndexerShoot(),
                new ShooterZero()
        );
    }
}