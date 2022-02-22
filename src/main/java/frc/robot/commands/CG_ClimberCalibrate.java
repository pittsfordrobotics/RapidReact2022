package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_ClimberCalibrate extends SequentialCommandGroup {
    public CG_ClimberCalibrate() {
        super(
                new ClimberFront(),
                new ClimberResetEncoders(),
                new ClimberCenter()
        );
    }
}