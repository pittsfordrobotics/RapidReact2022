package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_LowShot extends SequentialCommandGroup {
    public CG_LowShot() {
        super(
                new ShooterLow(),
                new ShooterPrimed(),
                new IndexerShoot(),
                new WaitCommand(1),
                new ShooterZero()
        );
    }
}