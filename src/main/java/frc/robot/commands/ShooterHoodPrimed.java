package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.shooter.Shooter;


public class ShooterHoodPrimed extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Shooter shooter = Shooter.getInstance();

    public ShooterHoodPrimed() {
        addRequirements(this.hood, this.shooter);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return hood.atGoal() && shooter.isAtSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
    }
}