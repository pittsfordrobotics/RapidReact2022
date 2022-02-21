package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class AutoBack180Turn extends SequentialCommandGroup {
    public AutoBack180Turn() {
        super(
            new AutoPathing(Constants.TRAJECTORY_BACKWARD),
            new AutoTurn(180)
        );
    }
}