// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.AutoPathing;
import frc.robot.commands.DriveXbox;
import frc.robot.subsystems.Drive;
import frc.robot.util.controller.BetterXboxController;
import frc.robot.util.controller.BetterXboxController.Hand;

public class RobotContainer {
  public static final BetterXboxController driverController = new BetterXboxController(0, Hand.RIGHT);
  public static final BetterXboxController operatorController = new BetterXboxController(1);

  private final SendableChooser<Command> commandChooser = new SendableChooser<Command>();

  public RobotContainer() {
    commandChooser.setDefaultOption("Path Planner Test", new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_TEST));
    SmartDashboard.putData("Auto Command", commandChooser);

    Drive.getInstance().setDefaultCommand(new DriveXbox());

    SmartDashboard.putString("Driver Mode", driverController.getHand() == Hand.LEFT ? "Left Handed" : "Right Handed");

    configureButtonBindings();
  }

  private void configureButtonBindings() {
  }

  public Command getAutonomousCommand() {
    return commandChooser.getSelected();
  }
}