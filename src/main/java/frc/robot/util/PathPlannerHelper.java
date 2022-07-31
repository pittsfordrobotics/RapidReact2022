package frc.robot.util;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Constants;

public class PathPlannerHelper {
    public static Trajectory loadPath(String path) {
        return loadPath(path, false);
    }

    public static Trajectory loadPath(String path, boolean reversed) {
        return PathPlanner.loadPath(path, Constants.DRIVE_MAX_VELOCITY_METERS_PER_SECOND, Constants.DRIVE_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED, reversed);
    }
}