package frc.robot.subsystems.vision;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CameraPosition;
import frc.robot.FieldConstants;
import frc.robot.RobotState;
import frc.robot.subsystems.vision.VisionIO.CameraMode;
import frc.robot.subsystems.vision.VisionIO.LED;
import frc.robot.subsystems.vision.VisionIO.Pipelines;
import frc.robot.subsystems.vision.VisionIO.VisionIOInputs;
import frc.robot.util.Alert;
import frc.robot.util.Alert.AlertType;
import frc.robot.util.CircleFitter;
import frc.robot.util.GeomUtil;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.List;

public class Vision extends SubsystemBase {
    private final VisionIO io;
    private final VisionIOInputs inputs = new VisionIOInputs();

    private Pipelines pipeline = Pipelines.COMPETITION;
    private LED led = LED.OFF;
    private CameraMode camera = CameraMode.VISION_PROCESSING;

    private double lastTranslationsTimestamp = 0.0;
    private List<Translation2d> lastTranslations = new ArrayList<>();

    private static double lastCaptureTimestamp = 0;
    private static final int minTargetCount = 3; // For calculating odometry
    private static final double extraLatencySecs = 0.06;
    private static final double circleFitPrecision = 0.01;

    private static final double vizMaxNoLog = 0.25; // How long to wait with no vision data before
    // clearing log visualization
    private static final double vizFinalWidth = 1080.0;
    private static final double vizFinalHeight = 1920.0;
    private static final double vizOriginX = 540.0;
    private static final double vizOriginY = 1536.0;
    private static final double vizHeightMeters = 12.0;

    // FOV constants
    private static final double vpw =
            2.0 * Math.tan(Constants.LIMELIGHT_FOV_HOR.getRadians() / 2.0);
    private static final double vph =
            2.0 * Math.tan(Constants.LIMELIGHT_FOV_VER.getRadians() / 2.0);

    private final Alert limelightAlert = new Alert("Limelight not detected! Vision will NOT work!", AlertType.ERROR);

    private static final Vision INSTANCE = new Vision(Constants.ROBOT_VISION_IO);
    public static Vision getInstance() {
        return INSTANCE;
    }

    private Vision(VisionIO io) {
        this.io = io;
        ShuffleboardTab visionTab = Shuffleboard.getTab("Vision");
        visionTab.addBoolean("Has Target", this::hasTarget);
        visionTab.addNumber("Distance", this::getDistance);
        visionTab.addNumber("Horizontal", this::getHorizontal);
        visionTab.addNumber("Vertical", this::getVertical);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Vision", inputs);

        limelightAlert.set(!inputs.connected);

        if (DriverStation.isDisabled() || RobotState.getInstance().isClimbing()) {
            led = LED.OFF;
            camera = CameraMode.DRIVER_CAMERA;
        }
        else {
            led = LED.ON;
            camera = CameraMode.VISION_PROCESSING;
        }

        io.setLEDs(led);
        io.setPipeline(pipeline);
        io.setCameraModes(camera);

        Logger.getInstance().recordOutput("Vision/TargetCount", inputs.cornerX.length / 4);
        processFrame(inputs.cornerX.length / 4);

        // Log individual translations
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        xList.add(vizOriginX);
        yList.add(vizOriginY);
        if (Timer.getFPGATimestamp() - lastTranslationsTimestamp < vizMaxNoLog) {
            double pixelsPerMeter = vizFinalHeight / vizHeightMeters;
            for (Translation2d translation : lastTranslations) {
                double x = vizOriginX - (translation.getY() * pixelsPerMeter);
                double y = vizOriginY - (translation.getX() * pixelsPerMeter);
                x = MathUtil.clamp(x, 0.0, vizFinalWidth);
                y = MathUtil.clamp(y, 0.0, vizFinalHeight);
                xList.add(x);
                yList.add(y);
            }
        }
        Logger.getInstance().recordOutput("Vision/CornersVizX",
                xList.stream().mapToDouble(Double::doubleValue).toArray());
        Logger.getInstance().recordOutput("Vision/CornersVizY",
                yList.stream().mapToDouble(Double::doubleValue).toArray());
    }

