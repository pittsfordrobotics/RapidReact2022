package frc.robot.util;

import com.revrobotics.CANSparkMax;

public class LazySparkMax extends CANSparkMax {
    public LazySparkMax(int port, IdleMode mode) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setInverted(false);
        setIdleMode(mode);
        getEncoder().setPosition(0);
    }

    public LazySparkMax(int port, IdleMode mode, boolean inverted) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setInverted(inverted);
        setIdleMode(mode);
        getEncoder().setPosition(0);
    }

    public LazySparkMax(int port, IdleMode mode, CANSparkMax leader) {
        this(port, mode);
        follow(leader);
    }

    public LazySparkMax(int port, IdleMode mode, boolean inverted, CANSparkMax leader) {
        this(port, mode, inverted);
        follow(leader);
    }
}