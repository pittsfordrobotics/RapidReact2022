package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.util.controller.BetterXboxController;

// TODO: maybe remove
public class ClimberSpeedRight extends CommandBase {
    private final Climber climber = Climber.getInstance();
    private final BetterXboxController operatorController = BetterXboxController.getController(BetterXboxController.Humans.OPERATOR);

    public ClimberSpeedRight() {
        addRequirements(this.climber);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        climber.setRightMotor(operatorController.getBetterRightY()*0.1);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}