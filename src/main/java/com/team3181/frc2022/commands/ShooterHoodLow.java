package com.team3181.frc2022.commands;

import com.team3181.frc2022.Constants;
import com.team3181.frc2022.subsystems.hood.Hood;
import com.team3181.frc2022.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;


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
        hood.setAngle(Constants.HOOD_ANGLE_MAX, false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}