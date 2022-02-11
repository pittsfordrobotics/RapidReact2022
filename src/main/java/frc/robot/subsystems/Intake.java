package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LazySparkMax;
import frc.robot.util.LazySparkMax.Motor;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private final DoubleSolenoid solenoidLeft = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.INTAKE_PNEUMATIC_LEFT_FORWARD, Constants.INTAKE_PNEUMATIC_LEFT_REVERSE);
    private final DoubleSolenoid solenoidRight = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.INTAKE_PNEUMATIC_RIGHT_FORWARD, Constants.INTAKE_PNEUMATIC_RIGHT_REVERSE);
    private final LazySparkMax motor = new LazySparkMax(Constants.INTAKE_CAN_MAIN, IdleMode.kBrake, Motor.NEO);

    private final static Intake INSTANCE = new Intake();
    public static Intake getInstance() {
        return INSTANCE;
    }

    private Intake() {
        retract();
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Intake Extended", solenoidLeft.get() == DoubleSolenoid.Value.kForward);
    }

    public void extend() {
        solenoidLeft.set(DoubleSolenoid.Value.kForward);
        solenoidRight.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
        solenoidLeft.set(DoubleSolenoid.Value.kReverse);
        solenoidRight.set(DoubleSolenoid.Value.kReverse);
    }

    public void motorOn() {
        motor.set(Constants.INTAKE_MAIN_SPEED);
    }

    public void motorOff() {
        motor.stopMotor();
    }

    public void motorReverse() {
        motor.set(-Constants.INTAKE_MAIN_SPEED);
    }

    public DoubleSolenoid.Value getState() {
        return solenoidLeft.get();
    }

    public void toggleIntake() {
        solenoidLeft.toggle();
        solenoidRight.toggle();
    }
}