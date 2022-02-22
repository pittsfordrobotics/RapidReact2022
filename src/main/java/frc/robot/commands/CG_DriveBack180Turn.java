package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class CG_DriveBack180Turn extends SequentialCommandGroup {
    public CG_DriveBack180Turn() {
        super(
            new DrivePathing(Constants.TRAJECTORY_ONE_METER_BACKWARD),
            new DriveTurn(180)
        );
    }
}