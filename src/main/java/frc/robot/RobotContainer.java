// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.DriveXbox;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Drive;
import frc.robot.util.controller.BetterXboxController;
import frc.robot.util.controller.BetterXboxController.Hand;

public class RobotContainer {
  //  Subsystems
  private final Drive drive = Drive.getInstance();

  //  Controllers
  public static final BetterXboxController driverController = new BetterXboxController(0, Hand.RIGHT);
  public static final BetterXboxController operatorController = new BetterXboxController(1);

  public RobotContainer() {
    configureButtonBindings();

    drive.setDefaultCommand(new DriveXbox());

    SmartDashboard.putString("Driver Mode", driverController.getHand() == Hand.LEFT ? "Left Handed" : "Right Handed");
  }

  private void configureButtonBindings() {
    InstantCommand shooterOn = new InstantCommand(()-> Shooter.getInstance().shooterOn(3000),Shooter.getInstance());
    InstantCommand shooterOff = new InstantCommand(()-> Shooter.getInstance().shooterOff(),Shooter.getInstance());
    driverController.Buttons.A.whenActive(shooterOn).whenInactive(shooterOff);
  }

  public Command getAutonomousCommand() {
    return null;
  }
}