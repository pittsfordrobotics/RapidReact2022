package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.*;
import frc.robot.util.controller.BetterXboxController;
import frc.robot.util.controller.BetterXboxController.Humans;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom logging framework to help with rapid debugging and prototyping
 */
public class BetterLogger {
    private static final Drive drive = Drive.getInstance();
    private static final Shooter shooter = Shooter.getInstance();
    private static final Climber climber = Climber.getInstance();
    private static final Compressor7 compressor = Compressor7.getInstance();
    private static final Indexer indexer = Indexer.getInstance();
    private static final Intake intake = Intake.getInstance();
    private static final Limelight limelight = Limelight.getInstance();

    private static final String logFolder = "/media/sda2/";
    private static final String logTitle = "'BetterLogger'_MM-dd-yyyy_HH:mm:ss'.csv'";
    private static final String basicLogValues = "Timestamp,Enabled,X,Y,Rotation,Throttle,Shooter Armed,Indexer Balls,Target Detected";
    private static final String detailedLogValues = "Target Distance,Target Horizontal,Target Vertical,Shooter Speed,Shooter Set Speed,Indexer State,Ball0 Color,Ball0 Location,Ball1 Color,Ball1 Location,Compressor State,Intake Extended,Climber Encoder,Climber Forward Limit,Climber Reverse Limit,Pigeon Angle,Battery Voltage,CAN Bus Utilization";
    private static final String xboxLogValues = "Connected,Left X,Left Y,Right X,Right Y,Right Stick Button,Left Stick Button,A,B,X,Y,Start,Back,Right Bumper,Left Bumper,Right Trigger,Left Trigger,DPad";;
    private static final Timer logTimer = new Timer();
    private static final double logRate = 0.1;
    private static boolean loggerEnabled = false;

    private static FileWriter logger;

    public static void logData() {
        if (loggerEnabled) {
            if (logger != null) {
                if (logTimer.advanceIfElapsed(logRate)) {
                    try {
                        logBasicData(logger, false);
                        logDetailedData(logger, false);
                        logXboxController(logger, Humans.DRIVER, false);
                        logXboxController(logger, Humans.OPERATOR, true);
                    } catch (IOException e) {
                        DriverStation.reportWarning("Failed to log data.", false);
                    }
                }
            }
            else {
                if (DriverStation.isDSAttached() || DriverStation.isFMSAttached()) {
                    String logPath = logFolder + new SimpleDateFormat(logTitle).format(new Date());
                    try {
                        logTimer.reset();
                        logTimer.start();
                        new File(logPath).createNewFile();
                        logger = new FileWriter(logPath);
                        logger.write(new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(new Date()) + "\n");
                        logger.write(DriverStation.getAlliance().name() + "\n");
                        logger.write(basicLogValues + "," + detailedLogValues + ",Driver," + xboxLogValues + ",Operator," + xboxLogValues + "\n");
                        logger.flush();
                        System.out.println("Successfully opened log file '" + logPath + "'");
                    } catch (IOException e) {
                        DriverStation.reportWarning("Failed to open log file '" + logPath + "'", false);
                    }
                }
            }
        }
    }

    private static void logBasicData(FileWriter writer, boolean flush) throws IOException {
        writer.write(String.format("%.4f", Timer.getFPGATimestamp()) + ",");
        writer.write(DriverStation.isEnabled() ? "1," : "0,");
        writer.write(String.format("%.2f", drive.getPose().getX()) + ",");
        writer.write(String.format("%.2f", drive.getPose().getY()) + ",");
        writer.write(String.format("%.2f", drive.getPose().getRotation().getDegrees()) + ",");
        writer.write(String.format("%.1f", drive.getThrottle()) + ",");
        writer.write(shooter.isAtSpeed() ? "1," : "0,");
        writer.write(String.format("%o",indexer.getBallCount()) + ",");
        writer.write(limelight.hasTarget() ? "1" : "0");
        writer.write(flush ? "\n" : ",");
        if (flush) {
            writer.flush();
        }
    }

    private static void logDetailedData(FileWriter writer, boolean flush) throws IOException {
        writer.write(String.format("%.2f",limelight.getDistance()) + ",");
        writer.write(String.format("%.2f",limelight.getHorizontal()) + ",");
        writer.write(String.format("%.2f",limelight.getVertical()) + ",");
        writer.write(shooter.getSpeed() + ",");
        writer.write(shooter.getSetSpeed() + ",");
        writer.write(indexer.getState() + ",");
        writer.write(indexer.getBall0().getColor() + ",");
        writer.write(indexer.getBall0().getLocation() + ",");
        writer.write(indexer.getBall1().getColor() + ",");
        writer.write(indexer.getBall1().getLocation() + ",");
        writer.write(compressor.isEnabled() ? "1," : "0,");
        writer.write(intake.isExtended() ? "1," : "0,");
        writer.write(climber.getEncoder() + ",");
        writer.write(climber.forwardAtHardLimit() ? "1," : "0,");
        writer.write(climber.reverseAtHardLimit() ? "1," : "0,");
        writer.write(drive.getAngle() + ",");
        writer.write(RobotController.getBatteryVoltage() + ",");
        writer.write(String.format("%.2f",RobotController.getCANStatus().percentBusUtilization));
        writer.write(flush ? "\n" : ",");
        if (flush) {
            writer.flush();
        }
    }

    private static void logXboxController(FileWriter writer, Humans human, boolean flush) throws IOException {
        BetterXboxController controller = BetterXboxController.getController(human);
        writer.write(controller.isConnected() ? "1," : "0,");
        writer.write(String.format("%.2f",controller.getLeftX()) + ",");
        writer.write(String.format("%.2f",controller.getLeftY()) + ",");
        writer.write(String.format("%.2f",controller.getRightX()) + ",");
        writer.write(String.format("%.2f",controller.getRightY()) + ",");
        writer.write(controller.getRightStickButton() ? "1," : "0,");
        writer.write(controller.getLeftStickButton() ? "1," : "0,");
        writer.write(controller.getAButton() ? "1," : "0,");
        writer.write(controller.getBButton() ? "1," : "0,");
        writer.write(controller.getXButton() ? "1," : "0,");
        writer.write(controller.getYButton() ? "1," : "0,");
        writer.write(controller.getStartButton() ? "1," : "0,");
        writer.write(controller.getBackButton() ? "1," : "0,");
        writer.write(controller.getRightBumper() ? "1," : "0,");
        writer.write(controller.getLeftBumper() ? "1," : "0,");
        writer.write(controller.getRightTriggerAxis() > 0.5 ? "1," : "0,");
        writer.write(controller.getLeftTriggerAxis() > 0.5 ? "1," : "0,");
        writer.write(controller.getPOV());
        writer.write(flush ? "\n" : ",");
        if (flush) {
            writer.flush();
        }
    }

    public static void enable() {
        loggerEnabled = true;
    }

    public static void disable() {
        loggerEnabled = false;
    }
}