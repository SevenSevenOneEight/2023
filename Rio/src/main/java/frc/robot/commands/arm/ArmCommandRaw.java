package frc.robot.commands.arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.supers.ArmSuper;

public class ArmCommandRaw extends CommandBase {
    private final ArmSuper arm;
    private final CommandXboxController operatorController;

    public ArmCommandRaw(ArmSuper arm, CommandXboxController operatorController) {
        this.arm = arm;
        this.operatorController = operatorController;
        addRequirements(arm);
    }

    @Override
    public void execute() {
        if(operatorController.getHID().getAButtonPressed()) {
            arm.vacuum.set(1);
        } else if(operatorController.getHID().getBButtonPressed()) {
            arm.vacuum.set(0);
        }
        arm.rotation.set(-operatorController.getLeftY());
        arm.extension.set(-operatorController.getRightY());
    }
}