package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_UnoShot extends SequentialCommandGroup {
    public CG_UnoShot() {
        super(
                new ShooterLow(),
                new ShooterPrimed(),
                new IndexerUnoShoot(),
                new WaitCommand(1),
                new ShooterZero()
        );
    }
}