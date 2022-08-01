package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Trajectories;

public class AutoTopLeft2Reject2 extends SequentialCommandGroup {
    public AutoTopLeft2Reject2() {
        super(
            new TimeKeeper(true),
            new IndexerRejection(false),
            new AutoTopLeft1Reject1(),
            new IntakeDown(),
            new DrivePathing(Trajectories.PP_TOP_LEFT_BALL2_REJECT2_NUMBER2, false),
            new IndexerLoaded(1, Constants.INDEXER_LOADING_WAIT),
            new DriveTurn(-100),
            new CG_LimeShot(),
            new DrivePathing(Trajectories.PP_TOP_LEFT_BALL2_REJECT2_NUMBER3, false),
            new IndexerLoaded(1, Constants.INDEXER_LOADING_WAIT),
            new ParallelCommandGroup(
                    new DriveTurn(90),
                    new ShooterHoodSet(Constants.SHOOTER_AUTO_REJECT_SPEED, Constants.HOOD_AUTO_REJECT_ANGLE)
            ),
            new ShooterHoodPrimed(),
            new IndexerShoot(),
            new DriveTurn(180),
            new IntakeUp(),
            new IndexerRejection(true),
            new TimeKeeper(false)
        );
    }
}