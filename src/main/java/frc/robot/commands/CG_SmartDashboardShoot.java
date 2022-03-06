package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_SmartDashboardShoot extends SequentialCommandGroup {
    public CG_SmartDashboardShoot() {
        super(
                new ShooterSmartDashboard(),
                new ShooterPrimed(),
                new IndexerShoot()
        );
    }
}