package frc.robot.supers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants;

public abstract class ArmSuper extends SubsystemBase {
    // Arm Motors
    public final WPI_TalonFX extension = new WPI_TalonFX(Constants.ArmConstants.k_extensionID);
    public final WPI_TalonFX rotation = new WPI_TalonFX(Constants.ArmConstants.k_rotationID);
    public final CANSparkMax vacuum = new CANSparkMax(Constants.ArmConstants.k_vacuumID, CANSparkMaxLowLevel.MotorType.kBrushless);

    // functions
    public void configureMotors() {
        extension.setInverted(false);
        rotation.setInverted(false);
        vacuum.setInverted(false);

        extension.setNeutralMode(Constants.ArmConstants.k_neutralMode);
        rotation.setNeutralMode(Constants.ArmConstants.k_neutralMode);
        vacuum.setIdleMode(Constants.ArmConstants.k_idleMode);

        vacuum.setSmartCurrentLimit(Constants.ArmConstants.k_currentLimit);

        /*eContinuousCurrentLimitAmps(360),
	    ePeakCurrentLimitMs(361),
	    ePeakCurrentLimitAmps(362),*/
        extension.configForwardSoftLimitEnable(true);
        extension.configSetParameter(360, 40, 0, 0, 0);
        extension.configSetParameter(361, 100, 0, 0, 0);
        extension.configSetParameter(362, 60, 0, 0, 0);
        rotation.configForwardSoftLimitEnable(true);
        rotation.configSetParameter(360, 40, 0, 0, 0);
        rotation.configSetParameter(361, 100, 0, 0, 0);
        rotation.configSetParameter(362, 60, 0, 0, 0);
    }
    public abstract void renderMechanisms();
}
