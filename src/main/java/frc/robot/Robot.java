// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.CG_ClimberCalibrate;
import frc.robot.subsystems.*;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import frc.robot.util.controller.BetterXboxController;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggedNetworkTables;
import org.littletonrobotics.junction.inputs.LoggedSystemStats;
import org.littletonrobotics.junction.io.ByteLogReceiver;
import org.littletonrobotics.junction.io.LogSocketServer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Robot extends LoggedRobot {
  private final Drive drive = Drive.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final Climber climber = Climber.getInstance();
  private final Intake intake = Intake.getInstance();
  private final Indexer indexer = Indexer.getInstance();
  private final Compressor7 compressor = Compressor7.getInstance();
  private final Limelight limelight = Limelight.getInstance();

  private final ShuffleboardTab climberTab = Shuffleboard.getTab("Climber");

  private final PowerDistribution revPDH = new PowerDistribution();

  private Command autonomousCommand;

  private ByteLogReceiver logReceiver;
  private final Alert logReceiverQueueAlert = new Alert("Logging queue is full. Data will NOT be logged.", AlertType.ERROR);
  private final Alert logOpenFileAlert = new Alert("Failed to open log file. Data will NOT be logged", AlertType.ERROR);
  private final Alert logWriteAlert = new Alert("Failed write to the log file. Data will NOT be logged", AlertType.ERROR);

  private RobotContainer robotContainer;

  @Override
  public void robotInit() {
//    advantageKit
    Logger logger = Logger.getInstance();
    setUseTiming(true);
    LoggedNetworkTables.getInstance().addTable("/SmartDashboard/Indexer");
    logger.recordMetadata("Project", "RapidReact2022");
    logger.recordMetadata("Date", new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(new Date()));
    logReceiver = new ByteLogReceiver("/media/sda1/");
    logger.addDataReceiver(logReceiver);
    logger.addDataReceiver(new LogSocketServer(5800));
    LoggedSystemStats.getInstance().setPowerDistributionConfig(1, ModuleType.kRev);
    logger.start();
//    config
    DriverStation.silenceJoystickConnectionWarning(true);
    climberTab.add("Calibrate Climber", new CG_ClimberCalibrate());
    robotContainer = new RobotContainer();
    revPDH.setSwitchableChannel(true);
    drive.coastMode();
    intake.retract();
    limelight.disable();
    indexer.disable();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

    Logger.getInstance().recordOutput("ActiveCommands/Scheduler",
            NetworkTableInstance.getDefault()
                    .getEntry("/LiveWindow/Ungrouped/Scheduler/Names")
                    .getStringArray(new String[] {}));

    logReceiverQueueAlert.set(Logger.getInstance().getReceiverQueueFault());
    if (logReceiver != null) {
      logOpenFileAlert.set(logReceiver.getOpenFault());
      logWriteAlert.set(logReceiver.getWriteFault());
    }

    new BetterXboxController(0, BetterXboxController.Hand.LEFT, BetterXboxController.Humans.DRIVER);
    new BetterXboxController(1, BetterXboxController.Humans.OPERATOR);
  }

  @Override
  public void disabledInit() {
    if (drive.getAverageVelocity() == 0) {
      drive.coastMode();
    }
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    drive.brakeMode();
    indexer.resetEverything();
    autonomousCommand = robotContainer.getAutonomousCommand();

    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    drive.brakeMode();
    if (indexer.isDisabled()) {
      indexer.resetEverything();
    }
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }
}