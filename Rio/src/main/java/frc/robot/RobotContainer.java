package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.commands.ArmCommand;
import frc.robot.commands.drive.ArcadeDrive;
import frc.robot.commands.drive.CurvatureDrive;
import frc.robot.commands.drive.TankDrive;
import frc.robot.simsystems.ArmSim;
import frc.robot.simsystems.DifferentialDriveSim;
import frc.robot.subsystems.ArmReal;
import frc.robot.subsystems.DifferentialDriveReal;
import frc.robot.supers.ArmSuper;
import frc.robot.supers.DifferentialDriveSuper;
import org.jetbrains.annotations.NotNull;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import java.util.List;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Non-Static Access
  public static RobotContainer singleton;

  // Subsystem Supers
  private DifferentialDriveSuper differentialDrive;
  private ArmSuper arm;

  // Commands
  public ArcadeDrive arcadeDrive;
  public CurvatureDrive curvatureDrive;
  public TankDrive tankDrive;
  public ArmCommand armCommand;
  public Command autoCommand;

  // Controllers
  private final CommandXboxController pilotController
          = new CommandXboxController(Constants.OIConstants.k_pilotControllerPort);
  private final CommandXboxController operatorController
          = new CommandXboxController(Constants.OIConstants.k_operatorControllerPort);

  // Dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser = new LoggedDashboardChooser<>("Auto Choices");

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    if(Robot.isReal()) {
      assignSubsystems();
    } else  {
      assignSimSystems();
    }
    configureCommands();
    configureAutoChooser(autoChooser);
    configureButtonBindings();

    singleton = this;
  }

  /**
   * The function which assigns all real subsystems
   */
  private void assignSubsystems() {
    differentialDrive = new DifferentialDriveReal();
    arm = new ArmReal();
  }

  /**
   * The function which assigns all simulated subsystems
   */
  private void assignSimSystems() {
    differentialDrive = new DifferentialDriveSim();
    arm = new ArmSim();
  }

  /**
   * The function which assigns all commands
   */
  private void configureCommands() {
    arcadeDrive = new ArcadeDrive(differentialDrive, pilotController);
    curvatureDrive = new CurvatureDrive(differentialDrive, pilotController);
    tankDrive = new TankDrive(differentialDrive, pilotController);
    armCommand = new ArmCommand(arm, operatorController);
  }

  /**
   * A function which assigns auto options to the passed in chooser
   * @param chooser an AdvantageKit Logged Dashboard Command Chooser
   */
  private void configureAutoChooser(@NotNull LoggedDashboardChooser<Command> chooser) {
    chooser.addDefaultOption("Do Nothing", new InstantCommand());
    var autoVoltageConstraint =
            new DifferentialDriveVoltageConstraint(
                    new SimpleMotorFeedforward(
                            Constants.DriveConstants.ksVolts,
                            Constants.DriveConstants.kvVoltSecondsPerMeter,
                            Constants.DriveConstants.kaVoltSecondsSquaredPerMeter),
                    differentialDrive.driveKinematics,
                    10);
    TrajectoryConfig config =
            new TrajectoryConfig(
                    4,
                    2)
                    // Add kinematics to ensure max speed is actually obeyed
                    .setKinematics(differentialDrive.driveKinematics)
                    // Apply the voltage constraint
                    .addConstraint(autoVoltageConstraint);
    Trajectory kTrajectory =
            TrajectoryGenerator.generateTrajectory(
                    // Start at the origin facing the +X direction
                    new Pose2d(2, 1, new Rotation2d(0)),
                    // Pass through these two interior waypoints, making an 's' curve path
                    List.of(new Translation2d(6, 1), new Translation2d(10, 6)),
                    // End 3 meters straight ahead of where we started, facing forward
                    new Pose2d(14, 7, new Rotation2d(0)),
                    // Pass config
                    config);
    RamseteCommand ramseteCommand =
            new RamseteCommand(
                    kTrajectory,
                    differentialDrive.poseEstimator::getEstimatedPosition,
                    new RamseteController(2, 0.7),
                    new SimpleMotorFeedforward(
                            Constants.DriveConstants.ksVolts,
                            Constants.DriveConstants.kvVoltSecondsPerMeter,
                            Constants.DriveConstants.kaVoltSecondsSquaredPerMeter),
                    differentialDrive.driveKinematics,
                    differentialDrive::getWheelSpeeds,
                    new PIDController(2, 1, 0),
                    new PIDController(2, 1, 0),
                    // RamseteCommand passes volts to the callback
                    differentialDrive::voltageDrive,
                    differentialDrive);
    differentialDrive.robotWorld.getObject("Trajectory").setTrajectory(kTrajectory);
    chooser.addOption("Ramsete", ramseteCommand);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
  }

  /**
   * Use this to set the autonomous command
   */
  public void setAutonomousCommand() {
    autoCommand = autoChooser.get();
  }
}
