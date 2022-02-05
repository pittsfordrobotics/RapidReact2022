package frc.robot.subsystems;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private DoubleSolenoid intakePneumatic = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.INTAKE_PNEUMATIC_FORWARD, Constants.INTAKE_PNEUMATIC_REVERSE);

    private final static Intake INSTANCE = new Intake();

    public enum STATE {
        RETRACTED, EXTENDED
    }

    private STATE state = STATE.RETRACTED;

    public static Intake getInstance() {
        return INSTANCE;
    }

    private Intake() {
        retract();
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Intake State", state.toString());
    }

    public void extend() {
        intakePneumatic.set(DoubleSolenoid.Value.kForward);
        state = STATE.EXTENDED;
    }

    public void retract() {
        intakePneumatic.set(DoubleSolenoid.Value.kReverse);
        state = STATE.RETRACTED;
    }

    public void toggleIntake() {
        switch (state){
            case RETRACTED:
                extend();
                break;
            case EXTENDED:
                retract();
                break;
            default:
                break;
        }
    }
}