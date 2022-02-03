/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShooterXbox extends CommandBase {
    private final Shooter shooter;
    private final XboxController controller;

    public ShooterXbox(XboxController xboxController) {
        shooter = Shooter.getInstance();
        controller = xboxController;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        accelerate = false;
        pastInput = 0;
    }

    @Override
    public void execute() {
        shooter.TurningShooterOff();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}