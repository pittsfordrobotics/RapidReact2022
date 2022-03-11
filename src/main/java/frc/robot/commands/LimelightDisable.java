package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;


public class LimelightDisable extends CommandBase {
    private final Limelight limelight = Limelight.getInstance();

    public LimelightDisable() {
        addRequirements(this.limelight);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        limelight.disable();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}