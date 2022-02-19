package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;


public class HoodDefault extends CommandBase {
    private final Hood hood = Hood.getInstance();

    public HoodDefault() {
        addRequirements(this.hood);
    }

    @Override
    public void initialize() {
        hood.setPosition(0);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}