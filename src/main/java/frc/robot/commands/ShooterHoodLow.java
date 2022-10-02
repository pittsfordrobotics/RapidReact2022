package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.shooter.Shooter;


public class ShooterHoodLow extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Hood hood = Hood.getInstance();

    public ShooterHoodLow() {
        addRequirements(this.shooter, this.hood);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setSetpoint(Constants.SHOOTER_LOW_SPEED, false);
        hood.setAngle(Constants.HOOD_ANGLE_MAX_RAD, false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}