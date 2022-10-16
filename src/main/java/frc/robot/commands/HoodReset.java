package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

// TODO: someone do this i no want to
public class HoodReset extends CommandBase {
    private final Hood hood = Hood.getInstance();
    public final Timer timer = new Timer();

    public HoodReset() {
        addRequirements(this.hood);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
    }

    @Override
    public void execute() {
        hood.setVoltage(-2, true);
    }

    @Override
    public boolean isFinished() {
         if (timer.hasElapsed(1)) {
             timer.reset();
             return Math.abs(hood.getAbsoluteVelocity()) < 0.3 && hood.getAbsolutePosition() > 0.73;
         }
         return false;
    }

    @Override
    public void end(boolean interrupted) {
        hood.resetCounter();
        hood.setVoltage(0, true);
        timer.reset();
    }
}