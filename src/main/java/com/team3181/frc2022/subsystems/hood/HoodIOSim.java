package com.team3181.frc2022.subsystems.hood;

import com.team3181.lib.io2022.HoodIO;

public class HoodIOSim implements HoodIO {
    public HoodIOSim() {
    }

    @Override
    public void updateInputs(HoodIOInputs inputs) {
        inputs.absolutePositionRad = 0;
        inputs.positionRad = 0;
        inputs.velocityRadPerSec = 0;
        inputs.appliedVolts = 0;
        inputs.currentAmps = new double[] {0};
        inputs.tempCelcius = new double[] {0};
    }
}