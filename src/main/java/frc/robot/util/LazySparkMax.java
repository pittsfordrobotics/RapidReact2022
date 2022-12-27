package frc.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.util.Alert.AlertType;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This is a thick wrapper for CANSparkMax because I am lazy
 */
public class LazySparkMax extends CANSparkMax {
    private int errors = 1;
    private int attempts = -1;
    private static final ArrayList<LazySparkMax> sparkMaxes = new ArrayList<LazySparkMax>();

    /**
     * Lazy Spark Max
     * @param port port of CAN ID of CANSparkMax
     * @param mode Brake or Coast
     * @param currentLimit 0-30 for 550 / 0-80 for NEO
     * @param inverted Inverted?
     */
    public LazySparkMax(int port, IdleMode mode, int currentLimit, boolean inverted) {
        super(port, MotorType.kBrushless);
        while (errors > 0 && ++attempts <= 5) {
            if (attempts > 0) {
                DriverStation.reportWarning("SparkMax " + port + "FAILED to initialize. Reinitializing attempt " + attempts, false);
                Logger.getInstance().recordOutput("SparkMaxes/" + Constants.ROBOT_SPARKMAX_HASHMAP.get(port) + port, "Reinitializing attempt " + attempts);
            }
            errors = 0;
            errors += check(restoreFactoryDefaults());
            setInverted(inverted);
            errors += check(setIdleMode(mode));
            errors += check(enableVoltageCompensation(12));
            errors += check(getEncoder().setPosition(0));
            errors += check(setSmartCurrentLimit(currentLimit));
            errors += check(burnFlash());
        }
        if (errors > 0) {
            Logger.getInstance().recordOutput("SparkMaxes/" + Constants.ROBOT_SPARKMAX_HASHMAP.get(port) + port, "INITIALIZE_ERROR");
            new Alert("SparkMaxes",Constants.ROBOT_SPARKMAX_HASHMAP.get(port) + " FAILED to initialize (" + port + ")!", AlertType.ERROR).set(true);
        }
        else {
            checkUpdated();
            sparkMaxes.add(this);
        }
    }

    /**
     * Lazy Spark Max not inverted
     * @param port port of CAN ID of CANSparkMax
     * @param mode Brake or Coast
     * @param currentLimit 0-30 for 550 / 0-80 for NEO
     */
    public LazySparkMax(int port, IdleMode mode, int currentLimit) {
        this(port, mode, currentLimit, false);
    }

    /**
     * Lazy Spark Max
     * @param port port of CAN ID of CANSparkMax
     * @param mode Brake or Coast
     * @param currentLimit 0-30 for 550 / 0-80 for NEO
     * @param leader SparkMax to follow
     * @param inverted Whether to follow the leader inverted
     */
    public LazySparkMax(int port, IdleMode mode, int currentLimit, CANSparkMax leader, boolean inverted) {
        super(port, MotorType.kBrushless);
        while (errors > 0 && ++attempts <= 5) {
            if (attempts > 0) {
                DriverStation.reportWarning("SparkMax " + port + "FAILED to initialize. Reinitializing attempt " + attempts, false);
                Logger.getInstance().recordOutput("SparkMaxes/" + Constants.ROBOT_SPARKMAX_HASHMAP.get(port) + port, "Reinitializing attempt " + attempts);
            }
            errors = 0;
            errors += check(restoreFactoryDefaults());
            errors += check(setIdleMode(mode));
            errors += check(enableVoltageCompensation(12));
            errors += check(getEncoder().setPosition(0));
            errors += check(setSmartCurrentLimit(currentLimit));
            errors += check(follow(leader, inverted));
            errors += check(burnFlash());
        }
        if (errors > 0) {
            Logger.getInstance().recordOutput("SparkMaxes/" + Constants.ROBOT_SPARKMAX_HASHMAP.get(port) + port, "INITIALIZE_ERROR");
            new Alert("SparkMaxes",Constants.ROBOT_SPARKMAX_HASHMAP.get(port) + " FAILED to initialize (" + port + ")!", AlertType.ERROR).set(true);
        }
        else {
            checkUpdated();
            sparkMaxes.add(this);
        }
    }

    /**
     * Lazy Spark Max
     * @param port port of CAN ID of CANSparkMax
     * @param mode Brake or Coast
     * @param currentLimit 0-30 for 550 / 0-80 for NEO
     * @param leader SparkMax to follow
     */
    public LazySparkMax(int port, IdleMode mode, int currentLimit, CANSparkMax leader) {
        this(port, mode, currentLimit, leader, false);
    }

    public static void checkAlive() {
        for (LazySparkMax i : sparkMaxes) {
            if (LazySparkMax.check(i.getLastError()) != 0) {
                Logger.getInstance().recordOutput("SparkMaxes/" + Constants.ROBOT_SPARKMAX_HASHMAP.get(i.getDeviceId()) + i.getDeviceId(), "ERROR");
                new Alert("SparkMaxes", Constants.ROBOT_SPARKMAX_HASHMAP.get(i.getDeviceId()) + " PROBLEMED (" + i.getDeviceId() + ")!", AlertType.ERROR).set(true);
            }
        }
    }

    private void checkUpdated() {
        if (!Objects.equals(getFirmwareString(), Constants.ROBOT_SPARK_MAX_FIRMWARE)) {
            Logger.getInstance().recordOutput("SparkMaxes/" + Constants.ROBOT_SPARKMAX_HASHMAP.get(getDeviceId()) + getDeviceId(), "OUT_OF_DATE");
            new Alert("SparkMaxes",Constants.ROBOT_SPARKMAX_HASHMAP.get(getDeviceId()) + " needs to be updated (" + getDeviceId() + ")!", AlertType.INFO).set(true);
        }
    }

    /**
     * Used for checking RevLib functions
     * @return 1 for error, 0 for no error
     */
    private static int check(REVLibError err) {
        return err == REVLibError.kOk ? 0 : 1;
    }
}