/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class DriveXbox extends CommandBase {
    private double pastInput;
    private boolean accelerate;

    public DriveXbox() {
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
            accelerate = driverController.getDriveY() - pastInput < 0;
        }
        pastInput = driverController.getDriveY();
        drive.drive(accelerate ? drive.getRateLimit().calculate(driverController.getDriveY()) : driverController.getDriveY(), driverController.getDriveX() * -0.75);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}