// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.Drive;
import frc.robot.util.controller.BetterXboxController;

public class RobotContainer {
  private final Drive drive = Drive.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final Climber climber = Climber.getInstance();
  private final Intake intake = Intake.getInstance();
  private final Limelight limelight = Limelight.getInstance();
  private final Indexer indexer = Indexer.getInstance();
  private final Compressor7 compressor = Compressor7.getInstance();

  private final BetterXboxController driverController = new BetterXboxController(0, BetterXboxController.Hand.LEFT, BetterXboxController.Humans.DRIVER);
  private final BetterXboxController operatorController = new BetterXboxController(1, BetterXboxController.Humans.OPERATOR);

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();
  private final SendableChooser<Integer> ballChooser = new SendableChooser<>();

  public RobotContainer() {
    autoConfig();

    configureButtonBindings();
//    testButtons();

    drive.setDefaultCommand(new DriveXbox());
    compressor.setDefaultCommand(new CompressorSmart());
  }

  private void testButtons() {
    driverController.A.whenActive(new IntakeToggle());
//    driverController.X.whileActiveOnce(new CG_LowShot()).whenInactive(new ShooterZero());
    driverController.X.whenActive(new CG_IntakeWiggle());
    driverController.B.whileActiveOnce(new ShooterDashboard()).whenInactive(new ShooterZero());
    driverController.Y.whenActive(new ShooterLime());
    driverController.Start.whenActive(new LimelightEnable());
    driverController.Back.whenActive(new LimelightDisable());

    operatorController.Y.whenHeld(new IndexerOverride(false));
    operatorController.B.whileActiveOnce(new IndexerOverride(true));
  }

  private void configureButtonBindings() {
    driverController.A.whenActive(new IntakeToggle());
    driverController.X.whileActiveOnce(new CG_LowShot()).whenInactive(new ShooterZero());
//    driverController.Y.whileActiveOnce(new CG_LimeShot()).whenInactive(new ShooterZero());
    driverController.X.and(driverController.RB).whileActiveOnce(new ShooterLow()).whenInactive(new ShooterZero());
    driverController.B.whenActive(new CG_UnoShot());
    driverController.DUp.whenPressed(new DriveSetThrottle(1));
    driverController.DLeft.whenPressed(new DriveSetThrottle(0.8));
    driverController.DRight.whenPressed(new DriveSetThrottle(0.7));
    driverController.DDown.whenPressed(new DriveSetThrottle(0.6));

    operatorController.X.whileActiveOnce(new CG_LowShot()).whenInactive(new ShooterZero());
    operatorController.B.whenActive(new CG_UnoShot());
    operatorController.Y.whileActiveOnce(new IndexerOverride(false));
    operatorController.Y.and(operatorController.RB).whileActiveOnce(new SequentialCommandGroup(new IntakeReverse(), new IndexerOverride(true)));
    operatorController.LB.and(operatorController.Start).whileActiveContinuous(new ClimberForward()).whenInactive(new ClimberStop());
    operatorController.LB.and(operatorController.Back).whileActiveContinuous(new ClimberReverse()).whenInactive(new ClimberStop());
  }

  private void autoConfig() {
    autoChooser.setDefaultOption("No auto", new WaitCommand(0));
    autoChooser.addOption("Run Back", new SequentialCommandGroup(new DrivePathing(Constants.TRAJECTORY_THREE_METER_BACKWARD)));
    autoChooser.addOption("Shoot and Run Back", new AutoShootAndRun());
    autoChooser.addOption("2 Ball Bottom", new AutoFirstBottomLow2());
    autoChooser.addOption("2 Ball Left", new AutoFirstLeftLow2());
    autoChooser.addOption("3 Ball", new AutoSecondLow3());

    ballChooser.setDefaultOption("0", 0);
    ballChooser.addOption("1", 1);

    SmartDashboard.putData("Auto Command", autoChooser);
    SmartDashboard.putData("Starting Balls", ballChooser);
  }

  public Command getAutonomousCommand() {
    if (ballChooser.getSelected() == 1) {
      indexer.addBallToTower();
    }
    return autoChooser.getSelected();
  }
}