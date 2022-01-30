/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import frc.robot.util.BetterXboxController;

public class DriveXbox extends CommandBase {
    private final Drive drive;
    private final BetterXboxController controller;
    private double pastInput;
    private boolean accelerate;

    public DriveXbox(BetterXboxController xboxController) {
        drive = Drive.getInstance();
        controller = xboxController;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        accelerate = false;
        pastInput = 0;
    }

    @Override
    public void execute() {
        if (accelerate && Math.abs(drive.getLeftVelocity()) > 0) {
            accelerate = true;
        }
        else {
            accelerate = controller.getDriveY() - pastInput < 0;
        }
        pastInput = controller.getDriveY();
        drive.drive(accelerate ? drive.getRateLimit().calculate(controller.getDriveY()) : controller.getDriveY(), controller.getDriveX() * -0.75);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}