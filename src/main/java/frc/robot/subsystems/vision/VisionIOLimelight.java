package frc.robot.subsystems.vision;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.List;

public class VisionIOLimelight implements VisionIO {
    public double captureTimestamp = 0.0;
    public double[] cornerX = new double[] {};
    public double[] cornerY = new double[] {};
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
    private final NetworkTableEntry dataEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("tcornxy");
    private final NetworkTableEntry hAngleEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx");
    private final NetworkTableEntry vAngleEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty");
    private final NetworkTableEntry heartbeatEntry =
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("hb");

    public VisionIOLimelight() {
        latencyEntry.addListener(event -> {
            double timestamp = Logger.getInstance().getRealTimestamp()
                    - (latencyEntry.getDouble(0.0) / 1000.0);

            List<Double> cornerXList = new ArrayList<>();
            List<Double> cornerYList = new ArrayList<>();
            if (validEntry.getDouble(0.0) == 1.0) {
                boolean isX = true;
                for (double coordinate : dataEntry.getDoubleArray(new double[] {})) {
                    if (isX) {
                        cornerXList.add(coordinate);
                    } else {
                        cornerYList.add(coordinate);
                    }
                    isX = !isX;
                }
            }

            synchronized (VisionIOLimelight.this) {
                captureTimestamp = timestamp;
                cornerX = cornerXList.stream().mapToDouble(Double::doubleValue).toArray();
                cornerY = cornerYList.stream().mapToDouble(Double::doubleValue).toArray();
                hasTarget = validEntry.getDouble(0.0) == 1.0;
                connected = heartbeatEntry.getDouble(0.0) > 0.0;
                vAngle = vAngleEntry.getDouble(0.0);
                hAngle = hAngleEntry.getDouble(0.0);
            }

        }, EntryListenerFlags.kUpdate);
    }

    @Override
    public synchronized void updateInputs(VisionIOInputs inputs) {
        inputs.captureTimestamp = captureTimestamp;
        inputs.cornerX = cornerX;
        inputs.cornerY = cornerY;
        inputs.hasTarget = hasTarget;
        inputs.connected = connected;
        inputs.vAngle = vAngle;
        inputs.hAngle = hAngle;
    }

    @Override
    public void setPipeline(Pipelines pipeline) {
        pipelineEntry.forceSetDouble(pipeline.getNum());
    }

    @Override
    public void setCameraModes(CameraMode camera) {
        cameraEntry.forceSetDouble(camera.getNum());
    }

    @Override
    public void setLEDs(LED led) {
        ledEntry.forceSetDouble(led.getNum());
    }
}