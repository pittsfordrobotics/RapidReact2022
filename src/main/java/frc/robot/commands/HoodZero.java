package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;


/**
 * Zeroes the {@link Hood} by lowering it until it hits the limit switch. Then it sets the hood angle to 0 degrees.
 */
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
        hood.setVoltage(-5, true);
    }

    @Override
    public boolean isFinished() {
        return hood.getLimit();
    }

    /**
     * called when the command ends
     * @param interrupted whether the command was interrupted/canceled
     */
    @Override
    public void end(boolean interrupted) {
        // stop motor
        hood.setVoltage(0, true);
        // if it was interrupted we don't know if we should zero
        if (!interrupted) {
            hood.resetCounter();
            hood.zeroed();
        }
    }
}