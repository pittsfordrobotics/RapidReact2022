package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.intake.IntakeIO.IntakeIOInputs;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
    private boolean isExtended = false;

    private final IntakeIO io;
    private final IntakeIOInputs inputs = new IntakeIOInputs();

    private final static Intake INSTANCE = new Intake(new IntakeIOSparkMax());
    public static Intake getInstance() {
        return INSTANCE;
    }

    private Intake(IntakeIO io) {
        this.io = io;
        retract();
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Intake", inputs);
        io.setExtended(isExtended);
        SmartDashboard.putBoolean("Intake Extended", isExtended());
    }

    public void extend() {
        isExtended = true;
    }

    public void retract() {
        isExtended = false;
    }

    public void toggleSolenoid() {
        isExtended = !isExtended;
    }

    public void toggleMotor() {
        if (isExtended) {
            motorOn();
        }
        else {
            motorOff();
        }
    }

    public void motorOn() {
        io.set(Constants.INTAKE_MAIN_SPEED);
    }

    public void motorOff() {
        io.set(0);
    }

    public void motorReverse() {
        io.set(-Constants.INTAKE_MAIN_SPEED);
    }

    public boolean isExtended() {
        return isExtended;
    }
}