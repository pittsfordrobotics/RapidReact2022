package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class CG_ReadyToShoot extends ParallelCommandGroup {
    public CG_ReadyToShoot() {
        super(
                new HoodPrimed(),
                new ShooterPrimed()
        );
    }
}