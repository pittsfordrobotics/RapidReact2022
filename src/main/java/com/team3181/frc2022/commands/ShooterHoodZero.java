package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.hood.Hood;
import com.team3181.frc2022.subsystems.indexer.Indexer;
import com.team3181.frc2022.subsystems.shooter.Shooter;
import com.team3181.frc2022.subsystems.vision.Vision;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class ShooterHoodZero extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final Indexer indexer = Indexer.getInstance();
    private final Vision vision = Vision.getInstance();

    public ShooterHoodZero() {
        addRequirements(this.hood, this.shooter, this.indexer, this.vision);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        hood.setAngle(0, false);
        shooter.setSetpoint(0, false);
        indexer.setStateStopShoot();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}