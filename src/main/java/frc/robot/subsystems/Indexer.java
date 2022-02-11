package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ball;
import frc.robot.Ball.COLOR;
import frc.robot.Ball.LOCATION;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;
import frc.robot.util.LazySparkMax.Motor;

public class Indexer extends SubsystemBase {
    private final LazySparkMax stomachMotor = new LazySparkMax(Constants.INDEXER_CAN_STOMACH, IdleMode.kBrake, Motor.NEO);
    private final LazySparkMax towerMotor = new LazySparkMax(Constants.INDEXER_CAN_TOWER, IdleMode.kBrake, Motor.NEO);

    private final ColorSensorV3 colorSensorIntake = new ColorSensorV3(Constants.INDEXER_COLOR);
    private final DigitalInput sensorTower = new DigitalInput(Constants.INDEXER_SENSOR_TOWER);
    private final DigitalInput sensorShooter = new DigitalInput(Constants.INDEXER_SENSOR_SHOOTER);

    private final Ball[] balls = {new Ball(), new Ball()};

    private final static Indexer INSTANCE = new Indexer();

    public static Indexer getInstance() {
        return INSTANCE;
    }

    @Override
    public void periodic() {
        if (ballAtIntake()) {
            intakeBall();
        }
        if (ballAtTower()) {
            advanceToTower();
        }
        if (ballAtShooter()) {
            advanceToShooter();
        }
        else if (getBall0().getLocation() == LOCATION.SHOOTER) {
            shootBall();
        }
    }

    public void stomachMotorOn() {
        stomachMotor.set(Constants.INDEXER_STOMACH_SPEED);
    }

    public void stomachMotorOff() {
        stomachMotor.stopMotor();
    }

    public void towerMotorOn() {
        stomachMotor.set(Constants.INDEXER_TOWER_SPEED);
    }

    public void towerMotorOff() {
        towerMotor.stopMotor();
    }

    public Ball getBall0() {
        return balls[0];
    }

    public void resetBalls() {
        balls[0] = new Ball();
        balls[1] = new Ball();
    }

    public boolean atMaxBalls() {
        return balls[0].getLocation() != LOCATION.FIELD && balls[1].getLocation() != LOCATION.FIELD;
    }

    public boolean ballAtIntake() {
        return colorSensorIntake.getProximity() > Constants.INDEXER_COLOR_PROXIMITY;
    }

    public boolean ballAtTower() {
        return sensorTower.get();
    }

    public boolean ballAtShooter() {
        return sensorShooter.get();
    }

    public void intakeBall() {
        COLOR color = colorSensorIntake.getRed() > colorSensorIntake.getBlue() ? COLOR.RED : COLOR.BLUE;
        if (balls[0].getLocation() == LOCATION.FIELD) {
            balls[0].setLocationColor(LOCATION.INTAKE, color);
        }
        else {
            balls[1].setLocationColor(LOCATION.INTAKE, color);
        }
    }

    public void shootBall() {
        balls[0] = balls[1];
        balls[1] = new Ball();
    }

    public void advanceToTower() {
        if (balls[0].getLocation() != LOCATION.TOWER) {
            balls[0].setLocation(LOCATION.TOWER);
        }
        else {
            balls[1].setLocation(LOCATION.TOWER);
        }
    }

    public void advanceToShooter() {
        balls[0].setLocation(LOCATION.SHOOTER);
    }

    public boolean readyToShoot() {
        return balls[0].getLocation() == LOCATION.SHOOTER;
    }

}