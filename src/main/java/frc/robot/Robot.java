// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Limelight;
import frc.robot.util.controller.BetterXboxController;

public class Robot extends TimedRobot {
  private final Drive drive = Drive.getInstance();
  private final Limelight limelight = Limelight.getInstance();

  private Command autonomousCommand;

  private RobotContainer robotContainer;

  @Override
  public void robotInit() {
    robotContainer = new RobotContainer();
    drive.coastMode();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    new BetterXboxController(0, BetterXboxController.Hand.LEFT, BetterXboxController.Humans.DRIVER);
    new BetterXboxController(1, BetterXboxController.Humans.OPERATOR);
  }

  @Override
  public void disabledInit() {
    if (drive.getAverageVelocity() == 0) {
      drive.coastMode();
    }
    limelight.disable();
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    drive.brakeMode();
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