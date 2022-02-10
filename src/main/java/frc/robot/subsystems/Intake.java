package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LazySparkMax;
import static frc.robot.Constants.Intake.*;

public class Intake extends SubsystemBase {
    private final DoubleSolenoid solenoidLeft = new DoubleSolenoid(PneumaticsModuleType.REVPH, INTAKE_PNEUMATIC_LEFT_FORWARD, INTAKE_PNEUMATIC_LEFT_REVERSE);
    private final DoubleSolenoid solenoidRight = new DoubleSolenoid(PneumaticsModuleType.REVPH, INTAKE_PNEUMATIC_RIGHT_FORWARD, INTAKE_PNEUMATIC_RIGHT_REVERSE);
    private final LazySparkMax intake = new LazySparkMax(INTAKE_CAN_MAIN, CANSparkMax.IdleMode.kBrake);

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

    public void intakeOn() {
        intake.set(INTAKE_MAIN_SPEED);
    }

    public void intakeOff() {
        intake.stopMotor();
    }

    public void reverseIntake() {
        intake.set(-INTAKE_MAIN_SPEED);
    }

    public DoubleSolenoid.Value getState() {
        return solenoidLeft.get();
    }

    public void toggleIntake() {
        solenoidLeft.toggle();
        solenoidRight.toggle();
    }
}