package frc.robot.util.controller;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

public class BetterXboxController extends XboxController {
    private final Hand hand;
    public final Buttons Buttons;

    public BetterXboxController(int port) {
        super(port);
        this.hand = null;
        Buttons = new Buttons(this);
    }
    public BetterXboxController(int port, Hand hand) {
        super(port);
        this.hand = hand;
        Buttons = new Buttons(this);
    }

    public Hand getHand() {
        return hand;
    }

    public double getDriveX() {
        return hand == Hand.LEFT ? super.getRightX() : super.getLeftX();
    }

    public double getDriveY() {
        return hand == Hand.LEFT ? super.getLeftY() : super.getRightY();
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