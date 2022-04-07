package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BetterLogger {
    private static final String logFolder = "/media/sda2/";
    private static final String logTitle = "'BetterLogger'_MM-dd-yyyy_HH:mm:ss'.json'";
    private static String logPath;

    private static final String basicLogValues = "Timestamp,Enabled,X,Y,Rotation,Throttle,Shooter Armed,Indexer Balls,Target Detected";
    private static final Timer logTimer = new Timer();
    private static final double logRate = 0.1;

    private static boolean loggerEnabled = false;
    private static boolean fileMade = false;

    private static FileWriter fileWriter;
    private static final JSONObject jsonWriter = new JSONObject();
    private static final JSONObject jsonArray = new JSONObject();

    private static final HashMap<String, Supplier> supplierHelper = new HashMap<>();

    private static final Alert fileError = new Alert("Failed to open log file. Logging will NOT work!", Alert.AlertType.WARNING);
    private static final Alert loggingError = new Alert("There was an error while writing logging data! Logging will NOT work!", Alert.AlertType.WARNING);

    public static void createFile() {
        if (!fileMade) {
            logPath = logFolder + new SimpleDateFormat(logTitle).format(new Date());
            try {
                logTimer.reset();
                logTimer.start();
                fileWriter = new FileWriter(logPath);
                JSONObject jsonInner = new JSONObject();
                jsonInner.put("date", new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(new Date()));
                jsonInner.put("match", DriverStation.getMatchNumber());
                jsonInner.put("alliance", DriverStation.getAlliance().name());
                jsonWriter.put("setup", jsonInner);
                fileWriter.write(jsonWriter.toJSONString());
                fileWriter.flush();
                fileWriter.close();
                fileMade = true;
                fileError.set(false);
                System.out.println("Successfully opened log file '" + logPath + "'");
            } catch (IOException e) {
                fileError.set(true);
            }
        }
    }

    public static void enable() {
        loggerEnabled = true;
    }

    public static void disable() {
        loggerEnabled = false;
    }

    public static void logPeriodic() {
        if (loggerEnabled) {
            try {
                fileWriter = new FileWriter(logPath);
                JSONObject jsonInner = new JSONObject();
                jsonInner.put("Enabled", DriverStation.isEnabled() ? 1 : 0);
                jsonInner.put("Battery", RobotController.getBatteryVoltage());
                jsonInner.put("Browned Out", RobotController.isBrownedOut() ? 1 : 0);
                jsonInner.put("CANUtil", RobotController.getCANStatus().percentBusUtilization);
                for (Map.Entry<String, Supplier> data : supplierHelper.entrySet()) {
                    try {
                        jsonInner.put(data.getKey(), String.format("%.4f", data.getValue().get()));
                    } catch (Exception e) {
                        jsonInner.put(data.getKey(), data.getValue().get());
                    }
                }
                jsonWriter.put(String.format("%.4f", Timer.getFPGATimestamp()), jsonArray);
                fileWriter.write(jsonWriter.toString());
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                loggingError.set(true);
            }
        }
    }

    public static void logString(String key, Supplier<String> valueSupplier) {
        supplierHelper.put(key, valueSupplier);
    }

    public static void logNumber(String key, Supplier<Double> valueSupplier) {
        supplierHelper.put(key, valueSupplier);
    }

    public static void logBoolean(String key, Supplier<Boolean> valueSupplier) {
        supplierHelper.put(key, valueSupplier);
    }

    public static void logWestCoastDrive(Supplier<Double> x, Supplier<Double> y, Supplier<Double> rot, Supplier<Double> throttle) {
        supplierHelper.put("Drive/X", x);
        supplierHelper.put("Drive/Y", y);
        supplierHelper.put("Drive/Rotation", rot);
        supplierHelper.put("Drive/Throttle", throttle);
    }

    public static void logFlywheel(Supplier<Boolean> armed, Supplier<Double> setSpeed, Supplier<Double> realSpeed) {
        supplierHelper.put("Shooter/Armed", armed);
        supplierHelper.put("Shooter/Set Speed", setSpeed);
        supplierHelper.put("Shooter/Real Speed", realSpeed);
    }

    public static void logLimelight(Supplier<Double> hasTarget, Supplier<Double> tx, Supplier<Double> ty, Supplier<Double> distance, Supplier<Integer> LEDStatus, Supplier<Integer> pipeline, Supplier<Integer> cameraMode) {
        supplierHelper.put("Limelight/Has Target", hasTarget);
        supplierHelper.put("Limelight/Horizontal Angle", tx);
        supplierHelper.put("Limelight/Vertical Angle", ty);
        supplierHelper.put("Limelight/LED Status", LEDStatus);
        supplierHelper.put("Limelight/Pipeline", pipeline);
        supplierHelper.put("Limelight/Camera Mode", cameraMode);
    }

}