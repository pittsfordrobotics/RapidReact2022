package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.BangBangController;
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
        SmartDashboard.putNumber("feedforward", 0.0003);
        SmartDashboard.putNumber("speed", 0);
    }

    @Override
    public void periodic() {
//        TODO: enable bang bang
//        motorLeft.set(bangBangController.calculate(shooterEncoder.getVelocity(), speed) + 0.0003 * speed);
//        motorLeft.set(bangBangController.calculate(shooterEncoder.getVelocity(), SmartDashboard.getNumber("speed", 0)) + SmartDashboard.getNumber("feedforward", 0) * SmartDashboard.getNumber("speed", 0));
        motorLeft.set(0.0002 * speed);
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
//        TODO: test new at speed
//        return MathUtil.applyDeadband(shooterEncoder.getVelocity() - speed, 100) == 0;
        return shooterEncoder.getVelocity() > 0.9 * speed;
    }

    public double getSpeed() {
        return shooterEncoder.getVelocity();
    }

    public double getSetSpeed() {
        return speed;
    }

}