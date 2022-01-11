package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.*;

public class Limelight extends SubsystemBase {

    // With eager singleton initialization, any static variables/fields used in the
    // constructor must appear before the "INSTANCE" variable so that they are initialized
    // before the constructor is called when the "INSTANCE" variable initializes.

    /**
     * The Singleton instance of this Limelight. Code should use
     * the {@link #getInstance()} method to get the single instance (rather
     * than trying to construct an instance of this class.)
     */
    private final static Limelight limelight = new Limelight();


    /**
     * Returns the Singleton instance of this Limelight. This static method
     * should be used, rather than the constructor, to get the single instance
     * of this class. For example: {@code Limelight.getInstance();}
     */
    @SuppressWarnings("WeakerAccess")
    public static Limelight getInstance() {
        return limelight;
    }

    /**
     * Creates a new instance of this Limelight. This constructor
     * is private since this class is a Singleton. Code should use
     * the {@link #getInstance()} method to get the singleton instance.
     */
    private Limelight() {
        setLED(1);
        setCamMode(0);
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

    /**
     * Sets the pipeline of the Limelight.
     *
     * @param pipeline :
     */
    public void setPipeline(int pipeline) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(pipeline);
    }

    /**
     * Sets the LED mode of the Limelight.
     *
     * @param mode 0: pipeline mode, 1: off, 2: blink, 3: on
     */
    public void setLED(int mode) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(mode);
    }

    /**
     * Sets camera mode
     *
     * @param mode 0: Vision Processor, 1: Driver Camera
     */
    public void setCamMode(int mode) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(mode);
    }

    public void enable() {
        setCamMode(0);
        setLED(3);
    }

    public void disable() {
        setCamMode(1);
        setLED(1);
    }

    public double getDistance() {
        return (kLimelightTargetHeight - kLimelightRobotHeight) / Math.tan(kLimelightAngle + getVertical());
    }

}