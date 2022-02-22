package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

// TODO: add trajectory for auto climb and also line follower sensors
public class Climber extends SubsystemBase {
    private final LazySparkMax leftMotor = new LazySparkMax(Constants.CLIMBER_CAN_LEFT, IdleMode.kBrake, 50);
    private final LazySparkMax rightMotor = new LazySparkMax(Constants.CLIMBER_CAN_RIGHT, IdleMode.kBrake, 50);

    private final SparkMaxLimitSwitch leftForwardSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch leftReverseSwitch = leftMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch rightForwardSwitch = rightMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch rightReverseSwitch = rightMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

    private final RelativeEncoder leftEncoder = leftMotor.getEncoder();
    private final RelativeEncoder rightEncoder = rightMotor.getEncoder();

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

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public boolean hasBeenZeroed() {
        return MathUtil.applyDeadband(leftEncoder.getPosition() - Constants.CLIMBER_ROTATIONS_FRONT_TO_CENTER, 1) == 0 || MathUtil.applyDeadband(rightEncoder.getPosition() - Constants.CLIMBER_ROTATIONS_FRONT_TO_CENTER, 1) == 0;
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