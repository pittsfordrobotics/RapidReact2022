package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.vision.Vision;


public class ClimberEnable extends CommandBase {
    private final Climber climber = Climber.getInstance();
    private final Hood hood = Hood.getInstance();
    private final Intake intake = Intake.getInstance();
    private final Vision vision = Vision.getInstance();

    public ClimberEnable() {
        addRequirements(this.climber, this.hood, this.intake, this.vision);
    }

    @Override
    public void initialize() {
        climber.setEnabled(true);
        hood.setClimbing(true);
        intake.setClimbing(true);
        vision.setClimbing(true);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}