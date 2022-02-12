package frc.robot.subsystems;


import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;
import frc.robot.util.LazySparkMax.Motor;

public class Climber extends SubsystemBase {

    // With eager singleton initialization, any static variables/fields used in the 
    // constructor must appear before the "INSTANCE" variable so that they are initialized 
    // before the constructor is called when the "INSTANCE" variable initializes.
    private final LazySparkMax leftClimber = new LazySparkMax(Constants.CLIMBER_CAN_LEFT, IdleMode.kCoast, Motor.NEO);
    private final LazySparkMax rightClimber = new LazySparkMax(Constants.CLIMBER_CAN_RIGHT, IdleMode.kCoast, Motor.NEO);

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
        leftClimber.set(-Constants.CLIMBER_SPEED);
        rightClimber.set(-Constants.CLIMBER_SPEED);
    }
    public void stop() {
        leftClimber.stopMotor();
        rightClimber.stopMotor();
    }
}