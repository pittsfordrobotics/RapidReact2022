package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CG_ClimberCalibrate extends SequentialCommandGroup {
    public CG_ClimberCalibrate() {
        super(
                new ClimberCalibrateForward(),
                new ClimberResetEncoders(),
                new ClimberCalibrateReverse(),
                new ClimberCalibrateCenter(),
                new ClimberResetEncoders()
        );
    }
}