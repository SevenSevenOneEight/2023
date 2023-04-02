package frc.robot.supers;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public abstract class DifferentialDriveSuper extends SubsystemBase {
    // Drive Motors & Groups
    public final CANSparkMax leftFront = new CANSparkMax(Constants.DriveConstants.k_leftFrontID, CANSparkMaxLowLevel.MotorType.kBrushless);
    public final CANSparkMax leftBack = new CANSparkMax(Constants.DriveConstants.k_leftBackID, CANSparkMaxLowLevel.MotorType.kBrushless);
    public final MotorControllerGroup leftMotors = new MotorControllerGroup(leftFront, leftBack);
    public final CANSparkMax rightFront = new CANSparkMax(Constants.DriveConstants.k_rightFrontID, CANSparkMaxLowLevel.MotorType.kBrushless);
    public final CANSparkMax rightBack = new CANSparkMax(Constants.DriveConstants.k_rightBackID, CANSparkMaxLowLevel.MotorType.kBrushless);
    public final MotorControllerGroup rightMotors = new MotorControllerGroup(rightFront, rightBack);

    // Sensors
    public AHRS NavXMicro = new AHRS();

    // Pose Estimation, Kinematics, & Odometry
    public final DifferentialDriveKinematics driveKinematics = new DifferentialDriveKinematics(Constants.DriveConstants.k_trackWidth);
    public final DifferentialDrive drive = new DifferentialDrive(leftMotors, rightMotors);
    public DifferentialDriveOdometry driveOdometry = new DifferentialDriveOdometry(new Rotation2d(1), 0, 0, new Pose2d(1,1,new Rotation2d(1)));
    public DifferentialDrivePoseEstimator poseEstimator;

    // field
    public Field2d robotWorld = new Field2d();

    // functions
    public void configureMotors() {
        rightMotors.setInverted(true);
        leftMotors.setInverted(false);

        leftFront.setSmartCurrentLimit(Constants.DriveConstants.k_currentLimit);
        leftBack.setSmartCurrentLimit(Constants.DriveConstants.k_currentLimit);
        rightFront.setSmartCurrentLimit(Constants.DriveConstants.k_currentLimit);
        rightBack.setSmartCurrentLimit(Constants.DriveConstants.k_currentLimit);

        leftFront.setIdleMode(Constants.DriveConstants.k_idleMode);
        leftBack.setIdleMode(Constants.DriveConstants.k_idleMode);
        rightFront.setIdleMode(Constants.DriveConstants.k_idleMode);
        rightBack.setIdleMode(Constants.DriveConstants.k_idleMode);
    }
    public Rotation2d getHeading() {
        return Rotation2d.fromDegrees(-NavXMicro.getYaw());
    }
    public abstract double getLeftEncoderDistanceMeters();
    public abstract double getRightEncoderDistanceMeters();
    public abstract void renderFieldData();
}