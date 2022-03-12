package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

public class AutoSecondLow5 extends SequentialCommandGroup {
    public AutoSecondLow5() {
        super(
            new IntakeDown(),
            new WaitCommand(0.2),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL5_NUMBER3),
            new ParallelCommandGroup(
                new IntakeUp(),
                new DriveTurn(98.62), // 98.62 degrees clockwise
                new ShooterSpeed(Constants.SHOOTER_AUTO_5_SPEED)
            ),
            new ParallelCommandGroup(
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL5_NUMBER4),
                new IndexerWaitForArmed()
            ),
            new IndexerShoot(),
            new WaitCommand(0.5),
            new ParallelCommandGroup(
                    new ShooterZero(),
                    new DriveTurn(180)
            ),
            new ParallelCommandGroup(
                new IntakeDown(),
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL5_NUMBER5)
            ),
            new WaitCommand(1),
            new ParallelCommandGroup(
                new ShooterSpeed(Constants.SHOOTER_AUTO_5_SPEED),
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL5_NUMBER6)
            ),
            new IndexerWaitForArmed(),
            new IndexerShoot(),
            new WaitCommand(0.5),
            new ShooterZero()
        );
    }
}