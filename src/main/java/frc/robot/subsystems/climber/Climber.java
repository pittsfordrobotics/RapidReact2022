package frc.robot.subsystems.climber;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotState;
import frc.robot.commands.CG_ClimberCalibrate;
import frc.robot.subsystems.climber.ClimberIO.ClimberIOInputs;
import org.littletonrobotics.junction.Logger;

public class Climber extends SubsystemBase {
    private final ClimberIO io;
    private final ClimberIOInputs inputs = new ClimberIOInputs();

    private double halfway = 0;

    private final static Climber INSTANCE = new Climber(Constants.ROBOT_CLIMBER_IO);
    public static Climber getInstance() {
        return INSTANCE;
    }

    private Climber(ClimberIO io) {
        this.io = io;
        ShuffleboardTab climberTab = Shuffleboard.getTab("Climber");
        climberTab.add("Calibrate Climber", new CG_ClimberCalibrate());
        climberTab.addNumber("Encoder", () -> Units.radiansToRotations(inputs.positionRad));
        climberTab.addBoolean("Right Front Limit Switch", () -> inputs.rightForwardSwitch);
        climberTab.addBoolean("Right Reverse Limit Switch", () -> inputs.rightReverseSwitch);
        climberTab.addBoolean("Left Front Limit Switch", () -> inputs.leftForwardSwitch);
        climberTab.addBoolean("Left Reverse Limit Switch", () -> inputs.leftReverseSwitch);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Climber", inputs);
        Logger.getInstance().recordOutput("Climber/Enabled", RobotState.getInstance().isClimbing());
    }

    public void setSpeed(double speed) {
        if (RobotState.getInstance().isClimbing()) io.set(speed);
    }

    public void resetEncoders() {
        io.resetEncoders();
    }

    public void setSoftLimit(boolean state) {
        io.enableSoftLimit(state);
    }

    public double getEncoder() {
        return Units.radiansToRotations(inputs.positionRad);
    }

    public void saveHalfway() {
        halfway = Units.radiansToRotations(inputs.positionRad)/2;
    }

    public boolean hasBeenCentered() {
        return Units.radiansToRotations(inputs.positionRad) >= halfway;
    }

    public boolean forwardAtHardLimit() {
        return inputs.rightForwardSwitch && inputs.leftForwardSwitch;
    }

    public boolean reverseAtHardLimit() {
        return inputs.rightReverseSwitch && inputs.leftReverseSwitch;
    }

    public boolean forwardAtSoftLimit() {
        return getEncoder() > 90;
    }

    public boolean reverseAtSoftLimit() {
        return getEncoder() < -90;
    }

    public void front() {
        if (getEncoder() < 75) {
            setSpeed(0.7);
        }
        else {
            setSpeed(0.6);
        }
    }

    public void reverse() {
        if (getEncoder() > -75) {
            setSpeed(-0.7);
        }
        else {
            setSpeed(-0.6);
        }
    }

    public void calibrateFront() {
        setSpeed(0.3);
    }

    public void calibrateReverse() {
        setSpeed(-0.3);
    }

    public void stopAll() {
        io.set(0);
    }
}