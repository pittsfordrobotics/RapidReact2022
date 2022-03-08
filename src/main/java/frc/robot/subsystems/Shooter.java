package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class Shooter extends SubsystemBase {
    private final LazySparkMax motorLeft = new LazySparkMax(Constants.SHOOTER_CAN_LEFT, IdleMode.kCoast, 60, false);
    private final LazySparkMax motorRight = new LazySparkMax(Constants.SHOOTER_CAN_RIGHT, IdleMode.kCoast, 60, true, motorLeft);
    private final RelativeEncoder shooterEncoder = motorLeft.getEncoder();

    private final SimpleMotorFeedforward shooterFeedforward = new SimpleMotorFeedforward(Constants.SHOOTER_STATIC_GAIN, Constants.SHOOTER_VELOCITY_GAIN, Constants.SHOOTER_ACCELERATION_GAIN);
    private final BangBangController bangBangController = new BangBangController();

    private double speed = 0;

    private final ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");

    private final static Shooter INSTANCE = new Shooter();
    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter() {
        SmartDashboard.putNumber("Shooter Guess Speed", 0);
        shooterTab.addNumber("Shooter Target RPM", () -> speed);
        shooterTab.addNumber("Shooter Actual", shooterEncoder::getVelocity);
        shooterTab.addBoolean("Shooter up to Speed", this::isAtSpeed);
    }

    @Override
    public void periodic() {
    }

    public void setSmartDashboard() {
        this.speed = SmartDashboard.getNumber("Shooter Guess Speed", 0);
        setSpeed(speed);
    }

    public void setDumbSpeed() {
        this.speed = 0.8;
        motorLeft.set(speed);
    }

    public void dumbOff() {
        motorLeft.stopMotor();
    }

    public void setSpeed(double speed) {
        motorLeft.set(bangBangController.calculate(shooterEncoder.getVelocity(), speed) + 0.9 * shooterFeedforward.calculate(speed));
        this.speed = speed;
    }

    public void shootLow() {
        setSpeed(Constants.SHOOTER_LOW_SPEED);
        this.speed = Constants.SHOOTER_LOW_SPEED;
    }

    public void shootHighFender() {
        setSpeed(Constants.SHOOTER_LOW_SPEED);
        this.speed = Constants.SHOOTER_LOW_SPEED;
    }

    public void shootStop() {
        motorLeft.set(bangBangController.calculate(shooterEncoder.getVelocity(), 0));
        this.speed = 0;
    }

    public boolean isAtSpeed() {
        return MathUtil.applyDeadband(shooterEncoder.getVelocity() - speed, 0.2) == 0;
    }

}