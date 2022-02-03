// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.DriveXbox;
import frc.robot.subsystems.Drive;
import frc.robot.util.controller.BetterXboxController;
import frc.robot.util.controller.ControllerButtons;
import frc.robot.util.controller.Hand;

public class RobotContainer {
  //  Subsystems
  public final static Drive drive = Drive.getInstance();

  //  Controllers
  public final static BetterXboxController driverController = new BetterXboxController(0, Hand.RIGHT);
  public final static XboxController operatorController = new XboxController(1);

  public final static ControllerButtons driverButtons = new ControllerButtons(driverController);
  public final static ControllerButtons operatorButtons = new ControllerButtons(operatorController);

  public RobotContainer() {
    configureButtonBindings();

    drive.setDefaultCommand(new DriveXbox());

    SmartDashboard.putString("Driver Mode", driverController.getHand() == Hand.LEFT ? "Left Handed" : "Right Handed");
  }

  private void configureButtonBindings() {
  }

  public Command getAutonomousCommand() {
    return null;
  }
}