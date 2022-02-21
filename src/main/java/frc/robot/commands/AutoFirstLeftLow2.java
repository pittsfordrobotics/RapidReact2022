package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class AutoFirstLeftLow2 extends SequentialCommandGroup {
    public AutoFirstLeftLow2() {
        super(
                new IntakeDown(),
                new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_LEFT_BALL2_LOW1),
                new IntakeUp(),
                new AutoTurn(180),
                new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_LEFT_BALL2_LOW2),
                new CG_LowShot()
        );
    }
}