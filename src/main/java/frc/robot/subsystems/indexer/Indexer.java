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
    private boolean ballAtShooter = false;
    private boolean ballStillAtIntake = false;
    private boolean ballStillAtTower = false;
    private final Timer rejectionTimer = new Timer();
    private boolean rejectionTimerStarted = false;
    private boolean rejectionEnabled = false; // TODO: enable this

    private final Ball[] balls = {new Ball(), new Ball()};

    private enum SystemState {
        DISABLED, STOMACH, TOWER, STOMACH_TOWER, STOMACH_REVERSE
    }
    private enum BallState {
        DISABLED, FIELD2, REJECT, BALL1, BALL2, OVERRIDE
    }
    private SystemState systemState = SystemState.DISABLED;
    private BallState ballState = BallState.DISABLED;

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
        indexerTab.addString("Indexer state", () -> systemState.toString());
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
        Logger.getInstance().recordOutput("Indexer/SystemState", systemState.toString());
        Logger.getInstance().recordOutput("Indexer/BallState", ballState.toString());
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
        switch (systemState) {
            case STOMACH:
                stomachMotorOn();
                towerMotorOff();
                break;
            case TOWER:
                stomachMotorOff();
                towerMotorOn();
                break;
            case STOMACH_TOWER:
                stomachMotorOn();
                towerMotorOn();
                break;
            case STOMACH_REVERSE:
                towerMotorOff();
                stomachMotorReverse();
                break;
            case DISABLED:
            default:
                stomachMotorOff();
                towerMotorOff();
        }
        switch (ballState) {
            case FIELD2:
                systemState = SystemState.DISABLED;
                if (ballCurrentlyAtIntake && !isWrongColorBall()) {
                    systemState = SystemState.STOMACH_TOWER;
                } else if (ballCurrentlyAtIntake && isWrongColorBall()) {
                    systemState = SystemState.STOMACH_TOWER;
                }
                break;
            case BALL1:
                if (ballCurrentlyAtShooter) {
                    systemState = SystemState.DISABLED;
                }
                if (ballCurrentlyAtIntake && !isWrongColorBall()) {
                    systemState = SystemState.STOMACH;
                    ballState = BallState.BALL2;
                } else if (ballCurrentlyAtIntake && isWrongColorBall()) {
                    systemState = SystemState.STOMACH_REVERSE;
                    rejectionTimer.start();
                    rejectionTimerStarted = true;
                }
                if (rejectionTimerStarted) {
                    CommandScheduler.getInstance().schedule(false, new IntakeReverse());
                    if (rejectionTimer.hasElapsed(Constants.INDEXER_INTAKE_REJECTION_TIME)) {
                        rejectionTimer.stop();
                        rejectionTimer.reset();
                        rejectionTimerStarted = false;
                        systemState = SystemState.DISABLED;
                    }
                }
                if (shooting) {
                    systemState = SystemState.STOMACH_TOWER;
                    if (ballCurrentlyAtShooter) {
                        ballAtShooter = true;
                    } else if (ballAtShooter) {
                        ballAtShooter = false;
                        ballState = BallState.FIELD2;
                    }
                }
                break;
            case REJECT:
                if (ballCurrentlyAtShooter) {
                    systemState = SystemState.DISABLED;
                    Shooter.getInstance().setSetpoint(Constants.SHOOTER_INDEXER_REJECT_SPEED, true);
                    Hood.getInstance().setAngle(Constants.HOOD_ANGLE_MAX, true);
                    if (Shooter.getInstance().isAtSetpoint() && Hood.getInstance().atGoal()) {
                        rejectionTimer.start();
                    }
                }
                if (rejectionTimer.hasElapsed(Constants.INDEXER_SHOOTER_REJECTION_TIME)) {
                    systemState = SystemState.DISABLED;
                    ballState = BallState.FIELD2;
                    Shooter.getInstance().setSetpoint(-1, true);
                    Hood.getInstance().setAngle(-1, true);
                    rejectionTimer.stop();
                    rejectionTimer.reset();
                }
                break;
            case BALL2:
                if (ballCurrentlyAtTower) {
                    systemState = SystemState.DISABLED;
                }
                if (shooting) {
                    systemState = SystemState.STOMACH_TOWER;
                    if (!ballCurrentlyAtShooter) {
                        ballState = BallState.BALL1;
                    }
                }
                break;
            case OVERRIDE:
                systemState = SystemState.STOMACH_TOWER;
                break;
            case DISABLED:
            default:
                systemState = SystemState.DISABLED;
        }

        if (isFull() && !DriverStation.isAutonomous()) {
            CommandScheduler.getInstance().schedule(false, new IntakeUpNoInterrupt());
        }
        else if (!DriverStation.isAutonomous()) {
            CommandScheduler.getInstance().schedule(true, new IntakeUp());
        }
        Logger.getInstance().recordOutput("Indexer/Shooting", shooting);
        Logger.getInstance().recordOutput("Indexer/IsFull", isFull());
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
        return systemState == SystemState.DISABLED;
    }

    public void resetEverything() {
        systemState = SystemState.DISABLED;
        ballState = BallState.FIELD2;
    }

    public void override() {
        ballState = BallState.OVERRIDE;
    }


    public void addBallToTower() {
        ballState = BallState.BALL1;
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

    public void towerMotorOff() {
        io.setTower(0);
    }

    public boolean isFull() {
        return ballState == BallState.BALL2;
    }

    public boolean isEmpty() {
        return ballState == BallState.FIELD2;
    }

    public boolean isOneBall() {
        return ballState == BallState.BALL1;
    }

    public int getBallCount() {
        if (ballState == BallState.FIELD2) return 0;
        if (ballState == BallState.BALL1) return 1;
        if (ballState == BallState.BALL2) return 2;
        return 0;
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

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean ableToShoot() {
        return ballState != BallState.REJECT && ballState != BallState.DISABLED && ballState != BallState.FIELD2;
    }

    public boolean isWrongColorBall() {
        COLOR color = inputs.colorRed > inputs.colorBlue ? COLOR.RED : COLOR.BLUE;
        return (allianceColor != COLOR.UNKNOWN) && (allianceColor != color);
    }

    public void setRejectionEnabled(boolean enabled) {
        this.rejectionEnabled = enabled;
    }
}