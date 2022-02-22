package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.random.Constants;

public class AutoFirstBottomHigh2 extends SequentialCommandGroup {
    public AutoFirstBottomHigh2() {
        super(
                new IntakeDown(),
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_ALL1),
                new IntakeUp(),
                new DriveTurn(180),
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_HIGH2),
                new CG_HighShot(),
                new CG_DriveBack180Turn()
        );
    }
}