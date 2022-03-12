package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

public class AutoFirstBottomLow2 extends SequentialCommandGroup {
    public AutoFirstBottomLow2() {
        super(
            new IntakeDown(),
            new WaitCommand(1),
//            new WaitCommand(0.5),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_NUMBER1),
            new IndexerFullyArmed(),
            new IntakeUp(),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_NUMBER2),
            new DriveTurn(155),
            new CG_SpeedShot(Constants.SHOOTER_TARMAC_SPEED),
            new DriveTurn(180)
        );
    }
}