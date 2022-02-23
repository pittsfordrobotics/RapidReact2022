package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Limelight;
import frc.robot.util.PIDTuner;

// TODO: make this work
public class DriveTurnLime extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final Limelight limelight = Limelight.getInstance();
    private final PIDController pidController = new PIDController(0.1,0,0);
    private final PIDTuner pidTuner = new PIDTuner("DriveTunerLime", pidController);
    private double throttle;

    public DriveTurnLime() {
        addRequirements(this.drive, this.limelight);
    }

    @Override
    public void initialize() {
        limelight.setPipeline(Limelight.Pipelines.PRACTICE);
        limelight.enable();
        drive.setTempThrottle(0.6);
        pidController.setSetpoint(0);
        pidController.setTolerance(1);
    }

    @Override
    public void execute() {
        pidTuner.setP();
        drive.driveArcade(0, MathUtil.clamp(pidController.calculate(limelight.getHorizontal()) + limelight.getHorizontal() > 0 ? 0.1 : -0.1, -0.5, 0.5), false);
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        drive.setThrottleWithTemp();
        limelight.disable();
    }
}