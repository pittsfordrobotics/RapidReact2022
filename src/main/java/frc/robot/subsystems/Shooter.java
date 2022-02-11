package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;
import frc.robot.util.LazySparkMax.Motor;

public class Shooter extends SubsystemBase {
    private final LazySparkMax shooterMotor = new LazySparkMax(Constants.SHOOTER_CAN_MAIN, IdleMode.kCoast, Motor.NEO_MAX,false);

    private final static Shooter INSTANCE = new Shooter();

    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter() {
    }

    public void shooterOn() {
        shooterMotor.set(0.7);
    }
    public void shooterOff() {
        shooterMotor.stopMotor();
    }

}