package frc.robot.subsystems.vision;

public class VisionIOSim implements VisionIO {
    public VisionIOSim() {}

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        inputs.led = 0;
        inputs.pipeline = 0;
        inputs.camera = 0;
        inputs.hasTarget = false;
        inputs.connected = true;
        inputs.vAngle = 0;
        inputs.hAngle = 0;
    }
}