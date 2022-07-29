package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.indexer.Indexer;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.shooter.Shooter;


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
        shooter.updateSetpoint(0, false);
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