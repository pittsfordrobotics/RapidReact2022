package frc.robot.util.controller;

import edu.wpi.first.wpilibj.XboxController;

public class BetterXboxController extends XboxController {
    private final Hand hand;

    public BetterXboxController(int port, Hand hand) {
        super(port);
        this.hand = hand;
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
}