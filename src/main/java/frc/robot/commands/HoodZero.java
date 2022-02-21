package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;


public class HoodZero extends CommandBase {
    private final Hood hood = Hood.getInstance();

    public HoodZero() {
        addRequirements(this.hood);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        hood.zero();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}