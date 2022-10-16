package frc.robot.subsystems.vision;

public class VisionIOSim implements VisionIO {
    public VisionIOSim() {}

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        inputs.hasTarget = false;
        inputs.connected = true;
        inputs.vAngle = 0;
        inputs.hAngle = 0;
    }
}