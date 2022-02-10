package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.Limelight.*;

public class Limelight extends SubsystemBase {
    private final NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

    public enum Pipelines {
        ;

        private int num;
        Pipelines(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    public enum LED {
        PIPELINE(0), OFF(1), BLINK(2), ON(3);

        private int mode;

        LED(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }
    }

    public enum CameraMode {
        VISION_PROCESSING(0), DRIVER_CAMERA(1);

        private int num;

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
    }

    public void periodic() {
        SmartDashboard.putNumber("Limelight Horizontal", getHorizontal());
        SmartDashboard.putNumber("Limelight Vertical", getVertical());
        SmartDashboard.putNumber("Limelight Area", getArea());
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

    public void setLED(LED mode) {
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
        return (LIMELIGHT_TARGET_HEIGHT - LIMELIGHT_MOUNTING_HEIGHT) / Math.tan(LIMELIGHT_ANGLE + getVertical());
    }
}