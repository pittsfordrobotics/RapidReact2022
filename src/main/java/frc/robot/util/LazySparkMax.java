package frc.robot.util;

import com.revrobotics.CANSparkMax;

public class LazySparkMax extends CANSparkMax {

    public enum Motor {
        NEO_MIN(40), NEO(50), NEO_MAX(60), NEO_550_MIN(20), NEO_550(30), NEO_550_MAX(40);

        private final int currentLimit;

        Motor(int currentLimit) {
            this.currentLimit = currentLimit;
        }

        public int getCurrentLimit() {
            return currentLimit;
        }
    }

    public LazySparkMax(int port, IdleMode mode, Motor motor, boolean inverted) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setInverted(inverted);
        setIdleMode(mode);
        getEncoder().setPosition(0);
        setSmartCurrentLimit(motor.getCurrentLimit());
        burnFlash();
    }

    public LazySparkMax(int port, IdleMode mode, Motor motor) {
        this(port, mode, motor, false);
    }

    public LazySparkMax(int port, IdleMode mode, Motor motor, boolean inverted, CANSparkMax leader) {
        super(port, MotorType.kBrushless);
        restoreFactoryDefaults();
        setInverted(inverted);
        setIdleMode(mode);
        getEncoder().setPosition(0);
        setSmartCurrentLimit(motor.getCurrentLimit());
        follow(leader, inverted);
        burnFlash();
    }

    public LazySparkMax(int port, IdleMode mode, Motor motor, CANSparkMax leader) {
        this(port, mode, motor,false, leader);
    }
}