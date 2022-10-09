package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotState;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Vision;

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
            drive.rotate(-MathUtil.clamp(pidController.calculate(realRot) + Math.signum(RobotState.getInstance().getShortestRotationToHub().getDegrees()) * 0.1, -0.5, 0.5));
        }
        else {
            if (!hasVisionSetpoint) {
                pidController.setSetpoint(0);
                hasVisionSetpoint = true;
            }
            drive.rotate(-MathUtil.clamp(pidController.calculate(vision.getHorizontal()) + Math.signum(vision.getHorizontal()) * 0.1, -0.5, 0.5));
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
    }
}