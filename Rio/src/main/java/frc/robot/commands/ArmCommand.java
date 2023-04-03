package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.supers.ArmSuper;

public class ArmCommand extends CommandBase {
    private final ArmSuper arm;
    private final CommandXboxController operatorController;

    public ArmCommand(ArmSuper arm, CommandXboxController operatorController) {
        this.arm = arm;
        this.operatorController = operatorController;
        addRequirements(arm);
    }

    @Override
    public void execute() {
        arm.adjustTargetPosition(new ArmSuper.ArmPosition(
                -1.5 * operatorController.getLeftY(),
                -0.01 * operatorController.getRightY()
        ));
    }
}