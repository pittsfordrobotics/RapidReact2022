package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

// TODO: someone do this i no want to
public class HoodZero extends CommandBase {
    private final Hood hood = Hood.getInstance();
    public final Timer timer = new Timer();

    public HoodZero() {
        addRequirements(this.hood);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
    }

    @Override
    public void execute() {
        hood.setVoltage(-1);
    }

    @Override
    public boolean isFinished() {
//        if
//        timer.hasElapsed()
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        hood.resetCounter();
        timer.reset();
    }
}