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
    private final DigitalInput sensorTowerBottom = new DigitalInput(Constants.INDEXER_SENSOR_TOWER_BOTTOM);
    private final DigitalInput sensorTowerMiddle = new DigitalInput(Constants.INDEXER_SENSOR_TOWER_MIDDLE);
    private final DigitalInput sensorShooter = new DigitalInput(Constants.INDEXER_SENSOR_SHOOTER);

    private final Ball[] balls = {new Ball(), new Ball()};

    private enum State {
        FIELD, INTAKE1, INTAKE2, TOWER1INTAKE1, TOWER1, TOWER2, ARMED1INTAKE1, ARMED1TOWER1, ARMED1, ARMED2
    }
    private State state = State.FIELD;

    private final static Indexer INSTANCE = new Indexer();
    public static Indexer getInstance() {
        return INSTANCE;
    }

    @Override
    public void periodic() {
        switch (state) {
            case FIELD:
                stomachMotorOff();
                towerMotorOff();
                if (ballAtIntake()) {
                    intakeBall();
                    state = State.INTAKE1;
                }
                break;
            case INTAKE1:
                stomachMotorOn();
                towerMotorOff();
                if (ballAtTowerLow() && !ballAtIntake()) {
                    advanceToTowerLow();
                    state = State.TOWER1;
                }
                else if (ballAtTowerLow() && ballAtIntake()) {
                    advanceToTowerLow();
                    intakeBall();
                    state = State.TOWER1INTAKE1;
                }
                else if (!ballAtTowerLow() && ballAtIntake()) {
                    intakeBall();
                    state = State.INTAKE2;
                }
                break;
            case INTAKE2:
                stomachMotorOn();
                towerMotorOff();
                if (ballAtTowerLow()) {
                    advanceToTowerLow();
                    state = State.TOWER1INTAKE1;
                }
                break;
            case TOWER1INTAKE1:
                stomachMotorOn();
                towerMotorOn();
                if (ballAtShooter() && !ballAtTowerLow()) {
                    advanceToShooter();
                    state = State.ARMED1INTAKE1;
                }
                else if (ballAtShooter() && ballAtTowerLow()) {
                    advanceToShooter();
                    advanceToTowerLow();
                    state = State.ARMED1TOWER1;
                }
                else if (!ballAtShooter() && ballAtTowerLow()) {
                    advanceToTowerLow();
                    state = State.TOWER2;
                }
                break;
            case TOWER1:
                stomachMotorOff();
                towerMotorOn();
                if (ballAtShooter() && !ballAtIntake()) {
                    advanceToShooter();
                    state = State.ARMED1;
                }
                else if (ballAtShooter() && ballAtIntake()) {
                    advanceToShooter();
                    intakeBall();
                    state = State.ARMED1INTAKE1;
                }
                else if (!ballAtShooter() && ballAtIntake()) {
                    intakeBall();
                    state = State.TOWER1INTAKE1;
                }
                break;
            case TOWER2:
                stomachMotorOff();
                towerMotorOn();
                if (ballAtShooter() && !ballAtTowerMid()) {
                    advanceToShooter();
                    state = State.ARMED1TOWER1;
                }
                else if (ballAtShooter() && ballAtTowerMid()) {
                    advanceToShooter();
                    advanceToTowerMid();
                    state = State.ARMED2;
                }
                break;
            case ARMED1INTAKE1:
                stomachMotorOn();
                towerMotorOff();
                if (!ballAtShooter() && !ballAtTowerLow()) {
                    shootBall();
                    state = State.INTAKE1;
                }
                else if (!ballAtShooter() && ballAtTowerLow()) {
                    shootBall();
                    advanceToTowerLow();
                    state = State.TOWER1;
                }
                else if (ballAtShooter() && ballAtTowerLow()) {
                    advanceToTowerLow();
                    state = State.ARMED1TOWER1;
                }
                break;
            case ARMED1TOWER1:
                stomachMotorOff();
                towerMotorOn();
                if (!ballAtShooter() && !ballAtTowerMid()) {
                    shootBall();
                    state = State.TOWER1;
                }
                else if (!ballAtShooter() && ballAtTowerMid()) {
                    shootBall();
                    advanceToTowerMid();
                    state = State.TOWER1;
                }
                else if (ballAtShooter() && ballAtTowerMid()) {
                    advanceToTowerMid();
                    state = State.ARMED2;
                }
                break;
            case ARMED1:
                stomachMotorOff();
                towerMotorOff();
                if (!ballAtShooter() && !ballAtIntake()) {
                    shootBall();
                    state = State.FIELD;
                }
                else if (!ballAtShooter() && ballAtIntake()) {
                    shootBall();
                    intakeBall();
                    state = State.INTAKE1;
                }
                else if (ballAtShooter() && ballAtIntake()) {
                    intakeBall();
                    state = State.ARMED1INTAKE1;
                }
                break;
            case ARMED2:
                stomachMotorOff();
                towerMotorOff();
                if (!ballAtShooter()) {
                    shootBall();
                    state = State.ARMED1;
                }
                break;
            default:
                stomachMotorOff();
                towerMotorOff();
        }
    }

    public void setState(State state) {
        this.state = state;
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

    public int ballCount() {
        return (balls[0].getLocation() != LOCATION.FIELD ? 1 : 0) + (balls[1].getLocation() != LOCATION.FIELD ? 1 : 0);
    }

    public boolean ballAtIntake() {
        return colorSensorIntake.getProximity() > Constants.INDEXER_COLOR_PROXIMITY;
    }

    public boolean ballAtTowerLow() {
        return sensorTowerBottom.get();
    }

    public boolean ballAtTowerMid() {
        return sensorTowerMiddle.get();
    }

    public boolean ballAtShooter() {
        return sensorShooter.get();
    }

    public void intakeBall() {
        COLOR color = colorSensorIntake.getRed() > colorSensorIntake.getBlue() ? COLOR.RED : COLOR.BLUE;
        if (balls[0].getLocation() == LOCATION.FIELD) {
            balls[0].setLocationColor(LOCATION.INTAKE, color);
        }
        else if (balls[1].getLocation() == LOCATION.FIELD) {
            balls[1].setLocationColor(LOCATION.INTAKE, color);
        }
    }

    public void shootBall() {
        balls[0] = balls[1];
        balls[1] = new Ball();
    }

    public void advanceToTowerLow() {
        if (balls[0].getLocation() == LOCATION.INTAKE) {
            balls[0].setLocation(LOCATION.TOWER_LOW);
        }
        else if (balls[1].getLocation() == LOCATION.INTAKE) {
            balls[1].setLocation(LOCATION.TOWER_LOW);
        }
    }

    public void advanceToTowerMid() {
        if (balls[0].getLocation() == LOCATION.TOWER_LOW) {
            balls[0].setLocation(LOCATION.TOWER_MID);
        }
        else if (balls[1].getLocation() == LOCATION.TOWER_LOW) {
            balls[1].setLocation(LOCATION.TOWER_MID);
        }
    }

    public void advanceToShooter() {
        balls[0].setLocation(LOCATION.SHOOTER);
    }

    public boolean ableToShoot() {
        return state == State.ARMED1 || state == State.ARMED2 || state == State.ARMED1INTAKE1 || state == State.ARMED1TOWER1;
    }

    public boolean fullyLoaded() {
        return state == State.ARMED1 || state == State.ARMED2;
    }

}