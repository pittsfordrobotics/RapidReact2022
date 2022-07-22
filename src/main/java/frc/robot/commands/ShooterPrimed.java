package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.Shooter;


public class ShooterPrimed extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();

    public ShooterPrimed() {
        addRequirements(this.shooter);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return shooter.isAtSpeed();
    }

    @Override
    public void end(boolean interrupted) {
    }
}