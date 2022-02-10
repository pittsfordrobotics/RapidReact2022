package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LazySparkMax;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
private final CANSparkMax shooterMotor = new LazySparkMax(Constants.SHOOTER_CAN_MAIN, IdleMode.kCoast, false);

    private final static Shooter INSTANCE = new Shooter();

    public static Shooter getInstance() {
        return INSTANCE;
    }

    private Shooter() {
        // TODO: Set the default command, if any, for this subsystem by calling setDefaultCommand(command)
        //       in the constructor or in the robot coordination class, such as RobotContainer.
        //       Also, you can call addChild(name, sendableChild) to associate sendables with the subsystem
        //       such as SpeedControllers, Encoders, DigitalInputs, etc.
    }   
    public void shooterOn() {
        shooterMotor.set(0.7);
    }
    public void shooterOff() {
        shooterMotor.stopMotor();
    }

}