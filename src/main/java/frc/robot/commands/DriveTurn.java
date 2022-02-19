// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

public class DriveTurn extends CommandBase {
  private final double angle;
  private final Drive drive = Drive.getInstance();
  private final PIDController pidController = new PIDController(1,0,0);
  private double endingAngle;
  private double throttle;

  public DriveTurn(double angle) {
    this.angle = angle;
    addRequirements(this.drive);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pidController.setSetpoint();
    throttle = drive.getThrottle();
    endingAngle = drive.getAngle() + angle;
    drive.setThrottle(0.6);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(angle < 0) {
      drive.driveArcade(0, 1);
    }
    else {
      drive.driveArcade(0, -1);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.setThrottle(throttle);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double currentAngle = drive.getAngle();
    if(angle >= 0) {
      return currentAngle > endingAngle;
    }
    else {
      return currentAngle < endingAngle;
    }
  }
}