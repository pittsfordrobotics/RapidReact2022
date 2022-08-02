// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.commands.DriveSnap.SnapPosition;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.compressor7.Compressor7;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.indexer.Indexer;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Vision;
import frc.robot.util.controller.BetterXboxController;

public class RobotContainer {
  private final Drive drive = Drive.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final Climber climber = Climber.getInstance();
  private final Intake intake = Intake.getInstance();
  private final Vision vision = Vision.getInstance();
  private final Indexer indexer = Indexer.getInstance();
  private final Compressor7 compressor = Compressor7.getInstance();

  private final BetterXboxController driverController = new BetterXboxController(0, BetterXboxController.Hand.LEFT, BetterXboxController.Humans.DRIVER);
  private final BetterXboxController operatorController = new BetterXboxController(1, BetterXboxController.Humans.OPERATOR);

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();
  private final SendableChooser<Integer> ballChooser = new SendableChooser<>();
  private final SendableChooser<Pose2d> positionChooser = new SendableChooser<>();

  public RobotContainer() {
    autoConfig();
    driverDashboardSetup();

    configureButtonBindings();
//    testButtons();

    drive.setDefaultCommand(new DriveXbox());
    compressor.setDefaultCommand(new CompressorSmart());
  }

  private void driverDashboardSetup() {
    SmartDashboard.putBoolean("Intake Extended", intake.isExtended());
    SmartDashboard.putBoolean("Able to Shoot", indexer.ableToShoot());
    SmartDashboard.putBoolean("Is Empty", indexer.isEmpty());
    SmartDashboard.putBoolean("1 Ball", indexer.getBallCount() == 1);
    SmartDashboard.putBoolean("Fully Loaded", indexer.fullyLoaded());
    SmartDashboard.putBoolean("Snapped", RobotState.getInstance().isSnapped());
    SmartDashboard.putBoolean("Climber Enabled", RobotState.getInstance().isClimbing());
  }

  private void testButtons() {
    driverController.A.whenActive(new IntakeToggle());
//    driverController.X.whileActiveOnce(new CG_LowShot()).whenInactive(new ShooterHoodZero());
    driverController.X.whenActive(new CG_IntakeWiggle());
    driverController.Y.whenActive(new ShooterHoodLime());

    operatorController.Y.whenHeld(new IndexerOverride(false));
    operatorController.B.whileActiveOnce(new IndexerOverride(true));
  }

  private void configureButtonBindings() {
    JoystickButton driverShift = driverController.RB;
    JoystickButton operatorShift = operatorController.RB;

//    DRIVING
    driverController.Start.whileActiveOnce(new DriveAutoSnap());

    driverController.X.whileActiveOnce(new DriveSnap(SnapPosition.LEFT_FENDER_FAR));
    driverController.Y.whileActiveOnce(new DriveSnap(SnapPosition.RIGHT_FENDER_FAR));
    driverController.B.whileActiveOnce(new DriveSnap(SnapPosition.RIGHT_FENDER_CLOSE));
    driverController.A.whileActiveOnce(new DriveSnap(SnapPosition.LEFT_FENDER_CLOSE));

    driverController.X.and(driverShift).whileActiveOnce(new DriveSnap(SnapPosition.RIGHT));
    driverController.Y.and(driverShift).whileActiveOnce(new DriveSnap(SnapPosition.BACKWARD));
    driverController.B.and(driverShift).whileActiveOnce(new DriveSnap(SnapPosition.LEFT));
    driverController.A.and(driverShift).whileActiveOnce(new DriveSnap(SnapPosition.FORWARD));

    driverController.DUp.whenPressed(new DriveSetThrottle(1));
    driverController.DLeft.whenPressed(new DriveSetThrottle(0.4));
    driverController.DRight.whenPressed(new DriveSetThrottle(0.7));
    driverController.DDown.whenPressed(new DriveSetThrottle(0.1));

//    SHOOTING
    operatorController.X.whileActiveOnce(new CG_LimeShot());
    operatorController.Y.whileActiveOnce(new CG_FenderShot());

//    INDEXER
    driverController.LB.and(operatorController.LB).whenActive(new InstantCommand(Indexer.getInstance()::resetEverything, indexer));
    operatorController.B.whileActiveOnce(new IndexerOverride(false));
    operatorController.B.and(operatorShift).whileActiveOnce(new SequentialCommandGroup(new IntakeReverse(), new IndexerOverride(true)));

//    INTAKE
    operatorController.A.whileActiveContinuous(new IntakeDown()).whenInactive(new IntakeUp());

//    CLIMBING
    driverController.Start.and(operatorController.Start).whenActive(new ClimberSetState(!RobotState.getInstance().isClimbing()));
    operatorController.RT.whileActiveContinuous(new ClimberForward()).whenInactive(new ClimberStop());
    operatorController.LT.whileActiveContinuous(new ClimberReverse()).whenInactive(new ClimberStop());
    operatorController.Back.whileActiveContinuous(new CG_ClimberAuto()).whenInactive(new ClimberStop());
  }

