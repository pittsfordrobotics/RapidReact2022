// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.util.controller.BetterXboxController;
import frc.robot.util.controller.BetterXboxController.Hand;
import frc.robot.util.controller.BetterXboxController.Humans;

public class RobotContainer {
  private final Drive drive = Drive.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final Hood hood = Hood.getInstance();
  private final Indexer indexer = Indexer.getInstance();
  private final Compressor7 compressor = Compressor7.getInstance();

  private final BetterXboxController driverController = new BetterXboxController(0, Hand.RIGHT, Humans.DRIVER);
  private final BetterXboxController operatorController = new BetterXboxController(1, Humans.OPERATOR);

  private final SendableChooser<Command> commandChooser = new SendableChooser<>();

  public RobotContainer() {
    configureButtonBindings();

    drive.setDefaultCommand(new DriveXbox());
    shooter.setDefaultCommand(new ShooterDefault());
    hood.setDefaultCommand(new HoodDefault());
    compressor.setDefaultCommand(new CompressorSmart());

    commandChooser.setDefaultOption("Path Planner Test", new AutoPathing(Constants.TRAJECTORY_PATHPLANNER_TEST));

    SmartDashboard.putData("Auto Command", commandChooser);
    SmartDashboard.putString("Driver Mode", driverController.getHand() == Hand.LEFT ? "Left Handed" : "Right Handed");
  }

  private void configureButtonBindings() {
    driverController.A.whenActive(new CG_LowShot());

    driverController.B.whenPressed(new IntakeSmart());

    driverController.DUp.whenPressed(() -> drive.setThrottle(1));
    driverController.DLeft.whenPressed(() -> drive.setThrottle(0.7));
    driverController.DRight.whenPressed(() -> drive.setThrottle(0.4));
    driverController.DDown.whenPressed(() -> drive.setThrottle(0.1));
    
    operatorController.RT.and(operatorController.LB.negate()).whileActiveContinuous(new ClimberFront());
    operatorController.RT.and(operatorController.LB).whileActiveContinuous(new ClimberBack());
  }

  public Command getAutonomousCommand() {
    return commandChooser.getSelected();
  }
}