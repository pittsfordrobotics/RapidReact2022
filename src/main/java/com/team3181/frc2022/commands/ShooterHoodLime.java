package com.team3181.frc2022.commands;

import com.team3181.frc2022.Constants;
import com.team3181.frc2022.RobotState;
import com.team3181.frc2022.subsystems.hood.Hood;
import com.team3181.frc2022.subsystems.shooter.Shooter;
import com.team3181.frc2022.subsystems.vision.Vision;
import edu.wpi.first.wpilibj2.command.CommandBase;


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