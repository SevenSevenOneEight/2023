package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color8Bit;

import frc.robot.Constants;
import frc.robot.supers.ArmSuper;

public class ArmReal extends ArmSuper {
    private final Mechanism2d armMechanism = new Mechanism2d(3, 1.5);
    private MechanismLigament2d rotationMechanism;
    private MechanismLigament2d extensionMechanism;
    public ArmReal() {
        super();
        configurePIDControllers();
        configureRealArmMechanism();
    }

    @Override
    public void periodic() {
        renderTargetArm();
        renderRealArm();
    }

    private void configurePIDControllers() {
        // TODO tune PID values
        extension.config_kP(0, Constants.ArmConstants.k_extensionP);
        extension.config_kI(0, Constants.ArmConstants.k_extensionI);
        extension.config_kD(0, Constants.ArmConstants.k_extensionD);

        rotation.config_kP(0, Constants.ArmConstants.k_rotationP);
        rotation.config_kI(0, Constants.ArmConstants.k_rotationI);
        rotation.config_kD(0, Constants.ArmConstants.k_rotationD);
    }

    private void configureRealArmMechanism() {
        MechanismRoot2d armRoot = armMechanism.getRoot("arm", 1.525, Constants.ArmConstants.k_rotationHeight);
        rotationMechanism = armRoot.append(
                new MechanismLigament2d("rotation", 0, 0));
        extensionMechanism = targetRotationMechanism.append(
                new MechanismLigament2d("extension", Constants.ArmConstants.k_minExtension, 0, 4, new Color8Bit(80, 100, 120)));
        vacuumIntake = targetExtensionMechanism.append(
                new MechanismLigament2d("vacuum", 0.18, 90, 4, new Color8Bit(80, 80, 100)));
    }

    public void renderRealArm() {
        // TODO calc x & y these are arbitrary values based off encoder CPR & Gearbox Ratios
        double x = 10;
        double y = 15;
        rotationMechanism.setAngle(rotation.getSelectedSensorPosition() * x);
        extensionMechanism.setAngle(extension.getSelectedSensorPosition() * y);
    }
}
