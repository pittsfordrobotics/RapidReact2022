package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class AutoShootAndRun extends SequentialCommandGroup {
    public AutoShootAndRun() {
        super(
                new CG_LowShot(),
                new DrivePathing(Constants.TRAJECTORY_EXIT_TARMAC)
        );
    }
}