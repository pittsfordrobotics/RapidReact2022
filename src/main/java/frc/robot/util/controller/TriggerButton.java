package frc.robot.util.controller;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.util.controller.BetterXboxController.Hand;

public class TriggerButton extends Trigger {
    private final XboxController controller;
    private final Hand hand;
    private final double threshold;

    public TriggerButton(XboxController controller, Hand hand, double threshold) {
        this.controller = controller;
        this.hand = hand;
        this.threshold = threshold;
    }

    @Override
    public boolean get() {
        return hand == Hand.LEFT ? controller.getLeftTriggerAxis() >= threshold : controller.getRightTriggerAxis() >= threshold;
    }
}