/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.drive.Drive;

// TODO: Change this to use RobotState pose
public class DrivePathing extends SequentialCommandGroup {
  public DrivePathing(Trajectory trajectory, boolean resetPose) {
    super(
        new DriveZero(),
        new DriveResetOdometry(trajectory, resetPose),
        new RamseteCommand(
            trajectory,
            Drive.getInstance()::getPose,
            new RamseteController(),
            new SimpleMotorFeedforward(Constants.DRIVE_STATIC_GAIN, Constants.DRIVE_VELOCITY_GAIN, Constants.DRIVE_ACCELERATION_GAIN),
            new DifferentialDriveKinematics(Constants.DRIVE_TRACK_WIDTH_METERS),
            Drive.getInstance()::getWheelSpeeds,
            Drive.getInstance().getLeftController(),
            Drive.getInstance().getRightController(),
            Drive.getInstance()::setVolts,
            Drive.getInstance()),
        new DriveZero()
    );
  }
}