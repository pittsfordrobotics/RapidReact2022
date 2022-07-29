//package frc.robot;
//
//import edu.wpi.first.math.geometry.Pose2d;
//import edu.wpi.first.math.geometry.Translation2d;
//import edu.wpi.first.math.util.Units;
//import edu.wpi.first.wpilibj.TimeKeeper;
//import org.littletonrobotics.junction.Logger;
//
//import java.util.Map;
//
//public class RobotState {
//    //    TODO: make this work
//    private Pose2d realPose = new Pose2d();
//    private Pose2d drivePose = new Pose2d();
//
//    private double visionDistance = 0.0;
//    private boolean hasTarget = false;
//
//    public void updateDrivePose(Pose2d pose) {
//        drivePose = pose;
//    }
//
//    public void updateVisionDistance(boolean hasTarget, Translation2d math) {
//        this.hasTarget = hasTarget;
//        if (hasTarget) {
//            this.visionDistance = visionDistance;
//        }
//    }
//
//    public void setPose(Pose2d pose) {
//        realPose = pose;
//        drivePose = new Pose2d();
//        visionDistance = 0;
//    }
//
//    private void update() {
//        realPose
//        Logger.getInstance().recordOutput("Odometry/Robot",
//                new double[] {realPose.getX(), realPose.getY(),
//                        realPose.getRotation().getRadians()});
//        Map.Entry<Double, Translation2d> visionEntry = visionData.lastEntry();
//        if (visionEntry != null) {
//            if (visionEntry.getKey() > TimeKeeper.getFPGATimestamp() - maxNoVisionLog) {
//                Logger.getInstance().recordOutput("Odometry/VisionPose",
//                        new double[] {visionEntry.getValue().getX(),
//                                visionEntry.getValue().getY(),
//                                latestPose.getRotation().getRadians()});
//                Logger.getInstance().recordOutput("Odometry/VisionTarget",
//                        new double[] {FieldConstants.hubCenter.getX(),
//                                FieldConstants.hubCenter.getY()});
//                Logger.getInstance().recordOutput("Vision/DistanceInches",
//                        Units.metersToInches(
//                                visionEntry.getValue().getDistance(FieldConstants.hubCenter)));
//            }
//        }
//    }
//
//    public Pose2d getPose() {
//        return pose;
//    }
//}