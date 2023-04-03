package frc.robot.simsystems;

import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color8Bit;
import frc.robot.Constants;
import frc.robot.supers.ArmSuper;

public class ArmSim extends ArmSuper {
    public ArmSim() {
        super();
    }

    @Override
    public void periodic() {
        renderTargetArm();
    }
}
