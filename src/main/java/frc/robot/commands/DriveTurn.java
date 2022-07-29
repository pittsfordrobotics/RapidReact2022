// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;

public class DriveTurn extends CommandBase {
  private final double angle;
  private final Drive drive = Drive.getInstance();
  private final PIDController pidController = new PIDController(0.01,0, 0);
//  private final TrapezoidProfile profile = new TrapezoidProfile(new Constraints(10, 3), )

  /**
   * Auto turn for driving
   * @param angle positive = counterclockwise
   */
  public DriveTurn(double angle) {
    this.angle = angle;
    addRequirements(this.drive);
  }

  @Override
  public void initialize() {
    drive.setTempThrottle(0.6);
    pidController.setSetpoint(drive.getAngle() + angle);
    pidController.setTolerance(5);
  }

  @Override
  public void execute() {
    drive.driveArcade(0, -MathUtil.clamp(pidController.calculate(drive.getAngle()) + (angle > 0 ? 0.1 : -0.1), -0.5, 0.5), false);
//    drive.driveArcade(0, -MathUtil.clamp(pidController.calculate(drive.getAngle()) + (angle > 0 ? 0.1 : -0.1), -0.5, 0.5), false);
  }

  @Override
  public void end(boolean interrupted) {
    drive.setVolts(0,0);
    drive.setThrottleWithTemp();
  }

  @Override
  public boolean isFinished() {
    return pidController.atSetpoint();
  }
}