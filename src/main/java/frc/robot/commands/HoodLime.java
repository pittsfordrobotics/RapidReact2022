package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.random.Constants;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Limelight;


public class HoodLime extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Limelight limelight = Limelight.getInstance();

    public HoodLime() {
        addRequirements(this.hood, this.limelight);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        hood.setPosition(Constants.HOOD_TABLE.lookup(limelight.getDistance()));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}