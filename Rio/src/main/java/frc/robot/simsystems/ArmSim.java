package frc.robot.simsystems;

import frc.robot.supers.ArmSuper;

public class ArmSim extends ArmSuper {
    public ArmSim() {
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
