package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class AutoSecondLow3 extends SequentialCommandGroup {
    public AutoSecondLow3() {
        super(
            new IntakeDown(),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL3_NUMBER3),
            new IntakeUp(),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL3_NUMBER4),
            new DriveTurn(180),
            new IndexerWaitForArmed(),
            new CG_SpeedShot(Constants.SHOOTER_TARMAC_SPEED)
        );
    }
}