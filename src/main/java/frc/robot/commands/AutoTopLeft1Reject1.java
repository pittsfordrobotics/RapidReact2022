package frc.robot.commands;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Trajectories;

public class AutoTopLeft1Reject1 extends SequentialCommandGroup {
    public AutoTopLeft1Reject1() {
        super(
            new IndexerRejection(false),
            new DriveSetPose(new Pose2d(6.05, 4 , new Rotation2d(Units.degreesToRadians(0)))),
            new ParallelCommandGroup(
                new IntakeDown(),
                new CG_LimeShot()
            ),
            new DriveTurn(180),
            new DrivePathing(Trajectories.PP_TOP_LEFT_BALL2_REJECT2_NUMBER1, false),
            new IndexerLoaded(1, Constants.INDEXER_LOADING_WAIT),
            new ParallelCommandGroup(
                new DriveTurn(-45),
                new ShooterHoodSet(Constants.SHOOTER_AUTO_REJECT_SPEED, Constants.HOOD_ANGLE_MAX)
            ),
            new ShooterHoodPrimed(),
            new IndexerShoot(),
            new WaitCommand(1),
            new ShooterHoodZero(),
            new DriveTurn(-45),
            new IntakeUp(),
            new IndexerRejection(true)
        );
    }
}