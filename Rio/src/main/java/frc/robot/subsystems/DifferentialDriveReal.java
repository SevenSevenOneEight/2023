package frc.robot.subsystems;

import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.supers.DifferentialDriveSuper;

public class DifferentialDriveReal extends DifferentialDriveSuper {
    public DifferentialDriveReal() {
        configureMotors();
        setupDrive();
    }

    @Override
    public double getLeftEncoderDistanceMeters() {
        return Constants.EncoderConstants.realPositionToDistance(leftFront.getEncoder().getPosition());
    }

    @Override
    public double getRightEncoderDistanceMeters() {
        return Constants.EncoderConstants.realPositionToDistance(rightFront.getEncoder().getPosition());
    }

    private void setupDrive() {
        poseEstimator = new DifferentialDrivePoseEstimator(
                driveKinematics,
                driveOdometry.getPoseMeters().getRotation(),
                getLeftEncoderDistanceMeters(),
                getRightEncoderDistanceMeters(),
                driveOdometry.getPoseMeters()
        );
    }

    public void renderFieldData() {
        robotWorld.setRobotPose(poseEstimator.getEstimatedPosition());

        SmartDashboard.putData("Robot World", robotWorld);
    }
}
