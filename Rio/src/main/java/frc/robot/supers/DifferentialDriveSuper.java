package frc.robot.supers;

import com.kauailabs.navx.frc.AHRS;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    public DifferentialDriveOdometry driveOdometry = new DifferentialDriveOdometry(new Rotation2d(0), 0, 0, new Pose2d(2,1,new Rotation2d(0)));
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
    public double getLeftMetersPerSecond() {
        return leftFront.getEncoder().getVelocity()/Constants.GearboxConstants.k_driveGearing;
    }
    public double getRightMetersPerSecond() {
        return rightFront.getEncoder().getVelocity()/Constants.GearboxConstants.k_driveGearing;
    }
    public void voltageDrive(double leftVolts, double rightVolts) {
        leftMotors.setVoltage(leftVolts);
        rightMotors.setVoltage(-rightVolts);
        drive.feed();
    }
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(getLeftMetersPerSecond(), getRightMetersPerSecond());
    }
    public Matrix<N3, N1> calculateVisionUncertainty(double poseX, Rotation2d heading, Rotation2d cameraYaw, String cameraName) {
        double maximumUncertainty = 3;
        double minimumUncertainty = 0.02;
        double a = 6;
        double b = -1.3;
        Rotation2d cameraWorldYaw = cameraYaw.rotateBy(heading);
        boolean isCameraFacingFieldSideTags;
        boolean facingRedAlliance;
        double distanceFromTagSide;

        if(-90 < cameraWorldYaw.getDegrees() && cameraWorldYaw.getDegrees() < 90) {
            // camera facing towards red alliance
            facingRedAlliance = true;
            isCameraFacingFieldSideTags = poseX > Constants.FieldConstants.k_fieldLength / 2;
        } else {
            // camera facing towards blue alliance
            facingRedAlliance = false;
            isCameraFacingFieldSideTags = poseX < Constants.FieldConstants.k_fieldLength / 2;
        }

        if(isCameraFacingFieldSideTags) {
            // uncertainty low
            if(facingRedAlliance) {
                distanceFromTagSide = Constants.FieldConstants.k_fieldLength - poseX;
            } else {
                distanceFromTagSide = poseX;
            }
        } else {
            // uncertainty high
            if(!facingRedAlliance) {
                distanceFromTagSide = poseX;
            } else {
                distanceFromTagSide = Constants.FieldConstants.k_fieldLength - poseX;
            }
        }
        double positionUncertainty = ((maximumUncertainty-minimumUncertainty)/(1+Math.pow(Math.E, (a+(b*distanceFromTagSide)))))+minimumUncertainty;

        SmartDashboard.putNumber("Debug/Cameras/"+cameraName+"/Distance From Tag Side", distanceFromTagSide);
        SmartDashboard.putNumber("Debug/Cameras/"+cameraName+"/Vision Uncertainty", positionUncertainty);
        SmartDashboard.putBoolean("Debug/Cameras/"+cameraName+"/Facing Red Alliance", facingRedAlliance);

        return VecBuilder.fill(positionUncertainty,positionUncertainty,positionUncertainty*3);
    }
    public void setPose(Pose2d pose) {
        driveOdometry.resetPosition(getHeading(), 0, 0, pose);
        poseEstimator.resetPosition(getHeading(), 0, 0, pose);
    }
    public abstract double getLeftEncoderDistanceMeters();
    public abstract double getRightEncoderDistanceMeters();
    public abstract void renderFieldData();
}
