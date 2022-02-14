// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.DriveXbox;
import frc.robot.subsystems.Drive;
import frc.robot.util.controller.BetterXboxController;
import frc.robot.util.controller.BetterXboxController.Humans;
import frc.robot.util.controller.BetterXboxController.Hand;

public class RobotContainer {
  private final Drive drive = Drive.getInstance();

  private final BetterXboxController driverController = new BetterXboxController(0, Hand.RIGHT, Humans.DRIVER);
  private final BetterXboxController operatorController = new BetterXboxController(1, Humans.OPERATOR);

  public RobotContainer() {
    configureButtonBindings();

    drive.setDefaultCommand(new DriveXbox());

    SmartDashboard.putString("Driver Mode", driverController.getHand() == Hand.LEFT ? "Left Handed" : "Right Handed");
  }

  private void configureButtonBindings() {
    driverController.Buttons.DUp.whenPressed(() -> drive.setThrottle(1));
    driverController.Buttons.DLeft.whenPressed(() -> drive.setThrottle(0.7));
    driverController.Buttons.DRight.whenPressed(() -> drive.setThrottle(0.4));
    driverController.Buttons.DDown.whenPressed(() -> drive.setThrottle(0.1));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}