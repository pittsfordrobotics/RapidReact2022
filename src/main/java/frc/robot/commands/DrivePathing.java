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
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;
import static frc.robot.Constants.*;

public class DrivePathing extends SequentialCommandGroup {

  public DrivePathing(Trajectory traj) {
    super(
        new InstantCommand(() -> Drive.getInstance().resetOdometry(traj.getInitialPose()), Drive.getInstance()),
        new RamseteCommand(
            traj,
            Drive.getInstance()::getPose,
            new RamseteController(),
            new SimpleMotorFeedforward(DRIVE_S, DRIVE_V, DRIVE_A),
            new DifferentialDriveKinematics(DRIVE_TRACK_WIDTH),
            Drive.getInstance()::getWheelSpeeds,
            Drive.getInstance().getLeftController(),
            Drive.getInstance().getRightController(),
            Drive.getInstance()::driveVolts,
            Drive.getInstance()),
        new InstantCommand(() -> Drive.getInstance().driveVolts(0, 0), Drive.getInstance())
    );
  }
}