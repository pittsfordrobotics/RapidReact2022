package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.BetterMath;
import org.littletonrobotics.junction.Logger;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/** Manages the robot's pose based on state data from various subsystems. Also manages climbing. */
public class RobotState {
    private static final double historyLengthSecs = 1.0;

    /** How long to wait with no vision data before clearing log visualization */
    private static final double maxNoVisionLog = 0.25;
    private static final double visionShiftPerSec = 0.999; // After one second of vision data, what %
    // of pose average should be vision
    private static final double visionMaxAngularVelocity =
            Units.degreesToRadians(180.0); // Max angular velocity before vision data is rejected

    private final TreeMap<Double, Twist2d> driveData = new TreeMap<>(); // Relative movement per cycle
    private final TreeMap<Double, Translation2d> visionData = new TreeMap<>(); // Field to vehicle

    private Pose2d basePose = new Pose2d();
    private Pose2d latestPose = new Pose2d();
    private boolean resetOnNextVision = false;
    private boolean snapped = false;
    private boolean climbing = false;

    private static final RobotState INSTANCE = new RobotState();
    public static RobotState getInstance() {
        return INSTANCE;
    }

    private RobotState() {}

    /** Records a new drive movement. */
    public void addDriveData(double timestamp, Twist2d twist) {
        driveData.put(timestamp, twist);
        update();
    }

    /** Records a new vision frame. */
    public void addVisionData(double timestamp, Translation2d translation) {
        if (resetOnNextVision) {
            resetPose(new Pose2d(translation, getLatestRotation()));
            resetOnNextVision = false;
        } else {
            visionData.put(timestamp, translation);
            update();
        }
    }

    /** Returns the latest robot pose based on drive and vision data. */
    public Pose2d getLatestPose() {
        return latestPose;
    }

    /** Returns the latest robot rotation. */
    public Rotation2d getLatestRotation() {
        return latestPose.getRotation();
    }

    /** Resets the odometry to a known pose. */
    public void resetPose(Pose2d pose) {
        basePose = pose;
        driveData.clear();
        visionData.clear();
        update();
    }

    /** Resets the odometry once the first vision data is received. */
    public void resetOnNextVision() {
        resetOnNextVision = true;
    }

    /** Returns whether the odometry has been reset based on vision data. */
    public boolean getVisionResetComplete() {
        return !resetOnNextVision;
    }

    /** Clears old data and calculates the latest pose. */
    private void update() {
        // Clear old drive data
        while (driveData.size() > 1 && driveData
                .firstKey() < Timer.getFPGATimestamp() - historyLengthSecs) {
            basePose = getPose(driveData.higherKey(driveData.firstKey()));
            driveData.pollFirstEntry();
        }

        // Clear old vision data
        while (visionData.size() > 0
                && visionData.firstKey() < driveData.firstKey()) {
            // Any vision data before first drive data won't be used in calculations
            visionData.pollFirstEntry();
        }

        // Update latest pose
        latestPose = getPose(null);

        // Log poses
        Logger.getInstance().recordOutput("Odometry/Robot",
                new double[] {latestPose.getX(), latestPose.getY(),
                        latestPose.getRotation().getRadians()});
        Logger.getInstance().recordOutput("Odometry/Quadrant", getQuad());
        Logger.getInstance().recordOutput("Odometry/DistanceToHubMeters", getDistanceToHub());
        Logger.getInstance().recordOutput("Odometry/DegreesToHub", getRotationToHub().getDegrees());
        Logger.getInstance().recordOutput("Odometry/ShortestDegreesToHub", getShortestRotationToHub().getDegrees());
        Logger.getInstance().recordOutput("Odometry/LatestDegrees", getLatestRotation().getDegrees());
        Logger.getInstance().recordOutput("Odometry/Degrees360", BetterMath.clamp360(getLatestRotation().getDegrees()));
        Map.Entry<Double, Translation2d> visionEntry = visionData.lastEntry();
        if (visionEntry != null) {
            if (visionEntry.getKey() > Timer.getFPGATimestamp() - maxNoVisionLog) {
                Logger.getInstance().recordOutput("Odometry/VisionPose",
                        new double[] {visionEntry.getValue().getX(),
                                visionEntry.getValue().getY(),
                                latestPose.getRotation().getRadians()});
                Logger.getInstance().recordOutput("Odometry/VisionTarget",
                        new double[] {FieldConstants.hubCenter.getX(),
                                FieldConstants.hubCenter.getY()});
                Logger.getInstance().recordOutput("Vision/DistanceInches",
                        Units.metersToInches(
                                visionEntry.getValue().getDistance(FieldConstants.hubCenter)));
            }
        }
    }

