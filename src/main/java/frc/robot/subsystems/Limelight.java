package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

public class Limelight extends SubsystemBase {
    private final NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

    private final ShuffleboardTab limelightTab = Shuffleboard.getTab("Limelight");

    public enum Pipelines {
        PRACTICE(0), COMPETITION(1);

        private final int num;
        Pipelines(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    public enum LED {
        PIPELINE(0), OFF(1), BLINK(2), ON(3);

        private final int mode;

        LED(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }
    }

    public enum CameraMode {
        VISION_PROCESSING(0), DRIVER_CAMERA(1);

        private final int num;

        CameraMode(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    private final Alert limelightAlert = new Alert("Limelight not detected! Vision will NOT work!", AlertType.ERROR);

    private static final Limelight INSTANCE = new Limelight();
    public static Limelight getInstance() {
        return INSTANCE;
    }

    private Limelight() {
        limelightTab.addBoolean("Has Target", this::hasTarget);
        limelightTab.addNumber("Distance", this::getDistance);
        limelightTab.addNumber("Horizontal", this::getHorizontal);
        limelightTab.addNumber("Vertical", this::getVertical);
        setLED(LED.OFF);
        setCamMode(CameraMode.DRIVER_CAMERA);
    }

    @Override
    public void periodic() {
        limelightAlert.set(limelight.getEntry("tl").getDouble(0) == 0);
    }

    public boolean hasTarget() {
        return limelight.getEntry("tv").getDouble(0.0) == 1.0;
    }

    public double getHorizontal() {
        return limelight.getEntry("tx").getDouble(0.0);
    }

    public double getVertical() {
        return limelight.getEntry("ty").getDouble(0.0);
    }

    public double getArea() {
        return limelight.getEntry("ta").getDouble(0.0);
    }

    public void setPipeline(Pipelines pipeline) {
        limelight.getEntry("pipeline").setNumber(pipeline.getNum());
    }

    public void setLED(@NotNull LED mode) {
        limelight.getEntry("ledMode").setNumber(mode.getMode());
    }

    public void setCamMode(CameraMode mode) {
        limelight.getEntry("camMode").setNumber(mode.getNum());
    }

    public void enable() {
        setPipeline(Constants.LIMELIGHT_PIPELINE);
        setCamMode(CameraMode.VISION_PROCESSING);
        setLED(LED.ON);
    }

    public void disable() {
        setCamMode(CameraMode.DRIVER_CAMERA);
        setLED(LED.OFF);
    }

    public double getDistance() {
        return (Constants.LIMELIGHT_TARGET_HEIGHT_INCHES - Constants.LIMELIGHT_MOUNTING_HEIGHT_INCHES) / Math.tan(Constants.LIMELIGHT_ANGLE + getVertical());
    }
}