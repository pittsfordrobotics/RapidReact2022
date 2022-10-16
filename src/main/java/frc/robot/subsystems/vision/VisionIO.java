package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

/** Vision subsystem hardware interface. */
public interface VisionIO {
    /** The set of loggable inputs for the vision subsystem. */
    class VisionIOInputs implements LoggableInputs {
        public double captureTimestamp = 0.0;
        public double[] cornerX = new double[] {};
        public double[] cornerY = new double[] {};
        public boolean hasTarget = false;
        public boolean connected = false;
        public double vAngle = 0.0;
        public double hAngle = 0.0;

        public void toLog(LogTable table) {
            table.put("CaptureTimestamp", captureTimestamp);
            table.put("CornerX", cornerX);
            table.put("CornerY", cornerY);
            table.put("HasTarget", hasTarget);
            table.put("Connected", connected);
            table.put("AngleV", vAngle);
            table.put("AngleH", vAngle);
        }

        public void fromLog(LogTable table) {
            captureTimestamp = table.getDouble("CaptureTimestamp", captureTimestamp);
            cornerX = table.getDoubleArray("CornerX", cornerX);
            cornerY = table.getDoubleArray("CornerY", cornerY);
            hasTarget = table.getBoolean("HasTarget", hasTarget);
            connected = table.getBoolean("Connected", connected);
            vAngle = table.getDouble("AngleV", vAngle);
            hAngle = table.getDouble("AngleH", hAngle);
        }
    }

    enum Pipelines {
        COMPETITION(0);

        private final int num;
        Pipelines(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    enum LED {
        PIPELINE(0), OFF(1), BLINK(2), ON(3);

        private final int num;

        LED(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    enum CameraMode {
        VISION_PROCESSING(0), DRIVER_CAMERA(1);

        private final int num;

        CameraMode(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    /** Updates the set of loggable inputs. */
    default void updateInputs(VisionIOInputs inputs) {}

    /** Enabled or disabled vision LEDs. */
    default void setLEDs(LED led) {}

    /** Enabled or disabled vision LEDs. */
    default void setCameraModes(CameraMode mode) {}

    /** Sets the pipeline number. */
    default void setPipeline(Pipelines pipeline) {}
}