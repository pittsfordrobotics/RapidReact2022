package com.team3181.frc2022.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import com.team3181.frc2022.RobotState;
import com.team3181.frc2022.subsystems.drive.Drive;
import com.team3181.frc2022.subsystems.vision.Vision;

public class DriveAlignVision extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final Vision vision = Vision.getInstance();
    private final PIDController pidController = new PIDController(0.01,0,0);

    public DriveAlignVision() {
        addRequirements(this.drive, this.vision);
    }

    @Override
    public void initialize() {
        RobotState.getInstance().setSnapped(false);
        drive.setTempThrottle(0.6);
        pidController.setSetpoint(0);
        pidController.setTolerance(5);
    }

    @Override
    public void execute() {
        drive.driveArcade(0, -MathUtil.clamp(pidController.calculate(vision.getHorizontal()) + Math.signum(vision.getHorizontal()) * 0.1, -0.5, 0.5), false);
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        drive.setVolts(0,0);
        drive.setThrottleWithTemp();
        RobotState.getInstance().setSnapped(true);
    }
}