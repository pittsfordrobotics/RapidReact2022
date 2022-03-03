package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import org.jetbrains.annotations.NotNull;

public class Limelight extends SubsystemBase {
    private final NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

    private final ShuffleboardTab limelightTab = Shuffleboard.getTab("Limelight");

    public enum Pipelines {
        BASE(0), PRACTICE(1), COMPETITION(2);

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

    private static final Limelight INSTANCE = new Limelight();
    public static Limelight getInstance() {
        return INSTANCE;
    }

    private Limelight() {
        setLED(LED.OFF);
        setCamMode(CameraMode.DRIVER_CAMERA);

        limelightTab.add("Enable", new InstantCommand(this::enable));
        limelightTab.add("Disable", new InstantCommand(this::disable));
        limelightTab.addNumber("Distance", this::getDistance);
        limelightTab.addBoolean("Has Target", this::hasTarget);
        limelightTab.addNumber("Pipeline", this::getPipeline);
        limelightTab.addNumber("Horizontal", this::getHorizontal);
        limelightTab.addNumber("Vertical", this::getVertical);
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

    public int getPipeline() {
        return limelight.getEntry("pipeline").getNumber(0).intValue();
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
        setCamMode(CameraMode.VISION_PROCESSING);
        setLED(LED.ON);
    }

    public void disable() {
        setCamMode(CameraMode.DRIVER_CAMERA);
        setLED(LED.OFF);
    }

    public double getDistance() {
        return (Constants.LIMELIGHT_TARGET_HEIGHT_INCHES - Constants.LIMELIGHT_MOUNTING_HEIGHT) / Math.tan(Constants.LIMELIGHT_ANGLE + getVertical());
    }
}