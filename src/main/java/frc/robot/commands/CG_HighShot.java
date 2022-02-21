package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_HighShot extends SequentialCommandGroup {
    public CG_HighShot() {
        super(
                new ShooterLime(),
                new CG_ReadyToShoot(),
                new IndexerShoot(),
                new CG_ShooterDefault()
        );
    }
}