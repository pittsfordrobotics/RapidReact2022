package frc.robot.subsystems.drive;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

/** Drive subsystem hardware interface. */
public interface DriveIO {
    /** The set of loggable inputs for the drive subsystem. */
    class DriveIOInputs implements LoggableInputs {
        public double leftPositionRad = 0.0;
        public double leftVelocityRadPerSec = 0.0;
        public double leftAppliedVolts = 0.0;
        public double[] leftCurrentAmps = new double[] {};
        public double[] leftTempCelcius = new double[] {};

        public double rightPositionRad = 0.0;
        public double rightVelocityRadPerSec = 0.0;
        public double rightAppliedVolts = 0.0;
        public double[] rightCurrentAmps = new double[] {};
        public double[] rightTempCelcius = new double[] {};

        public int gyroUpTime = 0;
        public double gyroYawPositionRad = 0.0;
        public double gyroYawVelocityRadPerSec = 0.0;
        public double gyroPitchPositionRad = 0.0;
        public double gyroRollPositionRad = 0.0;

        public void toLog(LogTable table) {
            table.put("LeftPositionRad", leftPositionRad);
            table.put("LeftVelocityRadPerSec", leftVelocityRadPerSec);
            table.put("LeftAppliedVolts", leftAppliedVolts);
            table.put("LeftCurrentAmps", leftCurrentAmps);
            table.put("LeftTempCelcius", leftTempCelcius);

            table.put("RightPositionRad", rightPositionRad);
            table.put("RightVelocityRadPerSec", rightVelocityRadPerSec);
            table.put("RightAppliedVolts", rightAppliedVolts);
            table.put("RightCurrentAmps", rightCurrentAmps);
            table.put("RightTempCelcius", rightTempCelcius);

            table.put("GyroUpTime", gyroUpTime);
            table.put("GyroYawPositionRad", gyroYawPositionRad);
            table.put("GyroYawVelocityRadPerSec", gyroYawVelocityRadPerSec);
            table.put("GyroPitchPositionRad", gyroPitchPositionRad);
            table.put("GyroRollPositionRad", gyroRollPositionRad);
        }

        public void fromLog(LogTable table) {
            leftPositionRad = table.getDouble("LeftPositionRad", leftPositionRad);
            leftVelocityRadPerSec = table.getDouble("LeftVelocityRadPerSec", leftVelocityRadPerSec);
            leftAppliedVolts = table.getDouble("LeftAppliedVolts", leftAppliedVolts);
            leftCurrentAmps = table.getDoubleArray("LeftCurrentAmps", leftCurrentAmps);
            leftTempCelcius = table.getDoubleArray("LeftTempCelcius", leftTempCelcius);

            rightPositionRad = table.getDouble("RightPositionRad", rightPositionRad);
            rightVelocityRadPerSec = table.getDouble("RightVelocityRadPerSec", rightVelocityRadPerSec);
            rightAppliedVolts = table.getDouble("RightAppliedVolts", rightAppliedVolts);
            rightCurrentAmps = table.getDoubleArray("RightCurrentAmps", rightCurrentAmps);
            rightTempCelcius = table.getDoubleArray("RightTempCelcius", rightTempCelcius);

            gyroUpTime = table.getInteger("GyroUpTime", gyroUpTime);
            gyroYawPositionRad = table.getDouble("GyroYawPositionRad", gyroYawPositionRad);
            gyroYawVelocityRadPerSec = table.getDouble("GyroYawVelocityRadPerSec", gyroYawVelocityRadPerSec);
            gyroPitchPositionRad = table.getDouble("GyroPitchPositionRad", gyroPitchPositionRad);
            gyroRollPositionRad = table.getDouble("GyroRollPositionRad", gyroRollPositionRad);
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(DriveIOInputs inputs) {}

    /** Run open loop at the percentage of 12V from -1.0 to 1.0. */
    default void set(double leftPercent, double rightPercent) {}

    /** Run open loop at the specified voltage. */
    default void setVoltage(double leftVolts, double rightVolts) {}

    /** Enable or disable brake mode. */
    default void setBrakeMode(boolean enable) {}

    /**
     * Resets encoders to 0
     */
    default void resetEncoders() {}
}