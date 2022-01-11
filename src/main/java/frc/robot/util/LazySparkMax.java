package frc.robot.util;

import com.revrobotics.CANSparkMax;

public class LazySparkMax extends CANSparkMax {
    public LazySparkMax(int port, IdleMode mode) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setIdleMode(mode);
        getEncoder().setPosition(0);
    }

    public LazySparkMax(int port, IdleMode mode, CANSparkMax leader) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setIdleMode(mode);
        getEncoder().setPosition(0);
        follow(leader);
    }
}