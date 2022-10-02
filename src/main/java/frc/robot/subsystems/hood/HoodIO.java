// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.hood;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

/** Hood subsystem hardware interface. */
public interface HoodIO {
    /** Contains all of the input data received from hardware. */
    public static class HoodIOInputs implements LoggableInputs {
        public double absolutePositionRad = 0.0;
        public double positionRad = 0.0;
        public double velocityRadPerSec = 0.0;
        public double appliedVolts = 0.0;
        public double[] currentAmps = new double[] {};
        public double[] tempCelcius = new double[] {};

        public void toLog(LogTable table) {
            table.put("AbsolutePositionRad", absolutePositionRad);
            table.put("PositionRad", positionRad);
            table.put("VelocityRadPerSec", velocityRadPerSec);
            table.put("AppliedVolts", appliedVolts);
            table.put("CurrentAmps", currentAmps);
            table.put("TempCelcius", tempCelcius);
        }

        public void fromLog(LogTable table) {
            absolutePositionRad = table.getDouble("AbsolutePositionRad", absolutePositionRad);
            velocityRadPerSec = table.getDouble("VelocityRadPerSec", velocityRadPerSec);
            appliedVolts = table.getDouble("AppliedVolts", appliedVolts);
            currentAmps = table.getDoubleArray("CurrentAmps", currentAmps);
            tempCelcius = table.getDoubleArray("TempCelcius", tempCelcius);
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(HoodIOInputs inputs) {}

    /** Run open loop at the percentage of 12V from -1.0 to 1.0. */
    default void set(double percentage) {}

    /** Run open loop at the specified voltage. */
    default void setVoltage(double volts) {}

    /** Enabling and disabling the soft limit  */
    default void setSoftLimit(boolean enabled, float reverse, float forward) {}

    /** Enable or disable brake mode. */
    default void setBrakeMode(boolean enable) {}
}