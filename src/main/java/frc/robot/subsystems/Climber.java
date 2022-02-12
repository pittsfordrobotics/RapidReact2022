package frc.robot.subsystems;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

    // With eager singleton initialization, any static variables/fields used in the 
    // constructor must appear before the "INSTANCE" variable so that they are initialized 
    // before the constructor is called when the "INSTANCE" variable initializes.
    private CANSparkMax leftClimber = new CANSparkMax(Constants.CLIMBER_CAN_LEFT, MotorType.kBrushless);
    private CANSparkMax rightClimber = new CANSparkMax(Constants.CLIMBER_CAN_RIGHT, MotorType.kBrushless);

    private final static Climber INSTANCE = new Climber();

    public static Climber getInstance() {
        return INSTANCE;
    }

    private Climber() {
        
    }

    public void move() { // thanks paras
        leftClimber.set(Constants.CLIMBER_SPEED);
        rightClimber.set(Constants.CLIMBER_SPEED);
    }
    public void moveBack() {
        leftClimber.set(Constants.CLIMBER_SPEED);
        rightClimber.set(Constants.CLIMBER_SPEED);
    }
    public void stop() {
        leftClimber.stopMotor();
        rightClimber.stopMotor();
    }
}