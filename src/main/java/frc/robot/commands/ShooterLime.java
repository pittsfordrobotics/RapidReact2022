package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.shooter.Shooter;


public class ShooterLime extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Vision vision = Vision.getInstance();

    public ShooterLime() {
        addRequirements(this.vision, this.shooter);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.updateSetpoint(Constants.SHOOTER_SPEED_MAP.lookup(vision.getDistance()), false);
    }

    @Override
    public boolean isFinished() {
        return vision.hasTarget();
    }

    @Override
    public void end(boolean interrupted) {
    }
}