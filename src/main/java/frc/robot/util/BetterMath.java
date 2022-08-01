package frc.robot.util;

import edu.wpi.first.math.geometry.Rotation2d;

import java.util.List;

/**
 * Contains basic functions that are used often.
 * From 254 Util
 */
public class BetterMath {
    public static final double kEpsilon = 1e-12;

    /**
     * Prevent this class from being instantiated.
     */
    private BetterMath() {}

    /**
     * Limits the given input to the given magnitude.
     */
    public static double limit(double v, double maxMagnitude) {
        return limit(v, -maxMagnitude, maxMagnitude);
    }

    public static double limit(double v, double min, double max) {
        return Math.min(max, Math.max(min, v));
    }

    public static boolean inRange(double v, double maxMagnitude) {
        return inRange(v, -maxMagnitude, maxMagnitude);
    }

    /**
     * Checks if the given input is within the range (min, max), both exclusive.
     */
    public static boolean inRange(double v, double min, double max) {
        return v > min && v < max;
    }

    public static double interpolate(double a, double b, double x) {
        x = limit(x, 0.0, 1.0);
        return a + (b - a) * x;
    }

    public static String joinStrings(final String delim, final List<?> strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); ++i) {
            sb.append(strings.get(i).toString());
            if (i < strings.size() - 1) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    public static boolean epsilonEquals(double a, double b) {
        return epsilonEquals(a, b, kEpsilon);
    }

    public static boolean epsilonEquals(int a, int b, int epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    public static boolean allCloseTo(final List<Double> list, double value, double epsilon) {
        boolean result = true;
        for (Double value_in : list) {
            result &= epsilonEquals(value_in, value, epsilon);
        }
        return result;
    }

    public static double handleDeadband(double value, double deadband) {
        deadband = Math.abs(deadband);
        if (deadband == 1) {
            return 0;
        }
        double scaledValue = (value + (value < 0 ? deadband : -deadband)) / (1 - deadband);
        return (Math.abs(value) > Math.abs(deadband)) ? scaledValue : 0;
    }

    /** returns equal angle between 0 and 360 */
    public static double clamp360(double degrees) {
        return degrees % 360 < 0 ? degrees % 360 + 360 : degrees % 360;
    }

    /** returns equal angle between -180 and 180 */
    public static double clamp180(double degrees) {
        return clamp360(degrees) > 180 ? clamp360(degrees) - 360 : clamp360(degrees);
    }

    public static Rotation2d getShortestRotation(double currentAngle, double wantedAngle) {
        double angle = BetterMath.clamp360(currentAngle);
        double minus = wantedAngle - angle;
        double plus = (360 - Math.abs(wantedAngle - angle)) * -Math.signum(minus);
        if (Math.abs(minus) <= Math.abs(plus)) {
            return Rotation2d.fromDegrees(minus);
        }
        else {
            return Rotation2d.fromDegrees(plus);
        }
    }
}