    /**
     * Calculates the pose at the specified timestamp by combining drive and vision data. Data is not
     * interpolated between drive cycles; use getDriveRotation() for precise rotation data.
     */
    public Pose2d getPose(Double timestamp) {

        // Get drive data in range
        SortedMap<Double, Twist2d> filteredDriveData;
        if (timestamp == null) {
            filteredDriveData = driveData;
        } else {
            filteredDriveData = driveData.headMap(timestamp);
        }

        // Process drive and vision data
        Pose2d pose = basePose;
        for (Map.Entry<Double, Twist2d> driveEntry : filteredDriveData.entrySet()) {

            // Get the next timestamp and relevant vision data
            Double nextTimestamp = driveData.higherKey(driveEntry.getKey());
            if (nextTimestamp == null) {
                nextTimestamp = driveEntry.getKey() + 0.02; // 0.02 s = 20 ms
            }
            SortedMap<Double, Translation2d> filteredVisionData =
                    visionData.subMap(driveEntry.getKey(), nextTimestamp);

            // Apply vision data
            for (Map.Entry<Double, Translation2d> visionEntry : filteredVisionData
                    .entrySet()) {

                // Calculate vision shift based on angular velocity
                double angularVelocityRadPerSec = driveEntry.getValue().dtheta
                        / (nextTimestamp - driveEntry.getKey());
                double angularErrorScale =
                        Math.abs(angularVelocityRadPerSec) / visionMaxAngularVelocity;
                angularErrorScale = MathUtil.clamp(angularErrorScale, 0, 1);
                double visionShift = 1 - Math.pow(1 - visionShiftPerSec,
                        1 / Constants.LIMELIGHT_TARGET_FRAMERATE);
                visionShift *= 1 - angularErrorScale;

                // Adjust pose
                pose = new Pose2d(
                        pose.getX() * (1 - visionShift)
                                + visionEntry.getValue().getX() * visionShift,
                        pose.getY() * (1 - visionShift)
                                + visionEntry.getValue().getY() * visionShift,
                        pose.getRotation());
            }

            // Apply drive twist
            pose = pose.exp(driveEntry.getValue());
        }
        return pose;
    }

    /**
     * Interpolates to find the drive rotation at the specified timestamp. This only uses the drive
     * data since the gyro angle is unaffected by vision data.
     */
    public Rotation2d getDriveRotation(double timestamp) {
        Rotation2d rotation = basePose.getRotation();
        for (Map.Entry<Double, Twist2d> entry : driveData.headMap(timestamp)
                .entrySet()) {
            Double nextTimestamp = driveData.higherKey(entry.getKey());
            if (nextTimestamp != null && nextTimestamp > timestamp) { // Last twist, apply partial
                double t =
                        (timestamp - entry.getKey()) / (nextTimestamp - entry.getKey());
                rotation = rotation.plus(new Rotation2d(entry.getValue().dtheta * t));
            } else { // Apply full twist
                rotation = rotation.plus(new Rotation2d(entry.getValue().dtheta));
            }
        }
        return rotation;
    }

    /**
     *  Quadrants
     *  4   3
     *  1   2
     */
    public int getQuad() {
        if (latestPose.getY() <= FieldConstants.fieldWidth / 2) {
            if (latestPose.getX() < FieldConstants.fieldLength / 2) {
                return 1;
            }
            else {
                return 2;
            }
        }
        else {
            if (latestPose.getX() < FieldConstants.fieldLength / 2) {
                return 4;
            }
            else {
                return 3;
            }
        }
    }

    public Rotation2d getRotationToHub() {
        double angle = Units.radiansToDegrees(Math.atan((latestPose.getY()-FieldConstants.hubCenter.getY())/(latestPose.getX()-FieldConstants.hubCenter.getX())));
        switch (getQuad()) {
            case 1:
                return Rotation2d.fromDegrees(angle);
            case 2:
            case 3:
                return Rotation2d.fromDegrees(angle + 180);
            case 4:
                return Rotation2d.fromDegrees(angle + 360);
        }
        return null;
    }

    /**
     * Positive = Counterclockwise
     * Negative = Clockwise
     */
    public Rotation2d getShortestRotationToHub() {
        double angle = BetterMath.clamp360(latestPose.getRotation().getDegrees());
        double minus = getRotationToHub().getDegrees() - angle;
        double plus = (360 - Math.abs(getRotationToHub().getDegrees() - angle)) * -Math.signum(minus);
        if (Math.abs(minus) <= Math.abs(plus)) {
            return Rotation2d.fromDegrees(minus);
        }
        else {
            return Rotation2d.fromDegrees(plus);
        }
    }

    public double getDistanceToHub() {
        return Math.sqrt(Math.pow(latestPose.getX()-FieldConstants.hubCenter.getX(), 2) + Math.pow(latestPose.getY()-FieldConstants.hubCenter.getY(), 2));
    }

    public void setClimbing(boolean climbing) {
        this.climbing = climbing;
        if (climbing) snapped = false;
    }

    public boolean isClimbing() {
        return climbing;
    }

    public boolean isSnapped() {
        return snapped;
    }

    public void setSnapped(boolean snapped) {
        this.snapped = snapped;
    }
}