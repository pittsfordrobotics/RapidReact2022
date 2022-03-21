package frc.robot.util;

import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDTuner {
    private final String name;
    private PIDController pidController;
    private SparkMaxPIDController sparkMaxPIDController;

    private enum ControllerType {
        WPI, SPARKMAX
    }

    private final ControllerType controllerType;

    public PIDTuner(String name, PIDController pidController) {
        this.name = name;
        this.pidController = pidController;
        controllerType = ControllerType.WPI;
        SmartDashboard.putNumber(name + " P", pidController.getP());
        SmartDashboard.putNumber(name + " I", 0);
        SmartDashboard.putNumber(name + " D", 0);
    }

    public PIDTuner(String name, SparkMaxPIDController sparkMaxPIDController) {
        this.name = name;
        this.sparkMaxPIDController = sparkMaxPIDController;
        controllerType = ControllerType.SPARKMAX;
        SmartDashboard.putNumber(name + " P Tuner", 0);
        SmartDashboard.putNumber(name + " I Tuner", 0);
        SmartDashboard.putNumber(name + " D Tuner", 0);
    }

    public void setP() {
        if (controllerType == ControllerType.WPI) {
            pidController.setP(SmartDashboard.getNumber(name + " P Tuner", 0));
        }
        else {
            sparkMaxPIDController.setP(SmartDashboard.getNumber(name + " P Tuner", 0));
        }
    }

    public void setI() {
        if (controllerType == ControllerType.WPI) {
            pidController.setI(SmartDashboard.getNumber(name + " I Tuner", 0));
        }
        else {
            sparkMaxPIDController.setI(SmartDashboard.getNumber(name + " I Tuner", 0));
        }
    }

    public void setD() {
        if (controllerType == ControllerType.WPI) {
            pidController.setD(SmartDashboard.getNumber(name + " D Tuner", 0));
        }
        else {
            sparkMaxPIDController.setD(SmartDashboard.getNumber(name + " D Tuner", 0));
        }
    }

    public void setPID(boolean P, boolean I, boolean D) {
        if (P) {
            setP();
        }
        if (I) {
            setI();
        }
        if (D) {
            setD();
        }
    }

}