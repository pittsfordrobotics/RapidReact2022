package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ball;
import frc.robot.Ball.COLOR;
import frc.robot.Ball.LOCATION;
import frc.robot.Constants;
import frc.robot.commands.IntakeReverse;
import frc.robot.commands.IntakeUp;
import frc.robot.commands.IntakeUpNoInterrupt;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.indexer.IndexerIO.IndexerIOInputs;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import frc.robot.util.DisabledInstantCommand;
import org.littletonrobotics.junction.Logger;

/**
 * This is the real brains of the operation
 */
public class Indexer extends SubsystemBase {
    private final IndexerIO io;
    private final IndexerIOInputs inputs = new IndexerIOInputs();

    private boolean reverse = false;
    private boolean shooting = false;
    private boolean ballStillAtIntake = false;
    private boolean ballStillAtTower = false;
    private final Timer rejectionTimer = new Timer();
    private boolean rejectionTimerStarted = false;
    private boolean rejectionEnabled = true;

    private final Ball[] balls = {new Ball(), new Ball()};

    private enum State {
        DISABLED, FIELD2, INTAKE1, INTAKE2, TOWER1INTAKE1, TOWER1, INTAKE1REJECT1, TOWER1REJECT1, ARMED1REJECT1, ARMED1INTAKE1, ARMED1, ARMED2, REJECT1INTAKE1, REJECT1, REJECT1TOWER1, SHOOTING1INTAKE1, SHOOTING1, SHOOTING2, OVERRIDE
    }
    private State state = State.DISABLED;

    private COLOR allianceColor = COLOR.UNKNOWN;

    private final Alert colorSensorAlert = new Alert("Color sensor not detected! Auto indexing will NOT work!", AlertType.ERROR);
    private final Alert cargoRejectionAlert = new Alert("Cargo rejection has been disabled! Auto rejection will NOT work!", AlertType.WARNING);

    private final static Indexer INSTANCE = new Indexer(Constants.ROBOT_INDEXER_IO);
    public static Indexer getInstance() {
        return INSTANCE;
    }

