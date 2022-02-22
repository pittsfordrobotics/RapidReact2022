package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class Shooter extends SubsystemBase {
    private final LazySparkMax shooterMotor = new LazySparkMax(Constants.SHOOTER_CAN_MAIN, IdleMode.kCoast, 60,false);
    private final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();

    private final SimpleMotorFeedforward shooterFeedforward = new SimpleMotorFeedforward(Constants.SHOOTER_STATIC_GAIN, Constants.SHOOTER_VELOCITY_GAIN, Constants.SHOOTER_ACCELERATION_GAIN);
    private final BangBangController bangBangController = new BangBangController();

    private double speed = 0;

    private final static Shooter INSTANCE = new Shooter();
    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter() {}

    public void setShooterSpeed(double speed) {
        shooterMotor.set(bangBangController.calculate(shooterEncoder.getVelocity(), speed) + 0.9 * shooterFeedforward.calculate(speed));
        this.speed = speed;
    }

    public void shootLow() {
        setShooterSpeed(Constants.SHOOTER_LOW_SPEED);
        this.speed = Constants.SHOOTER_LOW_SPEED;
    }

    public void shooterOff() {
        shooterMotor.set(bangBangController.calculate(shooterEncoder.getVelocity(), 0));
        this.speed = 0;
    }

    public boolean isAtSpeed() {
        return MathUtil.applyDeadband(shooterEncoder.getVelocity() - speed, 0.2) == 0;
    }

}