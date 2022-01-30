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

public class DriveXbox extends CommandBase {
    private final Drive mDrive;
    private final XboxController mController;
    private double pastInput;
    private boolean accelerate;

    /**
     * Creates a new DriveWithJoysticks.
     */
    public DriveXbox(XboxController xboxController) {
        mDrive = Drive.getInstance();
        mController = xboxController;
        addRequirements(mDrive);
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
        if (accelerate && Math.abs(mDrive.getLeftVelocity()) > 0) {
            accelerate = true;
        }
        else {
            accelerate = mController.getLeftY() - pastInput < 0;
        }
        pastInput = mController.getLeftY();
        if (accelerate) {
            mDrive.drive(mDrive.getRateLimit().calculate(mController.getLeftY()), mController.getRightX() * -0.75);
        }
        else {
            mDrive.drive(mController.getLeftY(), mController.getRightX() * -0.75);
        }
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