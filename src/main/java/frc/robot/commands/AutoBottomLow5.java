package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

public class AutoBottomLow5 extends SequentialCommandGroup {
    public AutoBottomLow5() {
        super(
            new IntakeDown(),
            new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_LOW1),
            new IntakeUp(),
            new AutoTurn(180),
            new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_LOW2),
            new CG_LowShot(),
            new AutoPathing(Constants.TRAJECTORY_BACKWARD),
            new AutoTurn(180),
            new IntakeDown(),
            new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL5_LOW3),
            new WaitCommand(2),
            new IntakeUp(),
            new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL5_LOW4),
            new CG_LowShot()
        );
    }
}