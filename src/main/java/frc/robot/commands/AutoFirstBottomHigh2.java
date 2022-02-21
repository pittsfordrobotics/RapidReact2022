package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class AutoFirstBottomHigh2 extends SequentialCommandGroup {
    public AutoFirstBottomHigh2() {
        super(
                new IntakeDown(),
                new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_ALL1),
                new IntakeUp(),
                new AutoTurn(180),
                new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_HIGH2),
                new CG_HighShot(),
                new AutoBack180Turn()
        );
    }
}