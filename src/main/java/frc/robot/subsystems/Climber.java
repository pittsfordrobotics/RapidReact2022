package frc.robot.subsystems;

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

        leftEncoder.setPositionConversionFactor(1/Constants.CLIMBER_GEAR_RATIO);
        rightEncoder.setPositionConversionFactor(1/Constants.CLIMBER_GEAR_RATIO);
    }

    @Override
    public void periodic() {
        climberTab.add("Right Front Limit Switch", rightForwardSwitch.isPressed());
        climberTab.add("Right Reverse Limit Switch", rightReverseSwitch.isPressed());
        climberTab.add("Left Front Limit Switch", leftForwardSwitch.isPressed());
        climberTab.add("Left Reverse Limit Switch", leftReverseSwitch.isPressed());
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

    public boolean hasBeenCentered() {
        return Math.abs(leftEncoder.getPosition() - Constants.CLIMBER_ROTATIONS_FRONT_TO_CENTER) < 0.02;
    }

    public boolean forwardAtLimit() {
        return rightForwardSwitch.isPressed() && leftForwardSwitch.isPressed();
    }

    public boolean reverseAtLimit() {
        return rightForwardSwitch.isPressed() && leftForwardSwitch.isPressed();
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