package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Trajectories;

public class AutoTop2Reject1 extends SequentialCommandGroup {
    public AutoTop2Reject1() {
        super(
            new IndexerRejection(false),
            new CG_IntakeWiggle(),
            new DrivePathing(Trajectories.PP_TOP_BALL2_REJECT1_NUMBER1, true),
            new IndexerLoaded(2, Constants.INDEXER_LOADING_WAIT),
            new DriveTurn(180),
            new CG_LimeShot(),
            new DriveTurn(45),
            new DrivePathing(Trajectories.PP_TOP_BALL2_REJECT1_NUMBER2, false),
            new IndexerLoaded(1, Constants.INDEXER_LOADING_WAIT),
            new IntakeUp(),
            new ParallelCommandGroup(
                new DriveTurn(70),
                new ShooterHoodSet(Constants.SHOOTER_AUTO_REJECT_SPEED, Constants.HOOD_AUTO_REJECT_ANGLE)
            ),
            new ShooterHoodPrimed(),
            new IndexerShoot(),
            new DriveTurn(-160),
            new IndexerRejection(true)
        );
    }
}