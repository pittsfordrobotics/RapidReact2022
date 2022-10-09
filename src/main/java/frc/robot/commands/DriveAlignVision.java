package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotState;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Vision;

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
        pidController.setSetpoint(0);
        pidController.setTolerance(5);
    }

    @Override
    public void execute() {
        drive.rotate(-MathUtil.clamp(pidController.calculate(vision.getHorizontal()) + Math.signum(vision.getHorizontal()) * 0.1, -0.5, 0.5));
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        drive.setVolts(0,0);
        RobotState.getInstance().setSnapped(true);
    }
}