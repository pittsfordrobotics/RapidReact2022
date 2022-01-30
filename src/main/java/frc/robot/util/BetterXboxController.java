package frc.robot.util;

import edu.wpi.first.wpilibj.XboxController;

public class BetterXboxController extends XboxController {
    private final boolean isLefty;

    public BetterXboxController(int port, boolean isLefty) {
        super(port);
        this.isLefty = isLefty;
    }

    public boolean getIsLefty() {
        return isLefty;
    }

    public double getDriveX() {
        return isLefty ? super.getRightX() : super.getLeftX();
    }

    public double getDriveY() {
        return isLefty ? super.getLeftY() : super.getRightY();
    }
}