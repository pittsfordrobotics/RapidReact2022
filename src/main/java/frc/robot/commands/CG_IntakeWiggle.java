package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class CG_IntakeWiggle extends SequentialCommandGroup {
    public CG_IntakeWiggle() {
        super(
                new IntakeDown(),
                new WaitCommand(0.2),
                new IntakeUp(),
                new WaitCommand(0.3),
                new IntakeDown()
        );
    }
}