  private void autoConfig() {
    autoChooser.setDefaultOption("No auto", new WaitCommand(0));
    autoChooser.addOption("Run Back", new SequentialCommandGroup(new DrivePathing(Trajectories.THREE_METERS_BACKWARD, false)));
    autoChooser.addOption("Shoot and Run Back", new AutoLimeShotAndRun());
    autoChooser.addOption("2 Ball Top Reject 1", new AutoTop2Reject1());
    autoChooser.addOption("1 Ball Top Left Reject 1", new AutoTopLeft1Reject1());
    autoChooser.addOption("2 Ball Top Left Reject 2", new AutoTopLeft2Reject2());
    autoChooser.addOption("5 Ball", new Auto5BallAuto());
//    autoChooser.addOption("Test", new DrivePathing(PathPlanner.loadPath("New Path", Constants.DRIVE_MAX_VELOCITY_METERS_PER_SECOND, Constants.DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED), true));

    ballChooser.setDefaultOption("0", 0);
    ballChooser.addOption("1", 1);

    positionChooser.setDefaultOption("No position", new Pose2d());
    positionChooser.addOption("Bottom Right", new Pose2d(8.39, 1.91, new Rotation2d(Units.degreesToRadians(90))));
    positionChooser.addOption("Bottom Right Center", new Pose2d(7.6, 1.89, new Rotation2d(Units.degreesToRadians(90))));
    positionChooser.addOption("Bottom Left Center", new Pose2d(7.26, 2.04, new Rotation2d(Units.degreesToRadians(45))));
    positionChooser.addOption("Bottom Left", new Pose2d(6.7, 2.56, new Rotation2d(Units.degreesToRadians(45))));
    positionChooser.addOption("Bottom Hub Left", new Pose2d(7.62, 2.89, new Rotation2d(Units.degreesToRadians(67.65))));
    positionChooser.addOption("Bottom Hub Right", new Pose2d(7.91, 2.8, new Rotation2d(Units.degreesToRadians(67.65))));

    positionChooser.addOption("Top Right", new Pose2d(6.72, 5.68, new Rotation2d(Units.degreesToRadians(-45))));
    positionChooser.addOption("Top Right Center", new Pose2d(6.19, 5.1, new Rotation2d(Units.degreesToRadians(-45))));
    positionChooser.addOption("Top Left Center", new Pose2d(6.05, 4.72, new Rotation2d(Units.degreesToRadians(0))));
    positionChooser.addOption("Top Left", new Pose2d(6.05, 4.02, new Rotation2d(Units.degreesToRadians(0))));
    positionChooser.addOption("Top Hub Left", new Pose2d(6.94, 4.46, new Rotation2d(Units.degreesToRadians(-22))));
    positionChooser.addOption("Top Hub Right", new Pose2d(7.05, 4.78, new Rotation2d(Units.degreesToRadians(-22))));

    SmartDashboard.putData("Auto Command", autoChooser);
    SmartDashboard.putData("Starting Balls", ballChooser);
    SmartDashboard.putData("Position Chooser", positionChooser);
  }

  public Command getAutonomousCommand() {
    if (ballChooser.getSelected() == 1) {
      indexer.addBallToTower();
    }
    drive.resetOdometry(positionChooser.getSelected());
    return autoChooser.getSelected();
  }
}