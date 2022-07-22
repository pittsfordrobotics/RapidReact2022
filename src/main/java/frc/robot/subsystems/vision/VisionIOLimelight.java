package frc.robot.subsystems.vision;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class VisionIOLimelight implements VisionIO {
    private double led = 0.0;
    private double pipeline = 0.0;
    private double camera = 0.0;
    private boolean hasTarget = false;
    private boolean connected = false;
    private double vAngle = 0.0;
    private double hAngle = 0.0;

    private final NetworkTableEntry ledEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode");
    private final NetworkTableEntry pipelineEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline");
    private final NetworkTableEntry cameraEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode");
    private final NetworkTableEntry validEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv");
    private final NetworkTableEntry latencyEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("tl");
    private final NetworkTableEntry hAngleEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx");
    private final NetworkTableEntry vAngleEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty");

    public VisionIOLimelight() {
        led = ledEntry.getDouble(0.0);
        pipeline = pipelineEntry.getDouble(0.0);
        camera = cameraEntry.getDouble(0.0);
        hasTarget = validEntry.getDouble(0.0) == 1.0;
        connected = latencyEntry.getDouble(0.0) != 0.0;
        vAngle = vAngleEntry.getDouble(0.0);
        hAngle = hAngleEntry.getDouble(0.0);
    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        inputs.led = led;
        inputs.pipeline = pipeline;
        inputs.camera = camera;
        inputs.hasTarget = hasTarget;
        inputs.connected = connected;
        inputs.vAngle = vAngle;
        inputs.hAngle = hAngle;
    }

    @Override
    public void setPipeline(Pipelines pipeline) {
        pipelineEntry.forceSetDouble(pipeline.getNum());
        this.pipeline = pipeline.getNum();
    }

    @Override
    public void setCameraModes(CameraMode camera) {
        cameraEntry.forceSetDouble(camera.getNum());
        this.camera = camera.getNum();
    }

    @Override
    public void setLEDS(LED led) {
        ledEntry.forceSetDouble(led.getNum());
        this.led = led.getNum();
    }
}