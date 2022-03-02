package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;

public class CG_ClimberAuto extends SequentialCommandGroup {
    public CG_ClimberAuto() {
        super(
            new ClimberAlign(),
            new DrivePathing(Constants.TRAJECTORY_CLIMBER_BACKWARD),
            new ParallelCommandGroup(
                new ClimberForward(),
                new DrivePathing(Constants.TRAJECTORY_CLIMBER_FORWARD)
            ),
            new ClimberReverse(),
            new ClimberForward(),
            new ClimberMaintain()
        );
    }
}