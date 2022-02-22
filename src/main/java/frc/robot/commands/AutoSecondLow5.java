package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.random.Constants;

public class AutoSecondLow5 extends SequentialCommandGroup {
    public AutoSecondLow5() {
        super(
            new IntakeDown(),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL5_LOW3),
            new WaitCommand(2),
            new IntakeUp(),
            new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL5_LOW4),
            new CG_LowShot()
        );
    }
}