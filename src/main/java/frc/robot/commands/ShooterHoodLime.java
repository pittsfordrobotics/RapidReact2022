package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotState;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Vision;


public class ShooterHoodLime extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final Vision vision = Vision.getInstance();

    public ShooterHoodLime() {
        addRequirements(this.hood, this.vision, this.shooter);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
//        TODO: Change this
        if (vision.hasTarget()) {
            shooter.setSetpoint(Constants.SHOOTER_SPEED_MAP.lookup(vision.getDistance()), false);
            hood.setAngle(Constants.HOOD_ANGLE_MAP.lookup(vision.getDistance()), false);
        }
        else {
            shooter.setSetpoint(Constants.SHOOTER_SPEED_MAP.lookup(RobotState.getInstance().getDistanceToHub()), false);
            hood.setAngle(Constants.HOOD_ANGLE_MAP.lookup(RobotState.getInstance().getDistanceToHub()), false);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}