// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

public class DriveTurn extends CommandBase {
  private final double angle;
  private final Drive drive = Drive.getInstance();
  private final PIDController pidController = new PIDController(0.01,0,0);
  private double endingAngle;
  private double throttle;

  public DriveTurn(double angle) {
    this.angle = angle;
    addRequirements(this.drive);
    pidController.setTolerance(5);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pidController.setP(SmartDashboard.getNumber("PID TURN", 0));
    pidController.setD(SmartDashboard.getNumber("PID TURN D", 0));
    throttle = drive.getThrottle();
    endingAngle = drive.getAngle() + angle;
    drive.setThrottle(0.6);
    pidController.setSetpoint(endingAngle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drive.driveArcade(0, Math.min(pidController.calculate(drive.getAngle()),0.7));
    SmartDashboard.putNumber("PID OUT", Math.min(pidController.calculate(drive.getAngle()),0.7));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.setThrottle(throttle);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // double currentAngle = drive.getAngle();
    // if(angle >= 0) {
    //   return currentAngle > endingAngle;
    // }
    // else {
    //   return currentAngle < endingAngle;
   //    }
   return pidController.atSetpoint();
  }
}