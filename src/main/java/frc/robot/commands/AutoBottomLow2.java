package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class AutoBottomLow2 extends SequentialCommandGroup {
    public AutoBottomLow2() {
        super(
                new IntakeDown(),
                new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_LOW1),
                new IntakeUp(),
                new DriveTurn(180),
                new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_BOTTOM_BALL2_LOW2),
                new CG_LowShot()
        );
    }
}