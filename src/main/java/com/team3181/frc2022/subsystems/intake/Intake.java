package com.team3181.frc2022.subsystems.intake;

import com.team3181.frc2022.Constants;
import com.team3181.frc2022.RobotState;
import com.team3181.lib.io2022.IntakeIO;
import com.team3181.lib.io2022.IntakeIO.IntakeIOInputs;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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

    private final static Intake INSTANCE = new Intake(Constants.ROBOT_INTAKE_IO);
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
        if (!RobotState.getInstance().isClimbing()) {
            io.setExtended(isExtended);
            io.set(Constants.INTAKE_MAIN_SPEED * motorStatus);
        }
        else {
            io.setExtended(false);
            io.set(0);
            isExtended = false;
        }
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
}