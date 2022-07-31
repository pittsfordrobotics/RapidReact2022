package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_ClimberAuto extends SequentialCommandGroup {
    public CG_ClimberAuto() {
        super(

            new ClimberReverse(),
            new WaitCommand(1),
            new ClimberForward(),
            new ClimberMaintain()
        );
    }
}