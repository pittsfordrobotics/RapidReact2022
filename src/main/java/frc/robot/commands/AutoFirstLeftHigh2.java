package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.random.Constants;

public class AutoFirstLeftHigh2 extends SequentialCommandGroup {
    public AutoFirstLeftHigh2() {
        super(
                new IntakeDown(),
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_LEFT_BALL2_ALL1),
                new IntakeUp(),
                new DriveTurn(180),
                new DrivePathing(Constants.TRAJECTORY_PATHPLANNER_LEFT_BALL2_HIGH2),
                new CG_HighShot(),
                new CG_DriveBack180Turn()
        );
    }
}