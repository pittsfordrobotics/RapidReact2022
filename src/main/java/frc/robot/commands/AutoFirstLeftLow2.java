package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

public class AutoFirstLeftLow2 extends SequentialCommandGroup {
    public AutoFirstLeftLow2() {
        super(
            new IntakeDown(),
            new WaitCommand(0.5),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_LEFT_BALL2_NUMBER1),
            new IndexerWaitForArmed(),
            new IntakeUp(),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_LEFT_BALL2_NUMBER2),
            new DriveTurn(90),
            new CG_SpeedShot(Constants.SHOOTER_TARMAC_SPEED),
            new DriveTurn(180)
        );
    }
}