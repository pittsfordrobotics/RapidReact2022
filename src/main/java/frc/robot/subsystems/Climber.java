package frc.robot.subsystems;

/* 
* i hate paras
* he sucks
*/

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;
import frc.robot.util.LazySparkMax.Motor;

public class Climber extends SubsystemBase {
    private final LazySparkMax leftClimber = new LazySparkMax(Constants.CLIMBER_CAN_LEFT, IdleMode.kBrake, Motor.NEO);
    private final LazySparkMax rightClimber = new LazySparkMax(Constants.CLIMBER_CAN_RIGHT, IdleMode.kBrake, Motor.NEO, leftClimber);
    private final RelativeEncoder leftEncoder = leftClimber.getEncoder();

    private final static Climber INSTANCE = new Climber();
    public static Climber getInstance() {
        return INSTANCE;
    }

    private Climber() {
        leftClimber.enableSoftLimit(SoftLimitDirection.kForward, true);
        leftClimber.enableSoftLimit(SoftLimitDirection.kReverse, true);
        leftClimber.setSoftLimit(SoftLimitDirection.kForward, Constants.CLIMBER_ROTATION_LIMIT_UP);
        leftClimber.setSoftLimit(SoftLimitDirection.kReverse, Constants.CLIMBER_ROTATION_LIMIT_DOWN);
    }

    public boolean atLimit() {
        return leftEncoder.getPosition() >= Constants.CLIMBER_ROTATION_LIMIT_UP || leftEncoder.getPosition() <= Constants.CLIMBER_ROTATION_LIMIT_DOWN;
    }

    public void climbFront() {
        leftClimber.set(Constants.CLIMBER_SPEED);
    }

    public void climbBack() {
        leftClimber.set(-Constants.CLIMBER_SPEED);
    }

    public void stop() {
        leftClimber.stopMotor();
    }

}