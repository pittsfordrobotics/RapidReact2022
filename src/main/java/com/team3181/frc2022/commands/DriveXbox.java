/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team3181.frc2022.commands;

import com.team3181.frc2022.subsystems.drive.Drive;
import edu.wpi.first.wpilibj2.command.CommandBase;

import com.team3181.lib.controller.BetterXboxController;
import com.team3181.lib.controller.BetterXboxController.Humans;

public class DriveXbox extends CommandBase {
    private final Drive drive = Drive.getInstance();
    private final BetterXboxController driverController = BetterXboxController.getController(Humans.DRIVER);

    public DriveXbox() {
        addRequirements(drive);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        drive.driveCurve(-driverController.getDriveY(), driverController.getDriveX());
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}