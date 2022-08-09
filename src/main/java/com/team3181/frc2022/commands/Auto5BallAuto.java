package com.team3181.frc2022.commands;


import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import com.team3181.frc2022.Constants;
import com.team3181.frc2022.Trajectories;

public class Auto5BallAuto extends SequentialCommandGroup {
    public Auto5BallAuto() {
        super(
                new TimeKeeper(true),
                new IntakeDown(),
                new DrivePathing(Trajectories.PP_BALL5_NUMBER1,true),
                new IndexerLoaded(2, Constants.INDEXER_LOADING_WAIT),
                new DrivePathing(Trajectories.PP_BALL5_NUMBER2,false),
                new DriveTurn(-100),
                new CG_LimeShot(),
                new ParallelDeadlineGroup(
                    new IndexerLoaded(1, Constants.INDEXER_LOADING_WAIT + 0.2),
                    new DrivePathing(Trajectories.PP_BALL5_NUMBER3, false)
                ),
                new CG_LimeShot(),
                new DrivePathing(Trajectories.PP_BALL5_NUMBER4,false),
                new WaitCommand(0.5),
                new ParallelDeadlineGroup(
                    new IndexerLoaded(2, Constants.INDEXER_HUMAN_LOADING_WAIT),
                    new DrivePathing(Trajectories.PP_BALL5_NUMBER5,false)
                ),
                new IntakeUp(),
                new DrivePathing(Trajectories.PP_BALL5_NUMBER6,false),
                new DriveTurn(180),
                new CG_LimeShot(),
                new TimeKeeper(false)
        );
    }
}