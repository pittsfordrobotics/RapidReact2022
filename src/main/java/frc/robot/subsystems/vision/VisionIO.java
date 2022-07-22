package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

/** Vision subsystem hardware interface. */
public interface VisionIO {
    /** The set of loggable inputs for the vision subsystem. */
    class VisionIOInputs implements LoggableInputs {
        public double led = 0.0;
        public double pipeline = 0.0;
        public double camera = 0.0;
        public boolean hasTarget = false;
        public boolean connected = false;
        public double vAngle = 0.0;
        public double hAngle = 0.0;

        public void toLog(LogTable table) {
            table.put("LEDState", led);
            table.put("Pipeline", pipeline);
            table.put("Camera", camera);
            table.put("HasTarget", hasTarget);
            table.put("Connected", connected);
            table.put("AngleV", vAngle);
            table.put("AngleH", vAngle);
        }

        public void fromLog(LogTable table) {
            led = table.getDouble("LEDState", led);
            pipeline = table.getDouble("Pipeline", pipeline);
            camera = table.getDouble("Camera", camera);
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
    default void setLEDS(LED led) {}

    /** Enabled or disabled vision LEDs. */
    default void setCameraModes(CameraMode mode) {}

    /** Sets the pipeline number. */
    default void setPipeline(Pipelines pipeline) {}
}