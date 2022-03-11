package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_ClimberCalibrate extends SequentialCommandGroup {
    public CG_ClimberCalibrate() {
        super(
                new WaitCommand(1),
                new ClimberResetEncoders(),
                new ClimberCalibrateForward(),
                new WaitCommand(1),
                new ClimberResetEncoders(),
                new ClimberCalibrateReverse(),
                new WaitCommand(1),
                new ClimberCalibrateCenter(),
                new WaitCommand(1),
                new ClimberResetEncoders()
        );
    }
}