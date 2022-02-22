package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.random.Constants;
import frc.robot.subsystems.Hood;


public class HoodLow extends CommandBase {
    private final Hood hood = Hood.getInstance();

    public HoodLow() {
        addRequirements(this.hood);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        hood.setPosition(Constants.HOOD_POSITION_LOW);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}