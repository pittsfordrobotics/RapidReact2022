package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeSmart extends CommandBase {
    private final Intake intake = Intake.getInstance();

    public IntakeSmart() {
        addRequirements(this.intake);
    }

    @Override
    public void initialize() {
        intake.toggleSolenoid();
    }

    @Override
    public void execute() {
        intake.smartMotor();
    }

    @Override
    public boolean isFinished() {
//      TODO:  add code here from indexer to detect when the robot has two balls
        return !intake.isExtended();
    }

    @Override
    public void end(boolean interrupted) {
        intake.retract();
    }
}