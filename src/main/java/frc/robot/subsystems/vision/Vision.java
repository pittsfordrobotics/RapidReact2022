package frc.robot.subsystems.vision;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.FieldConstants;
import frc.robot.subsystems.vision.VisionIO.CameraMode;
import frc.robot.subsystems.vision.VisionIO.LED;
import frc.robot.subsystems.vision.VisionIO.Pipelines;
import frc.robot.subsystems.vision.VisionIO.VisionIOInputs;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import org.littletonrobotics.junction.Logger;

public class Vision extends SubsystemBase {
    private final VisionIO io;
    private final VisionIOInputs inputs = new VisionIOInputs();

    private Pipelines pipeline = Pipelines.COMPETITION;
    private LED led = LED.OFF;
    private CameraMode camera = CameraMode.VISION_PROCESSING;

    private final ShuffleboardTab visionTab = Shuffleboard.getTab("Vision");

    private final Alert limelightAlert = new Alert("Limelight not detected! Vision will NOT work!", AlertType.ERROR);

    private static final Vision INSTANCE = new Vision(new VisionIOLimelight());
    public static Vision getInstance() {
        return INSTANCE;
    }

    private Vision(VisionIO io) {
        this.io = io;
        visionTab.addBoolean("Has Target", this::hasTarget);
        visionTab.addNumber("Distance", this::getDistance);
        visionTab.addNumber("Horizontal", this::getHorizontal);
        visionTab.addNumber("Vertical", this::getVertical);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Vision", inputs);
        if (DriverStation.isDisabled()) {
            led = LED.OFF;
            camera = CameraMode.DRIVER_CAMERA;
        }
        else {
            led = LED.ON;
            camera = CameraMode.VISION_PROCESSING;
        }
        io.setLEDS(led);
        io.setPipeline(pipeline);
        io.setCameraModes(camera);
        limelightAlert.set(!inputs.connected);
    }

    public boolean hasTarget() {
        return inputs.hasTarget;
    }

    public double getHorizontal() {
        return inputs.hAngle;
    }

    public double getVertical() {
        return inputs.vAngle;
    }

    public void setPipeline(Pipelines pipeline) {
        this.pipeline = pipeline;
    }

    public void setLED(LED led) {
        this.led = led;
    }

    public void setCamMode(CameraMode camera) {
        this.camera = camera;
    }

    public double getDistance() {
        return (FieldConstants.visionTargetHeighCenter - Constants.LIMELIGHT_MOUNTING_HEIGHT) / Math.tan(Constants.LIMELIGHT_ANGLE + getVertical());
    }
}