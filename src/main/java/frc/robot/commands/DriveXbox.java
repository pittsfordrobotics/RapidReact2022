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

    private enum Joysticks {

    }

    /**
     * Creates a new DriveWithJoysticks.
     */
    public DriveXbox(BetterXboxController xboxController) {
        drive = Drive.getInstance();
        controller = xboxController;
        addRequirements(drive);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        accelerate = false;
        pastInput = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
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

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}