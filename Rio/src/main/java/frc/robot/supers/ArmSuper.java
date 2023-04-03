package frc.robot.supers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants;

public abstract class ArmSuper extends SubsystemBase {
    // Arm Motors
    public final WPI_TalonFX rotation = new WPI_TalonFX(Constants.ArmConstants.k_rotationID);
    public final WPI_TalonFX extension = new WPI_TalonFX(Constants.ArmConstants.k_extensionID);
    public final CANSparkMax vacuum = new CANSparkMax(Constants.ArmConstants.k_vacuumID, CANSparkMaxLowLevel.MotorType.kBrushless);

    // Arm Position
    public static class ArmPosition {
        public double rotation;
        public double extension;
        public ArmPosition(double rotation, double extension) {
            this.rotation = rotation;
            this.extension = extension;
        }
    }

    // Arm Target Position Mechanism
    public final Mechanism2d targetArmMechanism = new Mechanism2d(3, 1.5);
    public MechanismLigament2d targetExtensionMechanism;
    public MechanismLigament2d targetRotationMechanism;
    public MechanismLigament2d vacuumIntake;

    // Constructor
    public ArmSuper() {
        configureMotors();
        configureTargetArmMechanism();
        setTargetPosition(new ArmPosition(-10, 0.4));
    }

    // functions
    public void configureMotors() {
        extension.setInverted(false);
        rotation.setInverted(false);
        vacuum.setInverted(false);

        extension.setNeutralMode(Constants.ArmConstants.k_neutralMode);
        rotation.setNeutralMode(Constants.ArmConstants.k_neutralMode);
        vacuum.setIdleMode(Constants.ArmConstants.k_idleMode);

        vacuum.setSmartCurrentLimit(Constants.ArmConstants.k_vacuumCurrentLimit);

        extension.configForwardSoftLimitEnable(true);
        extension.configSetParameter(360, 40, 0, 0, 0);
        extension.configSetParameter(361, 100, 0, 0, 0);
        extension.configSetParameter(362, 60, 0, 0, 0);
        rotation.configForwardSoftLimitEnable(true);
        rotation.configSetParameter(360, 40, 0, 0, 0);
        rotation.configSetParameter(361, 100, 0, 0, 0);
        rotation.configSetParameter(362, 60, 0, 0, 0);
    }
    public void configureTargetArmMechanism() {
        MechanismRoot2d armRoot = targetArmMechanism.getRoot("arm", 1.525, Constants.ArmConstants.k_rotationHeight);
        targetRotationMechanism = armRoot.append(
                new MechanismLigament2d("rotation", 0, 0));
        targetExtensionMechanism = targetRotationMechanism.append(
                new MechanismLigament2d("extension", Constants.ArmConstants.k_minExtension, 0, 4, new Color8Bit(40, 50, 60)));
        vacuumIntake = targetExtensionMechanism.append(
                new MechanismLigament2d("vacuum", 0.18, 90, 4, new Color8Bit(40, 40, 50)));
    }
    public void setTargetPosition(ArmPosition armMechanism) {
        targetRotationMechanism.setAngle(Math.min(
                Math.max(Constants.ArmConstants.k_minRotation, armMechanism.rotation),
                Constants.ArmConstants.k_maxRotation)
        );
        targetExtensionMechanism.setLength(Math.min(
                Math.max(Constants.ArmConstants.k_minExtension, armMechanism.extension),
                Constants.ArmConstants.k_maxExtension)
        );
    }
    public void adjustTargetPosition(ArmPosition armMechanism) {
        setTargetPosition(new ArmPosition(
                targetRotationMechanism.getAngle() + armMechanism.rotation,
                targetExtensionMechanism.getLength() + armMechanism.extension
        ));
    }
    public void renderTargetArm() {
        SmartDashboard.putData("Rendering/Target Arm", targetArmMechanism);
    }
}
