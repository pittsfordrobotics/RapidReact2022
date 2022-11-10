package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

/**
 * Zeroes the {@link Hood} by lowering it until it hits the limit switch. Then setting angle to 0.
 */
public class ZeroHood extends CommandBase {
    private final Hood hood = Hood.getInstance();
    public ZeroHood() {
        addRequirements(this.hood);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        hood.setVoltage(-2, true);
    }

    @Override
    public boolean isFinished() {
        if (hood.getLimit()) {
            hood.setVoltage(0, true);
            hood.setAngle(0, false);
            return true;
        }
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        hood.setVoltage(0, true);
    }
    
}
