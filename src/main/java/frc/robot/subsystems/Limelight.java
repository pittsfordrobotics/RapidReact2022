package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.Limelight.*;

public class Limelight extends SubsystemBase {
    private static Limelight INSTANCE = new Limelight();

    public static Limelight getInstance() {
        return INSTANCE;
    }

    private Limelight() {
        setLED(LED.OFF);
        setCamMode(Camera.DRIVER);
        LimelightManager limelightManager = new LimelightManager();
    }

    private static class LimelightManager {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        //NetworkTable key names can be found at https://docs.limelightvision.io/en/latest/networktables_api.html
        //Outputs from Limelight:
        NetworkTableEntry tv = table.getEntry("tv");
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");
        NetworkTableEntry ts = table.getEntry("ts");
        NetworkTableEntry tl = table.getEntry("tl");
        //NetworkTableEntry tshort = table.getEntry("tshort");
        //NetworkTableEntry tlong = table.getEntry("tlong");
        NetworkTableEntry thor = table.getEntry("thor");
        NetworkTableEntry tvert = table.getEntry("tvert");
        NetworkTableEntry getpipe = table.getEntry("getpipe");
        //NetworkTableEntry camtran = table.getEntry("camtran"); //6DOF 3D position solution (translation + YPR)

        //Inputs to Limelight:
        NetworkTableEntry ledMode = table.getEntry("ledMode");
        NetworkTableEntry camMode = table.getEntry("camMode");
        NetworkTableEntry pipeline = table.getEntry("pipeline");
        //NetworkTableEntry stream = table.getEntry("stream");
        //NetworkTableEntry snapshot = table.getEntry("snapshot");

    }
    LimelightManager limelightManager = new LimelightManager();

    public boolean hasTarget() {
        return limelightManager.tv.getDouble(0.0) == 1.0;
    }

    public double getHorizontal() {
        return limelightManager.tx.getDouble(0.0);
    }

    public double getVertical() {
        return limelightManager.ty.getDouble(0.0);
    }

    public double getArea() {
        return limelightManager.ta.getDouble(0.0);
    }

    public enum Pipelines {
        ; //the cursed lone semicolon

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
        VISION(0), DRIVER(1); //michael please fix your atrocious naming schemes thanks

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


    public void enableLimelight() {
        setCamMode(Camera.VISION);
        setLED(LED.ON);
    }

    public void disableLimelight() {
        setCamMode(Camera.DRIVER);
        setLED(LED.OFF);
    }

    public double getDistance() {
        return (LIMELIGHT_TARGET_HEIGHT - LIMELIGHT_MOUNTING_HEIGHT) / Math.tan(LIMELIGHT_ANGLE + getVertical());
    }

    public void periodic() {
        SmartDashboard.putNumber("Limelight Horizontal", getHorizontal());
        SmartDashboard.putNumber("Limelight Vertical", getVertical());
        SmartDashboard.putNumber("Limelight Area", getArea());
    }
}