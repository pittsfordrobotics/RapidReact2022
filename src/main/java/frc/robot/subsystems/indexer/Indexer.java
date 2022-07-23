package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ball;
import frc.robot.Ball.COLOR;
import frc.robot.Ball.LOCATION;
import frc.robot.Constants;
import frc.robot.commands.IntakeUpNoInterupt;
import frc.robot.subsystems.indexer.IndexerIO.IndexerIOInputs;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import frc.robot.util.controller.BetterXboxController;
import org.littletonrobotics.junction.Logger;

public class Indexer extends SubsystemBase {
    private final IndexerIO io;
    private final IndexerIOInputs inputs = new IndexerIOInputs();

    private boolean reverse = false;
    private boolean shooting = false;
    private boolean ballStillAtIntake = false;
    private boolean ballStillAtTower = false;

    private final Ball[] balls = {new Ball(), new Ball()};

    private enum State {
        DISABLED, FIELD2, INTAKE1, INTAKE2, TOWER1INTAKE1, TOWER1, ARMED1INTAKE1, ARMED1, ARMED2, SHOOTING1INTAKE1, SHOOTING1, SHOOTING2, OVERRIDE
    }
    private State state = State.DISABLED;

    private COLOR allianceColor = COLOR.UNKNOWN;

    private final Alert colorSensorAlert = new Alert("Color sensor not detected! Auto indexing will NOT work!", AlertType.ERROR);

    private final static Indexer INSTANCE = new Indexer(new IndexerIOSparkMax());
    public static Indexer getInstance() {
        return INSTANCE;
    }

    private Indexer(IndexerIO io) {
        this.io = io;
        ShuffleboardTab indexerTab = Shuffleboard.getTab("Indexer");
        indexerTab.add("Reset", new InstantCommand(this::resetEverything));
        indexerTab.add("Toggle Shooting", new InstantCommand(() -> shooting = !shooting));
        indexerTab.addString("Alliance Color", () -> allianceColor.toString());
        indexerTab.addString("Indexer state", () -> state.toString());
        indexerTab.addString("Ball 1 Color", () -> balls[0].getColor().toString());
        indexerTab.addString("Ball 2 Color", () -> balls[1].getColor().toString());
        indexerTab.addString("Ball 1 Location", () -> balls[0].getLocation().toString());
        indexerTab.addString("Ball 2 Location", () -> balls[1].getLocation().toString());
        indexerTab.addNumber("Intake Red", () -> inputs.colorRed);
        indexerTab.addNumber("Intake Blue", () -> inputs.colorBlue);
        indexerTab.addNumber("Intake proximity", () -> inputs.colorProximity);
        indexerTab.addBoolean("Reversed", () -> reverse);
        indexerTab.addBoolean("Intake Sensor", () -> inputs.colorProximity > 200);
        indexerTab.addBoolean("Tower Sensor", () -> inputs.towerDetected);
        indexerTab.addBoolean("Shooter Sensor", () -> inputs.shooterDetected);
        indexerTab.addBoolean("Shooting?", () -> shooting);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Indexer", inputs);
        Logger.getInstance().recordOutput("Indexer/State", state.toString());
        colorSensorAlert.set(!inputs.colorConnected);
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
                stomachMotorOn();
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
            case OVERRIDE:
                if (!reverse) {
                    stomachMotorOn();
                    towerMotorOn();
                }
                else {
                    stomachMotorReverse();
                    towerMotorReverse();
                }
                break;
            default:
                stomachMotorOff();
                towerMotorOff();
        }
        if (isFull() && !DriverStation.isAutonomous()) {
            CommandScheduler.getInstance().schedule(false, new IntakeUpNoInterupt());
        }
        if (isWrongColorBall()) {
            BetterXboxController.getController(BetterXboxController.Humans.OPERATOR).rumbleOn();
        }
        else {
            BetterXboxController.getController(BetterXboxController.Humans.OPERATOR).rumbleOff();
        }
        SmartDashboard.putBoolean("Fully Loaded", fullyLoaded());
        getAllianceColor();
    }

    public void getAllianceColor() {
        if (allianceColor == COLOR.UNKNOWN) {
            switch (DriverStation.getAlliance()){
                case Red:
                    allianceColor = COLOR.RED;
                    break;
                case Blue:
                    allianceColor = COLOR.BLUE;
                    break;
                case Invalid:
                default:
                    allianceColor = COLOR.UNKNOWN;
                    break;
            }
        }
    }

    public boolean isDisabled() {
        return state == State.DISABLED;
    }

    public void resetEverything() {
        state = State.FIELD2;
        resetBalls();
    }

    public void override() {
        state = State.OVERRIDE;
    }

    public void disable() {
        this.state = State.DISABLED;
    }

    public void addBallToTower() {
        balls[0] = new Ball(LOCATION.TOWER, allianceColor);
        state = State.TOWER1;
    }

    public void setStateShoot() {
        shooting = true;
    }

    public void setStateStopShoot() {
        shooting = false;
    }

    public void stomachMotorOn() {
        io.setStomachLeft(Constants.INDEXER_STOMACH_SPEED);
        io.setStomachRight(Constants.INDEXER_STOMACH_SPEED);
    }

    public void stomachMotorReverse() {
        io.setStomachRight(-Constants.INDEXER_STOMACH_SPEED);
        io.setStomachLeft(-Constants.INDEXER_STOMACH_SPEED);
    }

    public void stomachMotorOff() {
        io.setStomachLeft(0);
        io.setStomachRight(0);
    }

    public void towerMotorOn() {
        io.setTower(Constants.INDEXER_TOWER_SPEED);
    }

    public void towerMotorReverse() {
        io.setTower(-Constants.INDEXER_TOWER_SPEED);
    }

    public void towerMotorOff() {
        io.setTower(0);
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

    public int getBallCount() {
        return (balls[0].getLocation() != LOCATION.FIELD ? 1 : 0) + (balls[1].getLocation() != LOCATION.FIELD ? 1 : 0);
    }

    public boolean getBallAtIntake() {
        if (!inputs.colorConnected) {
            return true;
        }
        boolean ballAtIntake = false;
        if (ballStillAtIntake) {
            ballStillAtIntake = inputs.colorProximity > Constants.INDEXER_COLOR_PROXIMITY;
        }
        else {
            ballAtIntake = inputs.colorProximity > Constants.INDEXER_COLOR_PROXIMITY;
            ballStillAtIntake = ballAtIntake;
        }
        return ballAtIntake;
    }

    public boolean getBallAtTower() {
        boolean ballAtTower = false;
        if (ballStillAtTower) {
            ballStillAtTower = inputs.towerDetected;
        }
        else {
            ballAtTower = inputs.towerDetected;
            ballStillAtTower = ballAtTower;
        }
        return ballAtTower;
    }

    public boolean getBallAtShooter() {
        return inputs.shooterDetected;
    }

    public void intakeBall() {
        COLOR color = inputs.colorRed > inputs.colorBlue ? COLOR.RED : COLOR.BLUE;
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

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean ableToShoot() {
        return state == State.ARMED1 || state == State.ARMED2 || state == State.ARMED1INTAKE1;
    }

    public boolean fullyLoaded() {
        return state == State.ARMED2;
    }

    public boolean isWrongColorBall() {
        return balls[0].getColor() != allianceColor && balls[0].getColor() != COLOR.UNKNOWN && allianceColor != COLOR.UNKNOWN;
    }

    public boolean allianceIsUnknown() {
        return allianceColor == COLOR.UNKNOWN;
    }
}