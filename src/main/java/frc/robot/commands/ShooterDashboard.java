package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;


public class ShooterDashboard extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();

    public ShooterDashboard() {
        addRequirements(this.shooter);
        SmartDashboard.putNumber("shooter speed", 0);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setSpeed(SmartDashboard.getNumber("shooter speed", 0));
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}