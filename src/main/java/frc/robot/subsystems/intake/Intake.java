package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.intake.IntakeIO.IntakeIOInputs;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
    private boolean isExtended = false;
    /**
     * -1 = reverse
     *  0 = stop
     *  1 = forward
     */
    private int motorStatus = 0;

    private final IntakeIO io;
    private final IntakeIOInputs inputs = new IntakeIOInputs();

    private boolean climbing = false;

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
        if (!climbing) {
            io.setExtended(isExtended);
            io.set(Constants.INTAKE_MAIN_SPEED * motorStatus);
        }
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

    public void autoMotor() {
        if (isExtended) {
            motorOn();
        }
        else {
            motorOff();
        }
    }

    public void motorOn() {
        motorStatus = 1;
    }

    public void motorOff() {
        motorStatus = 0;
    }

    public void motorReverse() {
        motorStatus = -1;
    }

    public boolean isExtended() {
        return isExtended;
    }

    public void setClimbing(boolean climbing) {
        this.climbing = climbing;
    }
}