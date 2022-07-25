package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.Shooter;


public class ShooterSpeed extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final int speed;

    public ShooterSpeed(int speed) {
        addRequirements(this.shooter);
        this.speed = speed;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.updateSpeed(speed);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}