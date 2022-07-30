package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotState;
import frc.robot.subsystems.drive.Drive;


public class ClimberSetState extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final boolean state;

    public ClimberSetState(boolean state) {
        addRequirements(this.drive);
        this.state = state;
    }

    @Override
    public void initialize() {
        RobotState.getInstance().setClimbing(state);
        drive.setThrottle(state ? 0.2 : 0.6);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}