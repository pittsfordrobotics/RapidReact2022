package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_LimeShot extends SequentialCommandGroup {
    public CG_LimeShot() {
        super(
                new DriveTurnSnap(),
                new ShooterHoodLime(),
                new ShooterHoodPrimed(),
                new IndexerShoot(),
                new WaitCommand(0.5),
                new ShooterHoodZero()
        );
    }
}