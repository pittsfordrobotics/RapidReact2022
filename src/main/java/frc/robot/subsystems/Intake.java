package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LazySparkMax;
import static frc.robot.Constants.*;

public class Intake extends SubsystemBase {
    private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, INTAKE_PNEUMATIC_FORWARD, INTAKE_PNEUMATIC_REVERSE);
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
        SmartDashboard.putBoolean("Intake Extended", solenoid.get() == DoubleSolenoid.Value.kForward);
    }

    public void extend() {
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
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
        return solenoid.get();
    }

    public void toggleIntake() {
        solenoid.toggle();
    }
}