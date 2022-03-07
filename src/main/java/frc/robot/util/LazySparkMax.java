package frc.robot.util;

import com.revrobotics.CANSparkMax;

/**
 * This is a thick wrapper for CANSparkMax because I am lazy, that reduces code
 */
public class LazySparkMax extends CANSparkMax {

    /**
     * Lazy Spark Max
     * @param port port of CAN ID of CANSparkMax
     * @param mode Brake or Coast
     * @param currentLimit 0-30 for 550 / 0-80 for NEO
     * @param inverted Inverted?
     */
    public LazySparkMax(int port, IdleMode mode, int currentLimit, boolean inverted) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setInverted(inverted);
        setIdleMode(mode);
        getEncoder().setPosition(0);
        setSmartCurrentLimit(currentLimit);
        burnFlash();
    }

    public LazySparkMax(int port, IdleMode mode, int currentLimit) {
        this(port, mode, currentLimit, false);
    }

    public LazySparkMax(int port, IdleMode mode, int currentLimit, boolean inverted, CANSparkMax leader) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setIdleMode(mode);
        getEncoder().setPosition(0);
        setSmartCurrentLimit(currentLimit);
        follow(leader, inverted);
        burnFlash();
    }

    public LazySparkMax(int port, IdleMode mode, int currentLimit, CANSparkMax leader) {
        this(port, mode, currentLimit,false, leader);
    }
}