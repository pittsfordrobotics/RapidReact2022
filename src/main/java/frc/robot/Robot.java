// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.CG_ClimberCalibrate;
import frc.robot.subsystems.*;
import frc.robot.util.BetterLogger;
import frc.robot.util.controller.BetterXboxController;

public class Robot extends TimedRobot {
  private final Drive drive = Drive.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final Climber climber = Climber.getInstance();
  private final Intake intake = Intake.getInstance();
  private final Indexer indexer = Indexer.getInstance();
  private final Compressor7 compressor = Compressor7.getInstance();
  private final Limelight limelight = Limelight.getInstance();

  private final ShuffleboardTab climberTab = Shuffleboard.getTab("Climber");

  private final PowerDistribution revPDH = new PowerDistribution();

  private Command autonomousCommand;

  private RobotContainer robotContainer;

  @Override
  public void robotInit() {
    climberTab.add("Calibrate Climber", new CG_ClimberCalibrate());
    robotContainer = new RobotContainer();
    revPDH.setSwitchableChannel(true);
    drive.coastMode();
    intake.retract();
    limelight.disable();
    indexer.disable();
    BetterLogger.enable();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    new BetterXboxController(0, BetterXboxController.Hand.LEFT, BetterXboxController.Humans.DRIVER);
    new BetterXboxController(1, BetterXboxController.Humans.OPERATOR);
    BetterLogger.logData();
  }

  @Override
  public void disabledInit() {
    if (drive.getAverageVelocity() == 0) {
      drive.coastMode();
    }
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    drive.brakeMode();
    indexer.resetEverything();
    autonomousCommand = robotContainer.getAutonomousCommand();

    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    drive.brakeMode();
    if (indexer.isDisabled()) {
      indexer.resetEverything();
    }
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }
}