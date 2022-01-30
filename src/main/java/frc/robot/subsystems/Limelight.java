package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Limelight extends SubsystemBase {
    private static Limelight INSTANCE = new Limelight();

    public static Limelight getInstance() {
        return INSTANCE;
    }

    private Limelight() {
        setLED(LED.OFF);
        setCamMode(Camera.DRIVER);
    }

    public void periodic() {
        SmartDashboard.putNumber("Limelight Horizontal", getHorizontal());
        SmartDashboard.putNumber("Limelight Vertical", getVertical());
        SmartDashboard.putNumber("Limelight Area", getArea());
    }

    public boolean hasTarget() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0.0) == 1.0;
    }

    public double getHorizontal() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0.0);
    }

    public double getVertical() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0.0);
    }

    public double getArea() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0.0);
    }


    public enum Pipelines {
        TEMP(0);

        private int num;
        Pipelines(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    public void setPipeline(Pipelines pipeline) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(pipeline.getNum());
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

    public void setLED(LED mode) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(mode.getMode());
    }


    public enum Camera {
        VISION(0), DRIVER(1);

        private int num;

        Camera(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    public void setCamMode(Camera mode) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(mode.getNum());
    }


    public void enable() {
        setCamMode(Camera.VISION);
        setLED(LED.ON);
    }

    public void disable() {
        setCamMode(Camera.DRIVER);
        setLED(LED.OFF);
    }

    public double getDistance() {
        return (LIMELIGHT_TARGET_HEIGHT - LIMELIGHT_ROBOT_HEIGHT) / Math.tan(LIMELIGHT_ANGLE + getVertical());
    }

}