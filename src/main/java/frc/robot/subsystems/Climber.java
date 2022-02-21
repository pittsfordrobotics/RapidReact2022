package frc.robot.subsystems;

/* 
* i hate paras
* he sucks
*/

import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

;

public class Climber extends SubsystemBase {
    private final LazySparkMax leftClimber = new LazySparkMax(Constants.CLIMBER_CAN_LEFT, IdleMode.kBrake, 50);
    private final LazySparkMax rightClimber = new LazySparkMax(Constants.CLIMBER_CAN_RIGHT, IdleMode.kBrake, 50);

    private final SparkMaxLimitSwitch leftForwardSwitch = leftClimber.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch leftReverseSwitch = leftClimber.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch rightForwardSwitch = rightClimber.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
    private final SparkMaxLimitSwitch rightReverseSwitch = rightClimber.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

    private final SparkMaxPIDController leftController = leftClimber.getPIDController();
    private final SparkMaxPIDController rightController = rightClimber.getPIDController();

    private final RelativeEncoder leftEncoder = leftClimber.getEncoder();
    private final RelativeEncoder rightEncoder = rightClimber.getEncoder();

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

//        leftController.setP(0.1);
//        rightController.setP(0.1);

        leftEncoder.setPositionConversionFactor(1/Constants.CLIMBER_GEAR_RATIO);
        rightEncoder.setPositionConversionFactor(1/Constants.CLIMBER_GEAR_RATIO);
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public void zero() {
        resetEncoders();
        leftController.setReference(Constants.CLIMBER_ROTATIONS_FRONT_TO_CENTER, ControlType.kPosition);
        rightController.setReference(Constants.CLIMBER_ROTATIONS_FRONT_TO_CENTER, ControlType.kPosition);
    }

    public boolean hasBeenZeroed() {
        return MathUtil.applyDeadband(leftEncoder.getPosition() - Constants.CLIMBER_ROTATIONS_FRONT_TO_CENTER, 1) == 0 && MathUtil.applyDeadband(rightEncoder.getPosition() - Constants.CLIMBER_ROTATIONS_FRONT_TO_CENTER, 1) == 0;
    }

    public void resetPID() {
        leftController.setReference(0, ControlType.kPosition);
        rightController.setReference(0, ControlType.kPosition);
    }

    public boolean forwardAtLimit() {
        return rightForwardSwitch.isPressed() && leftForwardSwitch.isPressed();
    }

    public void climbFront() {
        leftClimber.set(Constants.CLIMBER_SPEED);
        rightClimber.set(Constants.CLIMBER_SPEED);
    }

    public void climbBack() {
        leftClimber.set(-Constants.CLIMBER_SPEED);
        rightClimber.set(-Constants.CLIMBER_SPEED);
    }

    public void stopAll() {
        leftClimber.stopMotor();
        rightClimber.stopMotor();
    }

    public void stopRight() {
        rightClimber.stopMotor();
    }

    public void stopLeft() {
        leftClimber.stopMotor();
    }

}