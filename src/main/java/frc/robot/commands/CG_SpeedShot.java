package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_SpeedShot extends SequentialCommandGroup {
    public CG_SpeedShot(int speed) {
        super(
                new ShooterSpeed(speed),
                new ShooterPrimed(),
                new IndexerShoot(),
                new WaitCommand(1),
                new ShooterZero()
        );
    }
}