/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;
import frc.robot.Constants;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Pathing extends SequentialCommandGroup {
  /**
   * Creates a new FollowPath.
   */
  public Pathing(Trajectory traj) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
        new InstantCommand(() -> Drive.getInstance().resetOdometry(traj.getInitialPose()), Drive.getInstance()),
        new RamseteCommand(
            traj,
            Drive.getInstance()::getPose,
            new RamseteController(),
            new SimpleMotorFeedforward(Constants.DriveS, Constants.DriveV, Constants.DriveA),
            Constants.DriveKinematics,
            Drive.getInstance()::getWheelSpeeds,
            Drive.getInstance().getLeftController(),
            Drive.getInstance().getRightController(),
            Drive.getInstance()::driveVolts,
            Drive.getInstance()),
        new InstantCommand(() -> Drive.getInstance().driveVolts(0, 0), Drive.getInstance())
    );
  }
}