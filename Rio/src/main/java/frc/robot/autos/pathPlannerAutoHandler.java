package frc.robot.autos;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.RamseteAutoBuilder;
import com.pathplanner.lib.commands.FollowPathWithEvents;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.Constants;
import frc.robot.supers.DifferentialDriveSuper;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import java.util.ArrayList;
import java.util.HashMap;

public class pathPlannerAutoHandler {
    LoggedDashboardChooser<Command> autoChooser;
    private final String autoName;
    private final DifferentialDriveSuper differentialDrive;
    RamseteAutoBuilder autoBuilder;
    HashMap<String, Command> eventMap = new HashMap<>();

    public pathPlannerAutoHandler(LoggedDashboardChooser<Command> chooser, String autoName, DifferentialDriveSuper differentialDrive) {
        this.differentialDrive = differentialDrive;
        this.autoChooser = chooser;
        this.autoName = autoName;
        System.out.println("WARNING: Path Planner Auto Handler is experimental and may not work as expected");
        System.out.println("WARNING: Path Planner Auto Handler currently uses a different coordinate frame than the rest of the robot code.");
        configureAuto();
    }

    private void configureAuto() {
        configureEventMap(eventMap);
        configureRamseteAutoBuilder(eventMap);
        PathPlannerTrajectory autoPath = PathPlanner.loadPath(autoName, new PathConstraints(5, 3.5));
        FollowPathWithEvents autoWithEvents = new FollowPathWithEvents(
                autoBuilder.followPath(autoPath),
                autoPath.getMarkers(),
                eventMap
        );
        autoChooser.addOption(autoName, autoWithEvents);
    }

    private void configureEventMap(HashMap<String, Command> eventMap) {
        eventMap.put("Waypoint 1", new PrintCommand("Passed marker 1"));
        eventMap.put("Waypoint 3", new PrintCommand("Passed marker 3"));
    }
    private void configureRamseteAutoBuilder(HashMap<String, Command> eventMap) {
        autoBuilder = new RamseteAutoBuilder(
                differentialDrive.poseEstimator::getEstimatedPosition,
                differentialDrive::setPose,
                new RamseteController(1.5, 5),
                differentialDrive.driveKinematics,
                new SimpleMotorFeedforward(
                        Constants.DriveConstants.ksVolts,
                        Constants.DriveConstants.kvVoltSecondsPerMeter,
                        Constants.DriveConstants.kaVoltSecondsSquaredPerMeter
                ),
                differentialDrive::getWheelSpeeds,
                new PIDConstants(1, 1, 0.5),
                differentialDrive::voltageDrive,
                eventMap,
                true,
                differentialDrive
        );
    }
}
