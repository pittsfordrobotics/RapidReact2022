package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.vision.Vision;


public class ClimberSetState extends CommandBase {
    private final Climber climber = Climber.getInstance();
    private final Hood hood = Hood.getInstance();
    private final Intake intake = Intake.getInstance();
    private final Vision vision = Vision.getInstance();
    private final boolean state;

    public ClimberSetState(boolean state) {
        addRequirements(this.climber, this.hood, this.intake, this.vision);
        this.state = state;
    }

    @Override
    public void initialize() {
        climber.setEnabled(state);
        hood.setClimbing(state);
        intake.setClimbing(state);
        vision.setClimbing(state);
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