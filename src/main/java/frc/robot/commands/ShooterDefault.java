package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;


public class ShooterDefault extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();

    public ShooterDefault() {
        addRequirements(this.shooter);
    }

    @Override
    public void initialize() {
        shooter.setShooterSpeed(0);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}