package frc.robot.util;

import com.revrobotics.CANSparkMax;

public class LazySparkMax extends CANSparkMax {

    public LazySparkMax(int port, IdleMode mode, boolean inverted) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setInverted(inverted);
        setIdleMode(mode);
        getEncoder().setPosition(0);
    }

    public LazySparkMax(int port, IdleMode mode) {
        this(port, mode, false);
    }

    public LazySparkMax(int port, IdleMode mode, boolean inverted, CANSparkMax leader) {
        this(port, mode);
        follow(leader, inverted);
    }

    public LazySparkMax(int port, IdleMode mode, CANSparkMax leader) {
        this(port, mode, false, leader);
    }
}