    private void processFrame(int targetCount) {
        // Exit if no new frame
        if (inputs.captureTimestamp == lastCaptureTimestamp) {
            return;
        }
        lastCaptureTimestamp = inputs.captureTimestamp;
        double captureTimestamp = inputs.captureTimestamp - extraLatencySecs;

        CameraPosition cameraPosition = Constants.LIMELIGHT_CAMERA_POSITION;

        // Calculate camera to target translation
        if (targetCount >= minTargetCount
                && inputs.cornerX.length == inputs.cornerY.length
                && inputs.cornerX.length % 4 == 0) {

            // Calculate individual corner translations
            List<Translation2d> cameraToTargetTranslations = new ArrayList<>();
            for (int targetIndex = 0; targetIndex < targetCount; targetIndex++) {
                List<VisionPoint> corners = new ArrayList<>();
                double totalX = 0.0, totalY = 0.0;
                for (int i = targetIndex * 4; i < (targetIndex * 4) + 4; i++) {
                    if (i < inputs.cornerX.length && i < inputs.cornerY.length) {
                        corners.add(new VisionPoint(inputs.cornerX[i], inputs.cornerY[i]));
                        totalX += inputs.cornerX[i];
                        totalY += inputs.cornerY[i];
                    }
                }

                VisionPoint targetAvg = new VisionPoint(totalX / 4, totalY / 4);
                corners = sortCorners(corners, targetAvg);

                for (int i = 0; i < corners.size(); i++) {
                    Translation2d translation = solveCameraToTargetTranslation(
                            corners.get(i),
                            i < 2 ? FieldConstants.visionTargetHeightUpper : FieldConstants.visionTargetHeightLower,
                            cameraPosition);
                    if (translation != null) {
                        cameraToTargetTranslations.add(translation);
                    }
                }
            }

            // Save individual translations
            lastTranslationsTimestamp = Timer.getFPGATimestamp();
            lastTranslations = cameraToTargetTranslations;

            // Combine corner translations to full target translation
            if (cameraToTargetTranslations.size() >= minTargetCount * 4) {
                Translation2d cameraToTargetTranslation =
                        CircleFitter.fit(FieldConstants.visionTargetDiameter / 2.0,
                                cameraToTargetTranslations, circleFitPrecision);

                // Calculate field to robot translation
                Rotation2d robotRotation =
                        RobotState.getInstance().getDriveRotation(captureTimestamp);
                Rotation2d cameraRotation = robotRotation
                        .rotateBy(cameraPosition.vehicleToCamera.getRotation());
                Transform2d fieldToTargetRotated =
                        new Transform2d(FieldConstants.hubCenter, cameraRotation);
                Transform2d fieldToCamera = fieldToTargetRotated.plus(GeomUtil
                        .transformFromTranslation(cameraToTargetTranslation.unaryMinus()));
                Pose2d fieldToVehicle = GeomUtil.transformToPose(
                        fieldToCamera.plus(cameraPosition.vehicleToCamera.inverse()));
                if (fieldToVehicle.getX() > FieldConstants.fieldLength
                        || fieldToVehicle.getX() < 0.0
                        || fieldToVehicle.getY() > FieldConstants.fieldWidth
                        || fieldToVehicle.getY() < 0.0) {
                    return;
                }

                // Send final translation
                RobotState.getInstance().addVisionData(captureTimestamp, fieldToVehicle.getTranslation());
            }
        }
    }

