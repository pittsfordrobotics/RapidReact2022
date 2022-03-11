package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
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
    }

    @Override
    public boolean isFinished() {
        return limelight.hasTarget();
    }

    @Override
    public void end(boolean interrupted) {

    }
}