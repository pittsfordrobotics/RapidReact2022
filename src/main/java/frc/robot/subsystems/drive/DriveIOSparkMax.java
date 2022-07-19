package frc.robot.subsystems.drive;

import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class DriveIOSparkMax implements DriveIO {
    private final LazySparkMax leftPrimary = new LazySparkMax(Constants.DRIVE_CAN_LEFT_LEADER, IdleMode.kBrake, 60,false);
    private final LazySparkMax leftFollower = new LazySparkMax(Constants.DRIVE_CAN_LEFT_FOLLOWER, IdleMode.kBrake, 60, leftPrimary);
    private final LazySparkMax rightPrimary = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_LEADER, IdleMode.kBrake, 60, true);
    private final LazySparkMax rightFollower = new LazySparkMax(Constants.DRIVE_CAN_RIGHT_FOLLOWER, IdleMode.kBrake, 60, rightPrimary);

    private final WPI_Pigeon2 pigeon = new WPI_Pigeon2(Constants.DRIVE_CAN_PIGEON);
    private final AHRS navX = new AHRS(SPI.Port.kMXP);

    public DriveIOSparkMax() {

    }
}