package com.team3181.frc2022.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.team3181.frc2022.RobotState;
import com.team3181.frc2022.subsystems.vision.Vision;


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
        return RobotState.getInstance().isSnapped() || !Vision.getInstance().isConnected();
    }

    @Override
    public void end(boolean interrupted) {

    }
}