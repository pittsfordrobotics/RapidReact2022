package frc.robot.subsystems.hood;

public class HoodIOSim implements HoodIO {
    public HoodIOSim() {
    }

    @Override
    public void updateInputs(HoodIOInputs inputs) {
        inputs.absolutePosition = 0;
        inputs.positionRad = 0;
        inputs.velocityRadPerSec = 0;
        inputs.appliedVolts = 0;
        inputs.currentAmps = new double[] {0};
        inputs.tempCelcius = new double[] {0};
    }
}