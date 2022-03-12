package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

public class AutoSecondLow3 extends SequentialCommandGroup {
    public AutoSecondLow3() {
        super(
            new IntakeDown(),
            new WaitCommand(1),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL3_NUMBER3),
            new IndexerWaitForArmed(),
            new IntakeUp(),
            new DriveTurn(126),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL3_NUMBER4),
            new DriveTurn(180),
            new CG_SpeedShot(Constants.SHOOTER_TARMAC_SPEED)
        );
    }
}