package frc.robot.util;

import com.revrobotics.CANSparkMax;

public class LazySparkMax extends CANSparkMax {

    /**
     * Makes a new LazySparkMax and helps configure a lot of default settings
     * @param port CAN port
     * @param mode Brake or coast mode
     * @param currentLimit NEO range 0-60 amps / 550 range 0-40 amps
     * @param inverted Inverted or not
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

    /**
     * Makes a new LazySparkMax and helps configure a lot of default settings
     * @param port CAN port
     * @param mode Brake or coast mode
     * @param currentLimit NEO range 0-60 amps / 550 range 0-40 amps
     */
    public LazySparkMax(int port, IdleMode mode, int currentLimit) {
        this(port, mode, currentLimit, false);
    }


    /**
     * Makes a new follower LazySparkMax and helps configure a lot of default settings
     * @param port CAN port
     * @param mode Brake or coast mode
     * @param currentLimit NEO range 0-60 amps / 550 range 0-40 amps
     * @param inverted Inverted or not
     * @param leader Leader CANSparkMax or LazySparkMax
     */
    public LazySparkMax(int port, IdleMode mode, int currentLimit, boolean inverted, CANSparkMax leader) {
        this(port, mode, currentLimit, false);
        follow(leader, inverted);
    }

    /**
     * Makes a new follower LazySparkMax and helps configure a lot of default settings
     * @param port CAN port
     * @param mode Brake or coast mode
     * @param currentLimit NEO range 0-60 amps / 550 range 0-40 amps
     * @param leader Leader CANSparkMax or LazySparkMax
     */
    public LazySparkMax(int port, IdleMode mode, int currentLimit, CANSparkMax leader) {
        this(port, mode, currentLimit,false, leader);
    }
}