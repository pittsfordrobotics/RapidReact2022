package frc.robot.util;

import com.revrobotics.CANSparkMax;

public class LazySparkMax extends CANSparkMax {

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
        this(port, mode, currentLimit, false);
        follow(leader, inverted);
    }

    public LazySparkMax(int port, IdleMode mode, int currentLimit, CANSparkMax leader) {
        this(port, mode, currentLimit,false, leader);
    }
}