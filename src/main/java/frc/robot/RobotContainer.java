// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.DriveXbox;
import frc.robot.subsystems.Drive;
import frc.robot.util.BetterXboxController;

public class RobotContainer {
  private final BetterXboxController driverController = new BetterXboxController(0, false);
  private final XboxController operatorController = new XboxController(1);
  private final Drive drive = Drive.getInstance();

  public RobotContainer() {
    configureButtonBindings();

    drive.setDefaultCommand(new DriveXbox(driverController));

    SmartDashboard.putString("Driver Mode", driverController.getIsLefty() ? "Left Handed" : "Right Handed");
  }

  private void configureButtonBindings() {}

  public Command getAutonomousCommand() {
    return null;
  }
}