    private List<VisionPoint> sortCorners(List<VisionPoint> corners,
                                          VisionPoint average) {

        // Find top corners
        Integer topLeftIndex = null;
        Integer topRightIndex = null;
        double minPosRads = Math.PI;
        double minNegRads = Math.PI;
        for (int i = 0; i < corners.size(); i++) {
            VisionPoint corner = corners.get(i);
            double angleRad =
                    new Rotation2d(corner.x - average.x, average.y - corner.y)
                            .minus(Rotation2d.fromDegrees(90)).getRadians();
            if (angleRad > 0) {
                if (angleRad < minPosRads) {
                    minPosRads = angleRad;
                    topLeftIndex = i;
                }
            } else {
                if (Math.abs(angleRad) < minNegRads) {
                    minNegRads = Math.abs(angleRad);
                    topRightIndex = i;
                }
            }
        }

        // Find lower corners
        Integer lowerIndex1 = null;
        Integer lowerIndex2 = null;
        for (int i = 0; i < corners.size(); i++) {
            boolean alreadySaved = false;
            if (topLeftIndex != null) {
                if (topLeftIndex.equals(i)) {
                    alreadySaved = true;
                }
            }
            if (topRightIndex != null) {
                if (topRightIndex.equals(i)) {
                    alreadySaved = true;
                }
            }
            if (!alreadySaved) {
                if (lowerIndex1 == null) {
                    lowerIndex1 = i;
                } else {
                    lowerIndex2 = i;
                }
            }
        }

        // Combine final list
        List<VisionPoint> newCorners = new ArrayList<>();
        if (topLeftIndex != null) {
            newCorners.add(corners.get(topLeftIndex));
        }
        if (topRightIndex != null) {
            newCorners.add(corners.get(topRightIndex));
        }
        if (lowerIndex1 != null) {
            newCorners.add(corners.get(lowerIndex1));
        }
        if (lowerIndex2 != null) {
            newCorners.add(corners.get(lowerIndex2));
        }
        return newCorners;
    }

    private Translation2d solveCameraToTargetTranslation(VisionPoint corner,
                                                         double goalHeight, CameraPosition cameraPosition) {

        double halfWidthPixels = Constants.LIMELIGHT_WIDTH_PIXELS / 2.0;
        double halfHeightPixels = Constants.LIMELIGHT_HEIGHT_PIXELS / 2.0;
        double nY = -((corner.x - halfWidthPixels - Constants.LIMELIGHT_CROSSHAIR_X)
                / halfWidthPixels);
        double nZ = -((corner.y - halfHeightPixels - Constants.LIMELIGHT_CROSSHAIR_Y)
                / halfHeightPixels);

        Translation2d xzPlaneTranslation = new Translation2d(1.0, vph / 2.0 * nZ)
                .rotateBy(cameraPosition.verticalRotation);
        double x = xzPlaneTranslation.getX();
        double y = vpw / 2.0 * nY;
        double z = xzPlaneTranslation.getY();

        double differentialHeight = cameraPosition.cameraHeight - goalHeight;
        if ((z < 0.0) == (differentialHeight > 0.0)) {
            double scaling = differentialHeight / -z;
            double distance = Math.hypot(x, y) * scaling;
            Rotation2d angle = new Rotation2d(x, y);
            return new Translation2d(distance * angle.getCos(),
                    distance * angle.getSin());
        }
        return null;
    }

    public static class VisionPoint {
        public final double x;
        public final double y;

        public VisionPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class TimestampedTranslation2d {
        public final double timestamp;
        public final Translation2d translation;

        public TimestampedTranslation2d(double timestamp,
                                        Translation2d translation) {
            this.timestamp = timestamp;
            this.translation = translation;
        }
    }

    public boolean hasTarget() {
        return inputs.hasTarget;
    }

    public double getHorizontal() {
        return inputs.hAngle;
    }

    public double getVertical() {
        return inputs.vAngle;
    }

    public void setPipeline(Pipelines pipeline) {
        this.pipeline = pipeline;
    }

    public void setLED(LED led) {
        this.led = led;
    }

    public void setCamMode(CameraMode camera) {
        this.camera = camera;
    }

    @Deprecated
    // switch to full field odometry
    public double getDistance() {
        return (FieldConstants.visionTargetHeightCenter - Constants.LIMELIGHT_VEHICLE_TO_CAMERA_Z) / Math.tan(Constants.LIMELIGHT_CAMERA_ANGLE.getDegrees() + getVertical());
    }
}