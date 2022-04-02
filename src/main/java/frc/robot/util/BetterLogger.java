package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BetterLogger {
    private final String logFolder = "/media/sda2/";
    private final String logTitle = "'TestLogger'_MM-dd-yyyy_HH:mm:ss'.csv'";
    private final String basicLogValues = "Timestamp,Enabled,X,Y,Rotation,Throttle,Shooter Armed,Indexer Balls,Target Detected";
    private final String detailedLogValues = "Target Distance,Target Horizontal,Target Vertical,Shooter Speed,Shooter Set Speed,Indexer State,Ball0 Color,Ball0 Location,Ball1 Color,Ball1 Location,Compressor State,Intake Extended,Climber Encoder,Climber Forward Limit,Climber Reverse Limit,Pigeon Angle,Battery Voltage,CAN Bus Utilization";
    private final String xboxLogValues = "Connected,Left X,Left Y,Right X,Right Y,Right Stick Button,Left Stick Button,A,B,X,Y,Start,Back,Right Bumper,Left Bumper,Right Trigger,Left Trigger,DPad";;
    private final Timer logTimer = new Timer();
    private final double logRate = 0.1;
    private boolean loggerEnabled = false;

    private FileWriter logger;

    private BetterLogger() {
        String logPath = logFolder + new SimpleDateFormat(logTitle).format(new Date());
        try {
            logTimer.reset();
            logTimer.start();
            System.out.println(new File(logPath).createNewFile() ? "Successfully created file" : "Failed to create file");
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

    private static final BetterLogger INSTANCE = new BetterLogger();
    public static BetterLogger getInstance() {
        return INSTANCE;
    }
}