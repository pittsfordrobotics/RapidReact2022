// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

// TODO: TUNE P CONTROLLER WITH FINISHED CHASSIS
public class DriveTurn extends CommandBase {
  private final double angle;
  private final Drive drive = Drive.getInstance();
  private final PIDController pidController = new PIDController(0.01,0, 0);
  private double throttle;

  public DriveTurn(double angle) {
    this.angle = angle;
    SmartDashboard.putNumber("PID TURN", 0.06);
    addRequirements(this.drive);
  }

  @Override
  public void initialize() {
    pidController.setP(SmartDashboard.getNumber("PID TURN", 0));
    drive.setTempThrottle(0.6);
    pidController.setSetpoint(drive.getAngle() + angle);
    pidController.setTolerance(1);
  }

  @Override
  public void execute() {
    drive.driveArcade(0, -MathUtil.clamp(pidController.calculate(drive.getAngle()) + ((angle < 0) ? -0.1 : 0.1), -0.5, 0.5), false);
//    SmartDashboard.putNumber("PID OUT", Math.min(pidController.calculate(drive.getAngle()),0.7));
  }

  @Override
  public void end(boolean interrupted) {
    drive.setThrottleWithTemp();
  }

  @Override
  public boolean isFinished() {
    return pidController.atSetpoint();
  }
}