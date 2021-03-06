package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;


public class ShooterZero extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Indexer indexer = Indexer.getInstance();
    private final Limelight limelight = Limelight.getInstance();

    public ShooterZero() {
        addRequirements(this.shooter, this.indexer, this.limelight);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.shootStop();
        indexer.setStateStopShoot();
        limelight.disable();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}