package frc.robot.util.controller;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.util.controller.BetterXboxController.Hand;

public class TriggerButton extends Trigger {
    private final XboxController controller;
    private final Hand hand;

    public TriggerButton(XboxController controller, Hand hand) {
        this.controller = controller;
        this.hand = hand;
    }

    @Override
    public boolean get() {
        return hand == Hand.LEFT ? controller.getLeftTriggerAxis() >= 0.5 : controller.getRightTriggerAxis() >= 0.5;
    }
}