package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drive;


public class ClimberAlign extends CommandBase {
    private final Climber climber = Climber.getInstance();
    private final Drive drive = Drive.getInstance();

    public ClimberAlign() {
        addRequirements(this.climber, this.drive);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (!climber.getRightSensor() && !climber.getLeftSensor()) {
            drive.driveArcade(-0.1, 0, false);
        }
        else if  (climber.getRightSensor() && !climber.getLeftSensor()) {
            drive.driveArcade(0, -0.1, false);
        }
        else if  (!climber.getRightSensor() && climber.getLeftSensor()) {
            drive.driveArcade(0, 0.1, false);
        }
    }

    @Override
    public boolean isFinished() {
        return climber.getRightSensor() && climber.getLeftSensor();
    }

    @Override
    public void end(boolean interrupted) {
    }
}