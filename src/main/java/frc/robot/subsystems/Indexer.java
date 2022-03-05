package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ball;
import frc.robot.Ball.COLOR;
import frc.robot.Ball.LOCATION;
import frc.robot.Constants;
import frc.robot.util.LazySparkMax;

public class Indexer extends SubsystemBase {
    private final LazySparkMax motorLeft = new LazySparkMax(Constants.INDEXER_CAN_STOMACH_LEFT, IdleMode.kBrake, 30);
    private final LazySparkMax motorRight = new LazySparkMax(Constants.INDEXER_CAN_STOMACH_RIGHT, IdleMode.kBrake, 30, motorLeft);
    private final LazySparkMax towerMotor = new LazySparkMax(Constants.INDEXER_CAN_TOWER, IdleMode.kBrake, 30);

    private final ColorSensorV3 colorSensorIntake = new ColorSensorV3(Constants.INDEXER_COLOR);
//    private final DigitalInput sensorTower = new DigitalInput(Constants.INDEXER_SENSOR_TOWER);
//    private final DigitalInput sensorShooter = new DigitalInput(Constants.INDEXER_SENSOR_SHOOTER);
    private final AnalogInput input = new AnalogInput(0);
    private final AnalogInput input2 = new AnalogInput(1);

    private boolean shooting = false;
    private boolean ballStillAtIntake = false;
    private boolean ballStillAtTower = false;

    private final Ball[] balls = {new Ball(), new Ball()};

    private enum State {
        FIELD2, INTAKE1, INTAKE2, TOWER1INTAKE1, TOWER1, ARMED1INTAKE1, ARMED1, ARMED2, SHOOTING1INTAKE1, SHOOTING1, SHOOTING2, PURGE
    }
    private State state = State.FIELD2;

    private final ShuffleboardTab indexerTab = Shuffleboard.getTab("Indexer");

    private final static Indexer INSTANCE = new Indexer();
    public static Indexer getInstance() {
        return INSTANCE;
    }

    private Indexer() {
        indexerTab.addString("Indexer state", () -> state.toString());
        indexerTab.addString("Ball 1 Color", () -> balls[0].getColor().toString());
        indexerTab.addString("Ball 2 Color", () -> balls[1].getColor().toString());
        indexerTab.addString("Ball 1 Location", () -> balls[0].getLocation().toString());
        indexerTab.addString("Ball 2 Location", () -> balls[1].getLocation().toString());
        indexerTab.addBoolean("Intake boolean", this::getBallAtIntake);
        indexerTab.addNumber("Intake proximity", colorSensorIntake::getProximity);
        indexerTab.addBoolean("Tower boolean", this::getBallAtTower);
        indexerTab.addBoolean("Shooter boolean", this::getBallAtShooter);
        indexerTab.add("Reset", new InstantCommand(this::resetEverything));
        indexerTab.add("Toggle Shooting", new InstantCommand(() -> shooting = !shooting));
        indexerTab.addBoolean("Shooting?", () -> shooting);
    }

