package frc.robot.subsystems;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

//TODO: add PID controller and constants
public class Hood extends SubsystemBase {
    private LazySparkMax hoodMotor = new LazySparkMax(Constants.HOOD_CAN_MAIN, IdleMode.kBrake, 40);
    private RelativeEncoder hoodEncoder = hoodMotor.getEncoder();
    private SparkMaxPIDController hoodPID = hoodMotor.getPIDController();

    private final static Hood INSTANCE = new Hood();
    public static Hood getInstance() {
        return INSTANCE;
    }

    private Hood() {
        hoodMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        hoodMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, Constants.HOOD_MAX_POSITION);

        hoodPID.setP(Constants.HOOD_POSITION_GAIN);
        hoodPID.setI(Constants.HOOD_INTEGRAL_GAIN);
        hoodPID.setD(Constants.HOOD_DERIVATIVE_GAIN);

        hoodEncoder.setPositionConversionFactor(Constants.HOOD_ROTATIONS_TO_DEGREES);
    }

    public void zero() {
        hoodEncoder.setPosition(0);
    }

    public void setPosition(double position) {
        hoodPID.setReference(position, CANSparkMax.ControlType.kPosition);
    }

}