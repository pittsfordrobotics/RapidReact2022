package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class AutoFirstBottomLow2 extends SequentialCommandGroup {
    public AutoFirstBottomLow2() {
        super(
            new IntakeDown(),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_NUMBER1),
            new IntakeUp(),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_NUMBER2),
            new DriveTurn(180),
            new IndexerWaitForArmed(),
            new CG_SpeedShot(Constants.SHOOTER_TARMAC_SPEED),
            new DriveTurn(180)
        );
    }
}