package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.util.controller.BetterXboxController;
import frc.robot.util.controller.BetterXboxController.Humans;


public class ControllerRumble extends CommandBase {
    private final Timer timer = new Timer();
    private final double time;
    private final boolean[] controllers;

    public ControllerRumble(double time, boolean[] controllers) {
        this.time = time;
        this.controllers = controllers;
        addRequirements();
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
        if (controllers[0]) {
            BetterXboxController.getController(Humans.DRIVER).setRumble(0.1);
        }
        if (controllers[1]) {
            BetterXboxController.getController(Humans.OPERATOR).setRumble(0.1);
        }
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(time);
    }

    @Override
    public void end(boolean interrupted) {
        BetterXboxController.getController(Humans.DRIVER).setRumble(0);
        BetterXboxController.getController(Humans.OPERATOR).setRumble(0);
    }
}