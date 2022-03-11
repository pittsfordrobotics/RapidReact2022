// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.util.controller.BetterXboxController;

public class RobotContainer {
  private final Drive drive = Drive.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final Climber climber = Climber.getInstance();
  private final Intake intake = Intake.getInstance();
  private final Indexer indexer = Indexer.getInstance();
  private final Compressor7 compressor = Compressor7.getInstance();

  private final BetterXboxController driverController = new BetterXboxController(0, BetterXboxController.Hand.LEFT, BetterXboxController.Humans.DRIVER);
  private final BetterXboxController operatorController = new BetterXboxController(1, BetterXboxController.Humans.OPERATOR);

  private final SendableChooser<Command> firstAutoChooser = new SendableChooser<>();
  private final SendableChooser<Command> secondAutoChooser = new SendableChooser<>();
  private final SendableChooser<Integer> ballChooser = new SendableChooser<>();

  public RobotContainer() {
    CameraServer.startAutomaticCapture(0);

    configureButtonBindings();
//    testButtons();

    drive.setDefaultCommand(new DriveXbox());
    compressor.setDefaultCommand(new CompressorSmart());

    firstAutoChooser.setDefaultOption("No auto", new WaitCommand(1));
    firstAutoChooser.setDefaultOption("Shoot and Run", new AutoShootAndRun());
    firstAutoChooser.addOption("2 Ball Bottom Low", new AutoFirstBottomLow2());
    firstAutoChooser.addOption("2 Ball Left Low", new AutoFirstLeftLow2());

    secondAutoChooser.setDefaultOption("No auto", new WaitCommand(1));
    secondAutoChooser.addOption("3 Ball Low", new AutoSecondLow3());
    secondAutoChooser.addOption("4 Ball Low", new AutoSecondLow4());

    ballChooser.setDefaultOption("0", 0);
    ballChooser.addOption("1", 1);

    //TODO: redo autos
    SmartDashboard.putData("First Auto Command", firstAutoChooser);
    SmartDashboard.putData("Second Auto Command", secondAutoChooser);
    SmartDashboard.putData("Starting Balls", ballChooser);
  }

  private void testButtons() {
    driverController.A.whenHeld(new CG_LowShot()).whenInactive(new ShooterZero());
    driverController.Y.whenActive(new IntakeToggle());
    driverController.X.whenHeld(new IntakeOff());
    driverController.B.whenActive(new DriveTurn(180));
//    driverController.X.whenHeld(new ShooterDumb()).whenInactive(new ShooterZero());
//    driverController.RB.and(driverController.LB).and(operatorController.RB).and(operatorController.LB).whileActiveOnce(new CG_ClimberAuto());
//    operatorController.A.whenActive(new CG_ClimberCalibrate());
//    operatorController.X.whenActive(new ClimberForward());
//    operatorController.Y.whenActive(new ClimberReverse());

    operatorController.Y.whenHeld(new IndexerOverride(false));
    operatorController.B.whileActiveOnce(new IndexerOverride(true));

    driverController.DUp.whenPressed(new DriveSetThrottle(1));
    driverController.DLeft.whenPressed(new DriveSetThrottle(0.7));
    driverController.DRight.whenPressed(new DriveSetThrottle(0.5));
    driverController.DDown.whenPressed(new DriveSetThrottle(0.25));

  }

  private void configureButtonBindings() {
    driverController.A.whenActive(new IntakeToggle());
    driverController.X.whileActiveOnce(new CG_LowShot()).whenInactive(new ShooterZero());
    driverController.X.and(driverController.RB).whileActiveOnce(new ShooterLow()).whenInactive(new ShooterZero());
    driverController.B.whenActive(new CG_UnoShot());
    driverController.DUp.whenActive(new DriveSetThrottle(1));
    driverController.DLeft.whenActive(new DriveSetThrottle(0.7));
    driverController.DRight.whenActive(new DriveSetThrottle(0.4));
    driverController.DDown.whenActive(new DriveSetThrottle(0.1));

    operatorController.A.whenActive(new IntakeToggle());
    operatorController.X.whileActiveOnce(new CG_LowShot()).whenInactive(new ShooterZero());
    operatorController.B.whenActive(new CG_UnoShot());
    operatorController.Y.whileActiveOnce(new IndexerOverride(false));
    operatorController.Y.and(operatorController.RB).whileActiveOnce(new IndexerOverride(true));
    operatorController.LB.and(operatorController.Back).whileActiveOnce(new CG_ClimberAuto()).whenInactive(new ClimberStop());
    operatorController.LB.and(operatorController.DUp).whileActiveOnce(new ClimberForward());
    operatorController.LB.and(operatorController.DDown).whileActiveOnce(new ClimberReverse());
  }

  public Command getAutonomousCommand() {
    if (ballChooser.getSelected() == 1) {
      indexer.addBallToTower();
    }
    return new SequentialCommandGroup(
      firstAutoChooser.getSelected(),
      secondAutoChooser.getSelected()
    );
  }
}