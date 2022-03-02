package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;


public class ShooterLime extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Limelight limelight = Limelight.getInstance();

    public ShooterLime() {
        addRequirements(this.limelight, this.shooter);
    }

    @Override
    public void initialize() {
        limelight.enable();
        limelight.setPipeline(Constants.LIMELIGHT_PIPELINE);
    }

    @Override
    public void execute() {
        shooter.setSpeed(Constants.SHOOTER_SPEED_MAP.lookup(limelight.getDistance()));
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        limelight.disable();
    }
}