package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ball;
import frc.robot.Ball.LOCATION;
import frc.robot.Ball.COLOR;
import java.util.ArrayList;
import static frc.robot.Constants.*;

public class Indexer extends SubsystemBase {
    private final ColorSensorV3 colorSensor = new ColorSensorV3(INDEXER_COLOR);
    private final DigitalInput beamBreakTower = new DigitalInput(INDEXER_BEAM_TOWER);
    private final DigitalInput beamBreakShooter = new DigitalInput(INDEXER_BEAM_SHOOTER);

    private final ArrayList<Ball> balls = new ArrayList<>(2);

    private final static Indexer INSTANCE = new Indexer();
    public static Indexer getInstance() {
        return INSTANCE;
    }

    private Indexer() {
        balls.add(new Ball());
        balls.add(new Ball());
    }

    public void resetBalls() {
        balls.set(0, new Ball());
        balls.set(1, new Ball());
    }

    public boolean atMaxBalls() {
        return balls.size() == 2;
    }

    public boolean ballAtIntake() {
        return colorSensor.getProximity() > INDEXER_COLOR_PROXIMITY;
    }

    public boolean ballAtTower() {
        return beamBreakTower.get();
    }

    public boolean ballAtShooter() {
        return beamBreakShooter.get();
    }

    public void intakeBall() {
        COLOR color = colorSensor.getRed() > colorSensor.getBlue() ? COLOR.RED : COLOR.BLUE;
        if (balls.get(0).getLocation() == LOCATION.FIELD) {
            balls.get(0).setLocationColor(LOCATION.INTAKE, color);
        }
        else {
            balls.get(1).setLocationColor(LOCATION.INTAKE, color);
        }
    }

    public void shootBall() {
        balls.set(0, balls.get(1));
        balls.set(1, new Ball());
    }

    public void advanceToTower() {
        if (balls.get(0).getLocation() != LOCATION.TOWER) {
            balls.get(0).setLocation(LOCATION.TOWER);
        }
        else {
            balls.get(1).setLocation(LOCATION.TOWER);
        }
    }

    public void advanceToShooter() {
        balls.get(0).setLocation(LOCATION.SHOOTER);
    }

    public boolean readyToShoot() {
        return balls.get(0).getLocation() == LOCATION.SHOOTER;
    }

}