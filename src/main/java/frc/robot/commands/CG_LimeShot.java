package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_LimeShot extends SequentialCommandGroup {
    public CG_LimeShot() {
        super(
                new LimelightEnable(),
                new ShooterLime(),
                new DriveTurnLime(),
                new ShooterLime(),
                new ShooterPrimed(),
                new IndexerShoot(),
                new WaitCommand(1),
                new LimelightDisable(),
                new ShooterZero()
        );
    }
}