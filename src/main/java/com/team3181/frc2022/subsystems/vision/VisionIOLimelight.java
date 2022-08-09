package com.team3181.frc2022.subsystems.vision;

import com.team3181.lib.io2022.VisionIO;
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
                led = ledEntry.getDouble(0.0);
                pipeline = pipelineEntry.getDouble(0.0);
                camera = cameraEntry.getDouble(0.0);
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
    public void setLEDs(LED led) {
        ledEntry.forceSetDouble(led.getNum());
        this.led = led.getNum();
    }
}