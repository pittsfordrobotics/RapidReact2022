/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

import static frc.robot.RobotContainer.driverController;

public class DriveXbox extends CommandBase {
    private final Drive drive = Drive.getInstance();

    private double pastInput;
    private double velocity;
    private boolean decelerate;

    public DriveXbox() {
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        decelerate = false;
        pastInput = 0;
        velocity = 0;
    }

    @Override
    public void execute() {
        SmartDashboard.putNumber("past input", pastInput);
        SmartDashboard.putNumber("deadband", MathUtil.applyDeadband((drive.getLeftVelocity()+drive.getRightVelocity())/2,0.2));
        SmartDashboard.putNumber("minus", driverController.getDriveY() - pastInput);
        if (Math.abs(MathUtil.applyDeadband((drive.getLeftVelocity()+drive.getRightVelocity())/2,0.2)) == 0) {
            decelerate = false;
        }
        else if (!decelerate) {
            decelerate = driverController.getDriveY() != 0 && (driverController.getDriveY() > 0 ? driverController.getDriveY() - pastInput < 0 : driverController.getDriveY() - pastInput > 0);
        }
        pastInput = driverController.getDriveY();
        if (decelerate) { driverController.rumbleOn();}
        else {
            driverController.rumbleOff();
        }

        velocity = decelerate ? drive.getRateLimit().calculate(driverController.getDriveY()) : driverController.getDriveY();
        drive.getRateLimit().calculate(driverController.getDriveY());
        drive.drive(velocity, -driverController.getDriveX() * 0.5);
//        drive.drive(driverController.getDriveY(), -driverController.getDriveX() * 0.5);


        SmartDashboard.putBoolean("decelerating", decelerate);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}