    private Indexer(IndexerIO io) {
        this.io = io;
        rejectionTimer.start();
        ShuffleboardTab indexerTab = Shuffleboard.getTab("Indexer");
        indexerTab.add("Reset", new DisabledInstantCommand(this::resetEverything));
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
        cargoRejectionAlert.set(!rejectionEnabled);
        boolean ballCurrentlyAtIntake = getBallAtIntake();
        boolean ballCurrentlyAtTower = getBallAtTower();
        boolean ballCurrentlyAtShooter = getBallAtShooter();
        Logger.getInstance().recordOutput("Indexer/InstantIntakeBall", ballCurrentlyAtIntake);
        Logger.getInstance().recordOutput("Indexer/InstantTowerBall", ballCurrentlyAtTower);
        Logger.getInstance().recordOutput("Indexer/InstantShooterBall", ballCurrentlyAtShooter);
        getAllianceColorFMS();
        Logger.getInstance().recordOutput("Indexer/AllianceColor", allianceColor.toString());
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
                if (ballCurrentlyAtTower && ballCurrentlyAtIntake) {
                    advanceToTower();
                    intakeBall();
                    if (isWrongColorBall(1) && rejectionEnabled) {
                        state = State.TOWER1REJECT1;
                    }
                    else {
                        state = State.TOWER1INTAKE1;
                    }
                    break;
                }
                if (!ballCurrentlyAtTower && ballCurrentlyAtIntake) {
                    intakeBall();
                    if (isWrongColorBall(1) && rejectionEnabled) {
                        state = State.INTAKE1REJECT1;
                    }
                    else {
                        state = State.INTAKE2;
                    }
                    break;
                }
                if (ballCurrentlyAtTower && !ballCurrentlyAtIntake) {
                    advanceToTower();
                    state = State.TOWER1;
                    break;
                }
                break;
            case INTAKE1REJECT1:
                towerMotorOff();
                stomachMotorReverse();
                if (!rejectionTimerStarted) {
                    rejectionTimer.reset();
                    rejectionTimerStarted = true;
                }
                if (rejectionTimer.advanceIfElapsed(Constants.INDEXER_INTAKE_REJECTION_TIME)) {
                    intakeReject();
                    rejectionTimerStarted = false;
                    state = State.INTAKE1;
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
            case TOWER1REJECT1:
                towerMotorOn();
                stomachMotorReverse();
                if (!rejectionTimerStarted) {
                    rejectionTimer.reset();
                    rejectionTimerStarted = true;
                }
                if (ballCurrentlyAtShooter && rejectionTimer.advanceIfElapsed(Constants.INDEXER_INTAKE_REJECTION_TIME)) {
                    intakeReject();
                    advanceToShooter();
                    rejectionTimerStarted = false;
                    state = State.ARMED1;
                }
                else if (ballCurrentlyAtShooter) {
                    advanceToShooter();
                    state = State.ARMED1REJECT1;
                }
                else if (rejectionTimer.advanceIfElapsed(Constants.INDEXER_INTAKE_REJECTION_TIME)) {
                    intakeReject();
                    rejectionTimerStarted = false;
                    state = State.TOWER1;
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
                    if (isWrongColorBall(1) && rejectionEnabled) {
                        state = State.ARMED1REJECT1;
                    }
                    else {
                        state = State.ARMED1INTAKE1;
                    }
                    break;
                }
                if (!ballCurrentlyAtShooter && ballCurrentlyAtIntake) {
                    intakeBall();
                    if (isWrongColorBall(1) && rejectionEnabled) {
                        state = State.TOWER1REJECT1;
                    }
                    else {
                        state = State.TOWER1INTAKE1;
                    }
                    break;
                }
                break;
            case ARMED1INTAKE1:
                stomachMotorOn();
                towerMotorOff();
                if (ballCurrentlyAtShooter && ballCurrentlyAtTower && isWrongColorBall(0) && rejectionEnabled) {
                    advanceToTower();
                    state = State.REJECT1TOWER1;
                    break;
                }
                else if (ballCurrentlyAtShooter && !ballCurrentlyAtTower && isWrongColorBall(0) && rejectionEnabled) {
                    state = State.REJECT1INTAKE1;
                    break;
                }
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
            case ARMED1REJECT1:
                towerMotorOff();
                stomachMotorReverse();
                if (!rejectionTimerStarted) {
                    rejectionTimer.reset();
                    rejectionTimerStarted = true;
                }
                if (rejectionTimer.advanceIfElapsed(Constants.INDEXER_INTAKE_REJECTION_TIME)) {
                    intakeReject();
                    rejectionTimerStarted = false;
                    state = State.ARMED1;
                }
                break;
            case ARMED1:
                stomachMotorOff();
                towerMotorOff();
                if (ballCurrentlyAtShooter && ballCurrentlyAtIntake && isWrongColorBall(0) && rejectionEnabled) {
                    intakeBall();
                    state = State.REJECT1INTAKE1;
                    break;
                }
                else if (ballCurrentlyAtShooter && isWrongColorBall(0) && rejectionEnabled) {
                    state = State.REJECT1;
                    break;
                }
                if (ballCurrentlyAtShooter && ballCurrentlyAtIntake) {
                    intakeBall();
                    if (isWrongColorBall(1) && rejectionEnabled) {
                        state = State.ARMED1REJECT1;
                    }
                    else if (shooting) {
                        state = State.SHOOTING1INTAKE1;
                    }
                    else {
                        state = State.ARMED1INTAKE1;
                    }
                }
                else if (shooting) {
                    state = State.SHOOTING1;
                }
                break;
            case ARMED2:
                stomachMotorOff();
                towerMotorOff();
                if (shooting) {
                    state = State.SHOOTING2;
                }
                break;
            case REJECT1INTAKE1:
                if (rejectionTimerStarted) {
                    towerMotorOn();
                }
                else {
                    towerMotorOff();
                }
                stomachMotorOn();
                if (Shooter.getInstance().isAtSetpoint() && Hood.getInstance().atSetpoint() && !rejectionTimerStarted) {
                    rejectionTimer.reset();
                    rejectionTimerStarted = true;
                }
                if (ballCurrentlyAtTower && rejectionTimerStarted && rejectionTimer.advanceIfElapsed(Constants.INDEXER_SHOOTER_REJECTION_TIME)) {
                    shootBall();
                    rejectionTimerStarted = false;
                    state = State.TOWER1;
                }
                else if (ballCurrentlyAtTower) {
                    advanceToTower();
                    state = State.REJECT1TOWER1;
                }
                else if (rejectionTimerStarted && rejectionTimer.advanceIfElapsed(Constants.INDEXER_SHOOTER_REJECTION_TIME)) {
                    shootBall();
                    rejectionTimerStarted = false;
                    state = State.INTAKE1;
                }
                break;
            case REJECT1:
                if (rejectionTimerStarted) {
                    towerMotorOn();
                }
                else {
                    towerMotorOff();
                }
                stomachMotorOff();
                if (Shooter.getInstance().isAtSetpoint() && Hood.getInstance().atSetpoint() && !rejectionTimerStarted) {
                    rejectionTimer.reset();
                    rejectionTimerStarted = true;
                }
                if (ballCurrentlyAtIntake && rejectionTimerStarted && rejectionTimer.advanceIfElapsed(Constants.INDEXER_SHOOTER_REJECTION_TIME)) {
                    shootBall();
                    intakeBall();
                    rejectionTimerStarted = false;
                    state = State.INTAKE1;
                }
                else if (ballCurrentlyAtIntake) {
                    intakeBall();
                    state = State.REJECT1INTAKE1;
                }
                else if (rejectionTimerStarted && rejectionTimer.advanceIfElapsed(Constants.INDEXER_SHOOTER_REJECTION_TIME)) {
                    shootBall();
                    rejectionTimerStarted = false;
                    state = State.FIELD2;
                }
                break;
            case REJECT1TOWER1:
                if (rejectionTimerStarted) {
                    towerMotorOn();
                }
                else {
                    towerMotorOff();
                }
                stomachMotorOff();
                if (Shooter.getInstance().isAtSetpoint() && Hood.getInstance().atSetpoint() && !rejectionTimerStarted) {
                    rejectionTimer.reset();
                    rejectionTimerStarted = true;
                }
                if (rejectionTimerStarted && rejectionTimer.advanceIfElapsed(Constants.INDEXER_SHOOTER_REJECTION_TIME)) {
                    shootBall();
                    rejectionTimerStarted = false;
                    state = State.TOWER1;
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
        if (state == State.REJECT1 || state == State.REJECT1INTAKE1 || state == State.REJECT1TOWER1) {
            Shooter.getInstance().setSetpoint(Constants.SHOOTER_INDEXER_REJECT_SPEED, true);
            Hood.getInstance().setAngle(Constants.HOOD_ANGLE_MAX_RAD, true);
        }
        else {
            Shooter.getInstance().setSetpoint(-1, true);
            Hood.getInstance().setAngle(-1, true);
        }
        if (state == State.ARMED1REJECT1 || state == State.TOWER1REJECT1 || state == State.INTAKE1REJECT1 || (state == State.OVERRIDE && reverse)) {
            CommandScheduler.getInstance().schedule(false, new IntakeReverse());
        }
        else if (isFull() && !DriverStation.isAutonomous()) {
            CommandScheduler.getInstance().schedule(false, new IntakeUpNoInterrupt());
        }
        else if (!DriverStation.isAutonomous()) {
            CommandScheduler.getInstance().schedule(true, new IntakeUp());
        }
        Logger.getInstance().recordOutput("Indexer/Ball0Color", getBall0().getColor().toString());
        Logger.getInstance().recordOutput("Indexer/Ball0Location", getBall0().getLocation().toString());
        Logger.getInstance().recordOutput("Indexer/Ball1Color", getBall1().getColor().toString());
        Logger.getInstance().recordOutput("Indexer/Ball1Location", getBall1().getLocation().toString());
        Logger.getInstance().recordOutput("Indexer/Shooting", shooting);
        Logger.getInstance().recordOutput("Indexer/NumberOfBalls", getBallCount());
        Logger.getInstance().recordOutput("Indexer/IsFull", isFull());
    }

    public boolean getRejection() {
        return state == State.ARMED1REJECT1 || state == State.TOWER1REJECT1 || state == State.INTAKE1REJECT1 || (state == State.OVERRIDE && reverse);
    }

    public void getAllianceColorFMS() {
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

    public Ball getBall1() {
        return balls[1];
    }

    public void resetBalls() {
        balls[0] = new Ball();
        balls[1] = new Ball();
    }

    public boolean isFull() {
        return balls[0].getLocation() != LOCATION.FIELD && balls[1].getLocation() != LOCATION.FIELD && balls[0].getColor() == balls[1].getColor() && balls[0].getColor() == allianceColor;
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
        if (!inputs.colorConnected) {
            color = COLOR.UNKNOWN;
        }
        if (balls[0].getLocation() == LOCATION.FIELD) {
            balls[0].setLocationColor(LOCATION.INTAKE, color);
        }
        else if (balls[1].getLocation() == LOCATION.FIELD) {
            balls[1].setLocationColor(LOCATION.INTAKE, color);
        }
    }

    public void intakeReject() {
        balls[1] = new Ball();
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

    public boolean isWrongColorBall(int index) {
        return (balls[index].getColor() != allianceColor) && (balls[index].getColor() != COLOR.UNKNOWN) && (allianceColor != COLOR.UNKNOWN);
    }

    public void setRejectionEnabled(boolean enabled) {
        this.rejectionEnabled = enabled;
    }

    public boolean allianceIsUnknown() {
        return allianceColor == COLOR.UNKNOWN;
    }
}