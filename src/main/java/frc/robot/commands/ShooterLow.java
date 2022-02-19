package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;


public class ShooterLow extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final Indexer indexer = Indexer.getInstance();

    public ShooterLow() {
        addRequirements(this.hood, this.shooter, this.indexer);
    }

    @Override
    public void initialize() {
        hood.setPosition(Constants.HOOD_POSITION_LOW);
        shooter.setShooterSpeed(Constants.SHOOTER_LOW_SPEED);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return indexer.isEmpty();
    }

    @Override
    public void end(boolean interrupted) {
    }
}