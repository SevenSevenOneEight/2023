package frc.robot.subsystems;

import frc.robot.supers.ArmSuper;

public class ArmReal extends ArmSuper {
    public ArmReal() {
        configureMotors();
    }

    @Override
    public void periodic() {
        renderMechanisms();
    }

    public void renderMechanisms() {
        // TODO
    }
}
