package frc.robot;

import frc.robot.commands.drive.ArcadeDrive;
import frc.robot.commands.drive.CurvatureDrive;
import frc.robot.simsystems.DifferentialDriveSim;
import frc.robot.subsystems.DifferentialDriveReal;
import frc.robot.supers.DifferentialDriveSuper;
import org.jetbrains.annotations.NotNull;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

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
  private DifferentialDriveSuper DifDrive;

  // Commands
  public ArcadeDrive arcadeDrive;
  public CurvatureDrive curvatureDrive;

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
    // TODO swap to real
    DifDrive = new DifferentialDriveReal();
  }

  /**
   * The function which assigns all simulated subsystems
   */
  private void assignSimSystems() {
    DifDrive = new DifferentialDriveSim();
  }

  private void configureCommands() {
    arcadeDrive = new ArcadeDrive(DifDrive, pilotController);
    curvatureDrive = new CurvatureDrive(DifDrive, pilotController);
  }

  /**
   * A function which assigns auto options to the passed in chooser
   * @param chooser an AdvantageKit Logged Dashboard Command Chooser
   */
  private void configureAutoChooser(@NotNull LoggedDashboardChooser<Command> chooser) {
    chooser.addDefaultOption("Do Nothing", new InstantCommand());
    chooser.addOption("Spin", new InstantCommand());
    chooser.addOption("Drive", new InstantCommand());
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
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.get();
  }
}
