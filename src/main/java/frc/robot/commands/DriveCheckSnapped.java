package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotState;
import frc.robot.subsystems.vision.Vision;


public class DriveCheckSnapped extends CommandBase {

    public DriveCheckSnapped() {
        addRequirements();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return RobotState.getInstance().isSnapped() || !Vision.getInstance().isConnected() || DriverStation.isAutonomous();
    }

    @Override
    public void end(boolean interrupted) {

    }
}