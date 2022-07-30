package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Vision;

// TODO: add to use robot pose
public class DriveTurnLime extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final Vision vision = Vision.getInstance();
    private final PIDController pidController = new PIDController(0.01,0,0);

    public DriveTurnLime() {
        addRequirements(this.drive, this.vision);
    }

    @Override
    public void initialize() {
        drive.setTempThrottle(0.6);
        pidController.setSetpoint(0);
        pidController.setTolerance(5);
    }

    @Override
    public void execute() {
        if (!vision.hasTarget()) {
            drive.driveArcade(0, 0.5, false);
        }
        else {
            drive.driveArcade(0, -MathUtil.clamp(pidController.calculate(vision.getHorizontal()) + (vision.getHorizontal() > 0 ? 0.1 : -0.1), -0.2, 0.2), false);
        }
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        drive.setThrottleWithTemp();
    }
}