package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.hood.Hood;
import com.team3181.frc2022.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;


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
        return hood.atAngle() && shooter.isAtSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
    }
}