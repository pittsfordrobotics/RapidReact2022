package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

//TODO: RE-ENABLE solenoids after they are wired
public class Intake extends SubsystemBase {
//    private final DoubleSolenoid solenoidLeft = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.INTAKE_PNEUMATIC_LEFT_FORWARD, Constants.INTAKE_PNEUMATIC_LEFT_REVERSE);
//    private final DoubleSolenoid solenoidRight = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.INTAKE_PNEUMATIC_RIGHT_FORWARD, Constants.INTAKE_PNEUMATIC_RIGHT_REVERSE);
    private final LazySparkMax motor = new LazySparkMax(Constants.INTAKE_CAN_MAIN, IdleMode.kBrake, 50);

    private DoubleSolenoid.Value state = DoubleSolenoid.Value.kReverse;

    private final static Intake INSTANCE = new Intake();
    public static Intake getInstance() {
        return INSTANCE;
    }

    private Intake() {
        retract();
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Intake Extended", isExtended());
    }

    public void extend() {
//        solenoidLeft.set(DoubleSolenoid.Value.kForward);
//        solenoidRight.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
//        solenoidLeft.set(DoubleSolenoid.Value.kReverse);
//        solenoidRight.set(DoubleSolenoid.Value.kReverse);
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

    public boolean isExtended() {
        return false;
//        return solenoidLeft.get() == DoubleSolenoid.Value.kForward;
    }

    public void toggleSolenoid() {
//        solenoidLeft.toggle();
//        solenoidRight.toggle();
    }
}