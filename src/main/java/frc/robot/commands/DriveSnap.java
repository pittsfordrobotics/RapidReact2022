package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotState;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Vision;
import frc.robot.util.BetterMath;

public class DriveSnap extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final Vision vision = Vision.getInstance();
    private boolean hasDriveSetpoint;
    private boolean hasVisionSetpoint;
    private final PIDController pidController = new PIDController(0.01,0,0);
    private final SnapPosition position;
    public enum SnapPosition {
        LEFT_FENDER_CLOSE(338.5), RIGHT_FENDER_CLOSE(68.5), LEFT_FENDER_FAR(248.5), RIGHT_FENDER_FAR(158.5),
        FORWARD(0), LEFT(90), RIGHT(270), BACKWARD(180);

        private final double angle;
        SnapPosition(double angle) {
            this.angle = angle;
        }

        public double getAngle() {
            return angle;
        }
    }

    public DriveSnap(SnapPosition position) {
        this.position = position;
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
            double rot = BetterMath.getShortestRotation(realRot, position.getAngle()).getDegrees() + realRot;
            if (!hasDriveSetpoint) {
                pidController.setSetpoint(rot);
                hasDriveSetpoint = true;
            }
            drive.rotate(-MathUtil.clamp(pidController.calculate(realRot) + Math.signum(BetterMath.getShortestRotation(realRot, position.getAngle()).getDegrees()) * 0.1, -0.5, 0.5));
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