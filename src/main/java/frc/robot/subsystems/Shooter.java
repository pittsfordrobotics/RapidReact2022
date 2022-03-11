package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
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

    private final static Shooter INSTANCE = new Shooter();
    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter() {
        ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
        shooterTab.addNumber("Shooter Target RPM", () -> speed);
        shooterTab.addNumber("Shooter Actual", shooterEncoder::getVelocity);
        shooterTab.addBoolean("Shooter up to Speed", this::isAtSpeed);
    }

    @Override
    public void periodic() {
//        motorLeft.set(bangBangController.calculate(shooterEncoder.getVelocity(), speed) + 0.0002 * speed);
        motorLeft.set(0.0002 * speed);
    }

    public void setDumbSpeed() {
        this.speed = 0.4;
        motorLeft.set(speed);
    }

    public void dumbOff() {
        motorLeft.stopMotor();
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void shootLow() {
        this.speed = Constants.SHOOTER_LOW_SPEED;
    }

    public void shootStop() {
        this.speed = 0;
    }

    public boolean isAtSpeed() {
        return shooterEncoder.getVelocity() > 0.9 * speed;
    }

}