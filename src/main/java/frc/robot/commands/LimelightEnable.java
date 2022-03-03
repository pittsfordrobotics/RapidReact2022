package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Limelight;

public class LimelightEnable extends CommandBase {
    private final Limelight limelight = Limelight.getInstance();

    public LimelightEnable() {
        addRequirements(this.limelight);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        limelight.enable();
        limelight.setPipeline(Constants.LIMELIGHT_PIPELINE);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}