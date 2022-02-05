package frc.robot.subsystems;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private DoubleSolenoid intakePneumatic = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.INTAKE_PNEUMATIC_FORWARD, Constants.INTAKE_PNEUMATIC_REVERSE);

    private final static Intake INSTANCE = new Intake();

    public static Intake getInstance() {
        return INSTANCE;
    }

    private Intake() {

    }

    public void extend() {
        intakePneumatic.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
        intakePneumatic.set(DoubleSolenoid.Value.kReverse);
    }
}