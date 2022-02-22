package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.random.Constants;

public class AutoSecondLow3 extends SequentialCommandGroup {
    public AutoSecondLow3() {
        super(
                new IntakeDown(),
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL3_LOW3),
                new IntakeUp(),
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BALL3_LOW4),
                new CG_LowShot()
        );
    }
}