package frc.robot.util.controller;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class BetterJoystickButton extends JoystickButton {
    /**
     * Creates a joystick button for triggering commands.
     *
     * @param joystick     The GenericHID object that has the button (e.g. Joystick, KinectStick, etc)
     * @param buttonNumber The button number (see {@link GenericHID#getRawButton(int) }
     */
    public BetterJoystickButton(GenericHID joystick, int buttonNumber) {
        super(joystick, buttonNumber);
    }

    /**
     * Cancels the current command before rescheduling
     */
    public Trigger whenActiveCancel(final Command command) {
        CommandScheduler.getInstance().cancel(command);
        return whenActive(command, true);
    }
}