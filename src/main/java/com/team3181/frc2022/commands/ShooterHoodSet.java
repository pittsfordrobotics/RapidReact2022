package com.team3181.frc2022.commands;

import com.team3181.frc2022.RobotState;
import com.team3181.frc2022.subsystems.hood.Hood;
import com.team3181.frc2022.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ShooterHoodSet extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final double speed;
    private final double angle;

    public ShooterHoodSet(double speed, double angle) {
        this.speed = speed;
        this.angle = angle;
        addRequirements(this.hood, this.shooter);
    }

    @Override
    public void initialize() {
        RobotState.getInstance().setSnapped(true);
        shooter.setSetpoint(speed, false);
        hood.setAngle(angle, false);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}