    @Override
    public void periodic() {
//        TODO: recheck this statemachine because intelj is mad at me
        boolean ballCurrentlyAtIntake = getBallAtIntake();
        boolean ballCurrentlyAtTower = getBallAtTower();
        boolean ballCurrentlyAtShooter = getBallAtShooter();
        switch (state) {
            case FIELD2:
                stomachMotorOff();
                towerMotorOff();
                if (ballCurrentlyAtIntake) {
                    intakeBall();
                    state = State.INTAKE1;
                }
                break;
            case INTAKE1:
                stomachMotorOn();
                towerMotorOff();
                if (ballCurrentlyAtTower && !ballCurrentlyAtIntake) {
                    advanceToTower();
                    state = State.TOWER1;
                    break;
                }
                if (ballCurrentlyAtTower && ballCurrentlyAtIntake) {
                    advanceToTower();
                    intakeBall();
                    state = State.TOWER1INTAKE1;
                    break;
                }
                if (!ballCurrentlyAtTower && ballCurrentlyAtIntake) {
                    intakeBall();
                    state = State.INTAKE2;
                    break;
                }
                break;
            case INTAKE2:
                stomachMotorOn();
                towerMotorOff();
                if (ballCurrentlyAtTower) {
                    advanceToTower();
                    state = State.TOWER1INTAKE1;
                }
                break;
            case TOWER1INTAKE1:
                stomachMotorOn();
                towerMotorOn();
                if (ballCurrentlyAtShooter && !ballCurrentlyAtTower) {
                    advanceToShooter();
                    state = State.ARMED1INTAKE1;
                    break;
                }
                if (ballCurrentlyAtShooter && ballCurrentlyAtTower) {
                    advanceToShooter();
                    advanceToTower();
                    state = State.ARMED2;
                    break;
                }
                break;
            case TOWER1:
                stomachMotorOff();
                towerMotorOn();
                if (ballCurrentlyAtShooter && !ballCurrentlyAtIntake) {
                    advanceToShooter();
                    state = State.ARMED1;
                    break;
                }
                if (ballCurrentlyAtShooter && ballCurrentlyAtIntake) {
                    advanceToShooter();
                    intakeBall();
                    state = State.ARMED1INTAKE1;
                    break;
                }
                if (!ballCurrentlyAtShooter && ballCurrentlyAtIntake) {
                    intakeBall();
                    state = State.TOWER1INTAKE1;
                    break;
                }
                break;
            case ARMED1INTAKE1:
                stomachMotorOn();
                towerMotorOff();
                if (ballCurrentlyAtShooter && ballCurrentlyAtTower && shooting) {
                    advanceToTower();
                    state = State.SHOOTING2;
                }
                else if (shooting) {
                    state = State.SHOOTING1INTAKE1;
                }
                else if (ballCurrentlyAtShooter && ballCurrentlyAtTower) {
                    advanceToTower();
                    state = State.ARMED2;
                }
                break;
            case ARMED1:
                stomachMotorOff();
                towerMotorOff();

                if (ballCurrentlyAtShooter && ballCurrentlyAtIntake && shooting) {
                    intakeBall();
                    state = State.SHOOTING1INTAKE1;
                }
                else if (shooting) {
                    state = State.SHOOTING1;
                }
                else if (ballCurrentlyAtShooter && ballCurrentlyAtIntake) {
                    intakeBall();
                    state = State.ARMED1INTAKE1;
                }
                break;
            case ARMED2:
                stomachMotorOff();
                towerMotorOff();
                if (shooting) {
                    state = State.SHOOTING2;
                }
                break;
            case SHOOTING1INTAKE1:
                stomachMotorOn();
                towerMotorOn();
                if (!ballCurrentlyAtShooter && !ballCurrentlyAtTower) {
                    shootBall();
                    state = State.INTAKE1;
                    break;
                }
                if (ballCurrentlyAtShooter && ballCurrentlyAtTower && shooting) {
                    advanceToTower();
                    state = State.SHOOTING2;
                    break;
                }
                if (ballCurrentlyAtShooter && ballCurrentlyAtTower) {
                    advanceToTower();
                    state = State.ARMED2;
                    break;
                }
                if (!ballCurrentlyAtShooter && ballCurrentlyAtTower) {
                    advanceToTower();
                    state = State.TOWER1;
                    break;
                }
                break;
            case SHOOTING1:
                stomachMotorOff();
                towerMotorOn();
                if (!ballCurrentlyAtShooter && ballCurrentlyAtIntake) {
                    intakeBall();
                    shootBall();
                    state = State.INTAKE1;
                    break;
                }
                if (!ballCurrentlyAtShooter) {
                    shootBall();
                    state = State.FIELD2;
                    break;
                }
                break;
            case SHOOTING2:
                stomachMotorOn();
                towerMotorOn();
                if (!ballCurrentlyAtShooter) {
                    shootBall();
                    state = State.TOWER1;
                }
                break;
            default:
                stomachMotorOff();
                towerMotorOff();
        }
        SmartDashboard.putBoolean("Fully Loaded", fullyLoaded());
    }

    public void resetEverything() {
        state = State.FIELD2;
        resetBalls();
    }

    public void purge() {
        state = State.PURGE;
        motorLeft.set(-Constants.INDEXER_STOMACH_SPEED);
        towerMotor.set(-Constants.INDEXER_TOWER_SPEED);
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setStateShoot() {
        shooting = true;
    }

    public void setStateStopShoot() {
        shooting = false;
    }

    public void stomachMotorOn() {
        motorLeft.set(Constants.INDEXER_STOMACH_SPEED);
    }

    public void stomachMotorOff() {
        motorLeft.stopMotor();
    }

    public void towerMotorOn() {
        motorLeft.set(Constants.INDEXER_TOWER_SPEED);
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

    public boolean isFull() {
        return balls[0].getLocation() != LOCATION.FIELD && balls[1].getLocation() != LOCATION.FIELD;
    }

    public boolean isEmpty() {
        return balls[0].getLocation() == LOCATION.FIELD && balls[1].getLocation() == LOCATION.FIELD;
    }

    public int ballCount() {
        return (balls[0].getLocation() != LOCATION.FIELD ? 1 : 0) + (balls[1].getLocation() != LOCATION.FIELD ? 1 : 0);
    }

    public boolean getBallAtIntake() {
        boolean ballAtIntake = false;
        if (ballStillAtIntake) {
            ballStillAtIntake = colorSensorIntake.getProximity() > Constants.INDEXER_COLOR_PROXIMITY;
        }
        else {
            ballAtIntake = colorSensorIntake.getProximity() > Constants.INDEXER_COLOR_PROXIMITY;
            ballStillAtIntake = ballAtIntake;
        }
        return ballAtIntake;
    }

    public boolean getBallAtTower() {
        boolean ballAtTower = false;
        if (ballStillAtTower) {
//            ballStillAtTower = !sensorTower.get();
            ballStillAtTower = input.getValue() < 1700;
        }
        else {
//            ballAtTower = !sensorTower.get();
            ballAtTower =  input.getValue() < 1700;
            ballStillAtTower = ballAtTower;
        }
        return ballAtTower;
    }

    public boolean getBallAtShooter() {
//        return !sensorShooter.get();
        return input2.getValue() < 1700;
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

    public void advanceToTower() {
        if (balls[0].getLocation() == LOCATION.INTAKE) {
            balls[0].setLocation(LOCATION.TOWER);
        }
        else if (balls[1].getLocation() == LOCATION.INTAKE) {
            balls[1].setLocation(LOCATION.TOWER);
        }
    }

    public void advanceToShooter() {
        balls[0].setLocation(LOCATION.SHOOTER);
    }

    public boolean ableToShoot() {
        return state == State.ARMED1 || state == State.ARMED2 || state == State.ARMED1INTAKE1;
    }

    public boolean fullyLoaded() {
        return state == State.ARMED2;
    }

}