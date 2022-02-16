package frc.robot.util.controller;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import java.util.HashMap;

public class BetterXboxController extends XboxController {
    private final Hand hand;
    public final Buttons Buttons;

    private static HashMap<Humans, BetterXboxController> controllers = new HashMap<>();

    public enum Hand {
        RIGHT, LEFT
    }

    public enum Humans {
        DRIVER, OPERATOR
    }

    public BetterXboxController(int port, Hand hand, Humans humans) {
        super(port);
        this.hand = hand;
        Buttons = new Buttons(this);
        controllers.put(humans, this);
    }

    public BetterXboxController(int port, Humans humans) {
        this(port, null, humans);
    }

    public static BetterXboxController getController(Humans humans) {
        return controllers.get(humans);
    }

    public Hand getHand() {
        return hand;
    }

    public double getDriveX() {
        if (hand == null) {
            return 0;
        }
        switch (hand) {
            case LEFT:
                return getRightX();
            case RIGHT:
                return getLeftX();
            default:
                return 0;
        }
    }

    public double getDriveY() {
        if (hand == null) {
            return 0;
        }
        switch (hand) {
            case LEFT:
                return getLeftY();
            case RIGHT:
                return getRightY();
            default:
                return 0;
        }
    }

    public static class Buttons {
        public final JoystickButton A;
        public final JoystickButton B;
        public final JoystickButton X;
        public final JoystickButton Y;
        public final JoystickButton LB;
        public final JoystickButton RB;
        public final POVButton DUp;
        public final POVButton DRight;
        public final POVButton DDown;
        public final POVButton DLeft;
        public final TriggerButton LT;
        public final TriggerButton RT;
        public final JoystickButton Start;
        public final JoystickButton Back;

        private Buttons(XboxController controller) {
            A = new JoystickButton(controller, XboxController.Button.kA.value);
            B = new JoystickButton(controller, XboxController.Button.kB.value);
            X = new JoystickButton(controller, XboxController.Button.kX.value);
            Y = new JoystickButton(controller, XboxController.Button.kY.value);
            LB = new JoystickButton(controller, XboxController.Button.kLeftBumper.value);
            RB = new JoystickButton(controller, XboxController.Button.kRightBumper.value);
            DUp = new POVButton(controller, 0);
            DRight = new POVButton(controller, 90);
            DDown = new POVButton(controller, 180);
            DLeft = new POVButton(controller, 270);
            LT = new TriggerButton(controller, Hand.LEFT);
            RT = new TriggerButton(controller, Hand.RIGHT);
            Start = new JoystickButton(controller, XboxController.Button.kStart.value);
            Back = new JoystickButton(controller, XboxController.Button.kBack.value);
        }
    }
}