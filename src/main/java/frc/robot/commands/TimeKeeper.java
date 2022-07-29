package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class TimeKeeper extends CommandBase {
    private static final Timer timer = new Timer();
    private final boolean start;

    /** @param start true for start; false for end */
    public TimeKeeper(boolean start) {
        this.start = start;
    }

    @Override
    public void initialize() {
        if (start) {
            timer.start();
            timer.reset();
        }
        else {
            System.out.println(timer.get());
        }
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}