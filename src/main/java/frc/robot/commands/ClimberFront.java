// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimberFront extends CommandBase {
  private final Climber climber = Climber.getInstance();

  public ClimberFront() {
    addRequirements(this.climber);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    climber.climbFront();
  }

  @Override
  public void end(boolean interrupted) {
    climber.stopAll();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}