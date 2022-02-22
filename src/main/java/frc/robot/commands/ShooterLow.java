package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;


public class ShooterLow extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();

    public ShooterLow() {
        addRequirements(this.shooter);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setShooterSpeed(Constants.SHOOTER_LOW_SPEED);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}