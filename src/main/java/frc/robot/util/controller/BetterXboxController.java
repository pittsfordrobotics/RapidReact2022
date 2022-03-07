package frc.robot.util.controller;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import java.util.HashMap;

public class BetterXboxController extends XboxController {
    private final Hand hand;
    public final BetterJoystickButton A;
    public final BetterJoystickButton B;
    public final BetterJoystickButton X;
    public final BetterJoystickButton Y;
    public final BetterJoystickButton LB;
    public final BetterJoystickButton RB;
    public final POVButton DUp;
    public final POVButton DRight;
    public final POVButton DDown;
    public final POVButton DLeft;
    public final TriggerButton LT;
    public final TriggerButton RT;
    public final BetterJoystickButton Start;
    public final BetterJoystickButton Back;

    private static final HashMap<Humans, BetterXboxController> controllers = new HashMap<>();

    public enum Hand {
        RIGHT, LEFT
    }

    public enum Humans {
        DRIVER, OPERATOR
    }

    public BetterXboxController(int port, Hand hand, Humans humans) {
        super(port);
        this.hand = hand;
        A = new BetterJoystickButton(this, XboxController.Button.kA.value);
        B = new BetterJoystickButton(this, XboxController.Button.kB.value);
        X = new BetterJoystickButton(this, XboxController.Button.kX.value);
        Y = new BetterJoystickButton(this, XboxController.Button.kY.value);
        LB = new BetterJoystickButton(this, XboxController.Button.kLeftBumper.value);
        RB = new BetterJoystickButton(this, XboxController.Button.kRightBumper.value);
        DUp = new POVButton(this, 0);
        DRight = new POVButton(this, 90);
        DDown = new POVButton(this, 180);
        DLeft = new POVButton(this, 270);
        LT = new TriggerButton(this, Hand.LEFT);
        RT = new TriggerButton(this, Hand.RIGHT);
        Back = new BetterJoystickButton(this, XboxController.Button.kBack.value);
        Start = new BetterJoystickButton(this, XboxController.Button.kStart.value);
        controllers.put(humans, this);
    }

    public BetterXboxController(int port, Humans humans) {
        this(port, null, humans);
    }

    public static BetterXboxController getController(Humans humans) {
        return controllers.get(humans);
    }

    public double getBetterLeftY() {
        return -getLeftY();
    }

    public double getBetterRightY() {
        return -getRightY();
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

    public void rumbleOn() {
        setRumble(RumbleType.kLeftRumble, 0.2);
        setRumble(RumbleType.kRightRumble, 0.2);
    }

    public void rumbleOff() {
        setRumble(RumbleType.kLeftRumble, 0);
        setRumble(RumbleType.kRightRumble, 0);
    }
}