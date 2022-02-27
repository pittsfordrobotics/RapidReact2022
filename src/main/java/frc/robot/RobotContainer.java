// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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

  public RobotContainer() {
    configureButtonBindings();

    drive.setDefaultCommand(new DriveXbox());
    climber.setDefaultCommand(new ClimberSpeed());
    compressor.setDefaultCommand(new CompressorSmart());

    firstAutoChooser.setDefaultOption("No auto", null);
    firstAutoChooser.addOption("2 Ball Bottom Low", new AutoFirstBottomLow2());
    firstAutoChooser.addOption("2 Ball Left Low", new AutoFirstLeftLow2());

    secondAutoChooser.setDefaultOption("No auto", null);
    secondAutoChooser.addOption("3 Ball Low", new AutoSecondLow3());
    secondAutoChooser.addOption("4 Ball Low", new AutoSecondLow4());

//    SmartDashboard.putData("Auto Command", commandChooser);
  }

  private void configureButtonBindings() {
//    driverController.A.whenActive(new CG_LowShot());
//    driverController.B.whenPressed(new IntakeSmart());
    driverController.X.whenPressed(new IntakeSmart());
//    driverController.RB.and(driverController.LB).and(operatorController.RB).and(operatorController.LB).whileActiveOnce(new CG_ClimberAuto());
    operatorController.A.whenActive(new CG_ClimberCalibrate());
//    operatorController.X.whenActive(new ClimberFrontSmart());
//    operatorController.Y.whenActive(new ClimberReverseSmart());

    driverController.DUp.whenPressed(new DriveSetThrottle(1));
    driverController.DLeft.whenPressed(new DriveSetThrottle(0.7));
    driverController.DRight.whenPressed(new DriveSetThrottle(0.4));
    driverController.DDown.whenPressed(new DriveSetThrottle(0.1));

//    operatorController.RT.and(operatorController.LB.negate()).whileActiveContinuous(new ClimberFront());
//    operatorController.RT.and(operatorController.LB).whileActiveContinuous(new ClimberReverse());
  }

  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
      firstAutoChooser.getSelected(),
      secondAutoChooser.getSelected()
    );
  }
}