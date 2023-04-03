package frc.robot.simsystems;

import frc.robot.supers.ArmSuper;

public class ArmSim extends ArmSuper {
    public ArmSim() {
        super();
    }

    @Override
    public void periodic() {
        renderMechanisms();
    }
}
