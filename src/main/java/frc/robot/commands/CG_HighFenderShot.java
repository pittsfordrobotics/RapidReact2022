package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_HighFenderShot extends SequentialCommandGroup {
    public CG_HighFenderShot() {
        super(
            new ShooterHighFender(),
            new ShooterPrimed(),
            new IndexerShoot(),
            new ShooterZero()
        );
    }
}