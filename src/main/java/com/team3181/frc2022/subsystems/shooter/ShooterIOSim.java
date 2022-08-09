// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team3181.frc2022.subsystems.shooter;

import com.team3181.lib.io2022.ShooterIO;

public class ShooterIOSim implements ShooterIO {
    public ShooterIOSim() {}

    @Override
    public void updateInputs(ShooterIOInputs inputs) {
        inputs.positionRot = 0;
        inputs.velocityRotPerMin = 10000;
        inputs.appliedVolts = 0;
        inputs.currentAmps = new double[] {0};
        inputs.tempCelcius = new double[] {0};
    }
}