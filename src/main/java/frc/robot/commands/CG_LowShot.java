package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_LowShot extends SequentialCommandGroup {
    public CG_LowShot() {
        super(
                new ShooterHoodLow(),
                new ShooterHoodPrimed(),
                new IndexerShoot(),
                new WaitCommand(0.5),
                new ShooterHoodZero()
        );
    }
}