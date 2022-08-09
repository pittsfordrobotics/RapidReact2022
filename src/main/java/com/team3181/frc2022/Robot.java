// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team3181.frc2022;

import com.team3181.frc2022.commands.ControllerRumble;
import com.team3181.frc2022.subsystems.indexer.Indexer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import com.team3181.frc2022.commands.CG_ClimberCalibrate;
import com.team3181.frc2022.subsystems.intake.Intake;
import com.team3181.lib.util.Alert;
import com.team3181.lib.util.Alert.AlertType;
import com.team3181.lib.util.PIDTuner;
import com.team3181.lib.controller.BetterXboxController;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggedNetworkTables;
import org.littletonrobotics.junction.inputs.LoggedSystemStats;
import org.littletonrobotics.junction.io.ByteLogReceiver;
import org.littletonrobotics.junction.io.LogSocketServer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Robot extends LoggedRobot {
  private final Intake intake = Intake.getInstance();
  private final Indexer indexer = Indexer.getInstance();

  private final ShuffleboardTab climberTab = Shuffleboard.getTab("Climber");

  private Command autonomousCommand;

  public static final Field2d field = new Field2d();

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
    LoggedNetworkTables.getInstance().addTable("/SmartDashboard/");
    LoggedNetworkTables.getInstance().addTable("/Shuffleboard/");
    logger.recordMetadata("Date", new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(new Date()));
    logger.recordMetadata("PIDTuner", Boolean.toString(Constants.ROBOT_PID_TUNER_ENABLED));
    logger.recordMetadata("RuntimeType", getRuntimeType().toString());
    logger.recordMetadata("ProjectName", GitConstants.MAVEN_NAME);
    logger.recordMetadata("BuildDate", GitConstants.BUILD_DATE);
    logger.recordMetadata("GitSHA", GitConstants.GIT_SHA);
    logger.recordMetadata("GitDate", GitConstants.GIT_DATE);
    logger.recordMetadata("GitBranch", GitConstants.GIT_BRANCH);
    logger.recordMetadata("GitDirty", GitConstants.DIRTY == 0 ? "Clean" : "Dirty");
    logger.addDataReceiver(new LogSocketServer(5800));
    if (RobotBase.isReal()) {
      logReceiver = new ByteLogReceiver(Constants.ROBOT_LOGGING_PATH);
      logger.addDataReceiver(logReceiver);
      LoggedSystemStats.getInstance().setPowerDistributionConfig(Constants.ROBOT_PDP_CAN, ModuleType.kRev);
    }
    if (Constants.ROBOT_LOGGING_ENABLED) logger.start();
    PIDTuner.enable(Constants.ROBOT_PID_TUNER_ENABLED);

//    setup
    robotContainer = new RobotContainer();
    DriverStation.silenceJoystickConnectionWarning(true);
    climberTab.add("Calibrate Climber", new CG_ClimberCalibrate());
    intake.retract();
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

    field.setRobotPose(RobotState.getInstance().getLatestPose());

    new BetterXboxController(0, BetterXboxController.Hand.LEFT, BetterXboxController.Humans.DRIVER);
    new BetterXboxController(1, BetterXboxController.Humans.OPERATOR);
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
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
    if (indexer.isDisabled()) {
      indexer.resetEverything();
    }
    indexer.setRejectionEnabled(true);
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    if (RobotState.getInstance().isSnapped()) {
      CommandScheduler.getInstance().schedule(new ControllerRumble(0, new boolean[] {false, true}));
    }
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }
}