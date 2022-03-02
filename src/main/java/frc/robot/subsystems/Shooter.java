package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class Shooter extends SubsystemBase {
    private final LazySparkMax shooterMotor = new LazySparkMax(Constants.SHOOTER_CAN_MAIN, IdleMode.kCoast, 60,false);
    private final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();

    private final SimpleMotorFeedforward shooterFeedforward = new SimpleMotorFeedforward(Constants.SHOOTER_STATIC_GAIN, Constants.SHOOTER_VELOCITY_GAIN, Constants.SHOOTER_ACCELERATION_GAIN);
    private final BangBangController bangBangController = new BangBangController();

    private double speed = 0;

    private final ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");

    private final static Shooter INSTANCE = new Shooter();
    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter() {
        shooterTab.addNumber("Shooter Target RPM", () -> speed);
        shooterTab.addNumber("Shooter Actual", shooterEncoder::getVelocity);
        shooterTab.addBoolean("Shooter up to Speed", this::isAtSpeed);
    }

    @Override
    public void periodic() {
    }

    public void setSpeed(double speed) {
        shooterMotor.set(bangBangController.calculate(shooterEncoder.getVelocity(), speed) + 0.9 * shooterFeedforward.calculate(speed));
        this.speed = speed;
    }

    public void shootLow() {
        setSpeed(Constants.SHOOTER_LOW_SPEED);
        this.speed = Constants.SHOOTER_LOW_SPEED;
    }

    public void motorOff() {
        shooterMotor.set(bangBangController.calculate(shooterEncoder.getVelocity(), 0));
        this.speed = 0;
    }

    public boolean isAtSpeed() {
        return MathUtil.applyDeadband(shooterEncoder.getVelocity() - speed, 0.2) == 0;
    }

}