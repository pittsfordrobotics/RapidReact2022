package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;


public class HoodPrimed extends CommandBase {
    private final Hood hood = Hood.getInstance();

    public HoodPrimed() {
        addRequirements(this.hood);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return hood.isAtPosition();
    }

    @Override
    public void end(boolean interrupted) {

    }
}