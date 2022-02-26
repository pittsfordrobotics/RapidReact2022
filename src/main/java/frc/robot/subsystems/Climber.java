package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class Climber extends SubsystemBase {
    private final LazySparkMax leftMotor = new LazySparkMax(Constants.CLIMBER_CAN_LEFT, IdleMode.kBrake, 50, true);
    private final LazySparkMax rightMotor = new LazySparkMax(Constants.CLIMBER_CAN_RIGHT, IdleMode.kBrake, 50);

    private final SparkMaxLimitSwitch leftForwardSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch leftReverseSwitch = leftMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch rightForwardSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch rightReverseSwitch = rightMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

    private final DigitalInput leftSensor = new DigitalInput(Constants.CLIMBER_SENSOR_LEFT);
    private final DigitalInput rightSensor = new DigitalInput(Constants.CLIMBER_SENSOR_RIGHT);

    private final RelativeEncoder leftEncoder = leftMotor.getEncoder();
    private final RelativeEncoder rightEncoder = rightMotor.getEncoder();

    private double halfway = 0;

    private final ShuffleboardTab climberTab = Shuffleboard.getTab("Climber");

    private final static Climber INSTANCE = new Climber();
    public static Climber getInstance() {
        return INSTANCE;
    }

    private Climber() {
        resetEncoders();

        leftForwardSwitch.enableLimitSwitch(true);
        leftReverseSwitch.enableLimitSwitch(true);
        rightForwardSwitch.enableLimitSwitch(true);
        rightReverseSwitch.enableLimitSwitch(true);

        rightMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 90);
        rightMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, -90);
        leftMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 90);
        leftMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, -90);

        climberTab.addNumber("Right Encoder", rightEncoder::getPosition);
        climberTab.addBoolean("Right Front Limit Switch", rightForwardSwitch::isPressed);
        climberTab.addBoolean("Right Reverse Limit Switch", rightReverseSwitch::isPressed);
        climberTab.addBoolean("Left Front Limit Switch", leftForwardSwitch::isPressed);
        climberTab.addBoolean("Left Reverse Limit Switch", leftReverseSwitch::isPressed);
    }

    public boolean getRightSensor() {
        return !rightSensor.get();
    }

    public boolean getLeftSensor() {
        return !leftSensor.get();
    }

    public void setSpeed(double speed) {
        leftMotor.set(speed);
        rightMotor.set(speed);
    }

    public void setRightMotor(double speed) {
        rightMotor.set(speed);
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public void enableSoftLimit() {
        rightMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        rightMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
        leftMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        leftMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
    }

    public double getEncoder() {
        return rightEncoder.getPosition();
    }

    public void saveHalfway() {
        halfway = rightEncoder.getPosition()/2;
    }

    public boolean hasBeenCentered() {
        return rightEncoder.getPosition() >= halfway;
    }

    public boolean forwardAtLimit() {
//        return rightForwardSwitch.isPressed() && leftForwardSwitch.isPressed();
        return rightForwardSwitch.isPressed();
    }

    public boolean reverseAtLimit() {
//        return rightForwardSwitch.isPressed() && leftForwardSwitch.isPressed();
        return rightReverseSwitch.isPressed();
    }

    public void climbFront() {
        leftMotor.set(Constants.CLIMBER_SPEED);
        rightMotor.set(Constants.CLIMBER_SPEED);
    }

    public void climbBack() {
        leftMotor.set(-Constants.CLIMBER_SPEED);
        rightMotor.set(-Constants.CLIMBER_SPEED);
    }

    public void stopAll() {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }

}