package com.team3181.frc2022.commands;

import com.team3181.frc2022.RobotState;
import com.team3181.frc2022.subsystems.drive.Drive;
import com.team3181.frc2022.subsystems.vision.Vision;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveAutoSnap extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final Vision vision = Vision.getInstance();
    private boolean hasDriveSetpoint;
    private boolean hasVisionSetpoint;
    private final PIDController pidController = new PIDController(0.01,0,0);

    public DriveAutoSnap() {
        addRequirements(this.drive, this.vision);
    }

    @Override
    public void initialize() {
        hasDriveSetpoint = false;
        hasVisionSetpoint = false;
        drive.setTempThrottle(0.6);
        pidController.setTolerance(5);
    }

    @Override
    public void execute() {
        if (!vision.hasTarget()) {
            double realRot = drive.getAngle();
            double rot = RobotState.getInstance().getShortestRotationToHub().getDegrees() + realRot;
            if (!hasDriveSetpoint) {
                pidController.setSetpoint(rot);
                hasDriveSetpoint = true;
            }
            drive.driveArcade(0, -MathUtil.clamp(pidController.calculate(realRot) + Math.signum(RobotState.getInstance().getShortestRotationToHub().getDegrees()) * 0.1, -0.5, 0.5), false);
        }
        else {
            if (!hasVisionSetpoint) {
                pidController.setSetpoint(0);
                hasVisionSetpoint = true;
            }
            drive.driveArcade(0, -MathUtil.clamp(pidController.calculate(vision.getHorizontal()) + Math.signum(vision.getHorizontal()) * 0.1, -0.5, 0.5), false);
        }
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        RobotState.getInstance().setSnapped(true);
        drive.setVolts(0,0);
        drive.setThrottleWithTemp();
    }
}