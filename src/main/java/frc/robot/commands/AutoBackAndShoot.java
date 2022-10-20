package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Trajectories;

public class AutoBackAndShoot extends SequentialCommandGroup {
    public AutoBackAndShoot() {
        super(
                new ShooterHoodSet(4500, 0),
                new DrivePathing(Trajectories.ONE_METER_BACKWARD, true),
                new ParallelDeadlineGroup(
                    new WaitCommand(2),
                    new DriveAlignVision()
                ),
                new IndexerShoot(),
                new WaitCommand(3),
                new ShooterHoodZero()
        );
    }
}