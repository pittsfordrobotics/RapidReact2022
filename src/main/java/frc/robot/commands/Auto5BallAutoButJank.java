package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Trajectories;

public class Auto5BallAutoButJank extends SequentialCommandGroup {
    public Auto5BallAutoButJank() {
        super(
                new TimeKeeper(true),
                new IntakeDown(),
                new ShooterHoodSet(5500, 0),
                new DrivePathing(Trajectories.PP_BALL5_NUMBER1,true),
                new IndexerLoaded(2, Constants.INDEXER_LOADING_WAIT),
                new DrivePathing(Trajectories.PP_BALL5_NUMBER2,false),
                new DriveTurn(-100),
                new IndexerShoot(),
                new WaitCommand(Constants.SHOOTER_SHOT_CALM_DELAY),
                new ShooterHoodSet(5000, 0),
                new ParallelDeadlineGroup(
                    new IndexerLoaded(1, 2),
                    new DrivePathing(Trajectories.PP_BALL5_NUMBER3, false)
                ),
                new WaitCommand(0.2),
                new ParallelDeadlineGroup(
                    new WaitCommand(1),
                    new ShooterHoodPrimed()
                ),
                new IndexerShoot(),
                new WaitCommand(Constants.SHOOTER_SHOT_CALM_DELAY),
                new DrivePathing(Trajectories.PP_BALL5_NUMBER4,false),
                new WaitCommand(0.5),
                new ParallelDeadlineGroup(
                    new IndexerLoaded(2, Constants.INDEXER_HUMAN_LOADING_WAIT),
                    new DrivePathing(Trajectories.PP_BALL5_NUMBER5,false)
                ),
                new IntakeUp(),
                new ShooterHoodSet(4700, 0),
                new DrivePathing(Trajectories.PP_BALL5_NUMBER6,false),
                new DriveTurn(180),
                new IndexerShoot(),
                new WaitCommand(Constants.SHOOTER_SHOT_CALM_DELAY),
                new ShooterHoodZero(),
                new TimeKeeper(false)
        );
    }
}