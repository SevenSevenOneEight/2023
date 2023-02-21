// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
// These are out imported libraries (DO NOT CHANGE)
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // private final Timer m_timer = new Timer(); <---- We are currently not using a timer. 
 
//DriveTrain Motor Controllers
//Do Not Remove

//Left Motor Controller Group
MotorController frontLeft = new CANSparkMax(5, MotorType.kBrushless);
MotorController rearLeft = new CANSparkMax(4, MotorType.kBrushless);
MotorControllerGroup leftDrive = new MotorControllerGroup(frontLeft, rearLeft);

//Right Motor Controller Group
MotorController frontRight = new CANSparkMax(8, MotorType.kBrushless);
MotorController rearRight = new CANSparkMax(6, MotorType.kBrushless);
MotorControllerGroup rightDrive = new MotorControllerGroup(frontRight, rearRight);
;

//Drivetrain Configuration 
DifferentialDrive drive = new DifferentialDrive(rightDrive, leftDrive);

//Controller Configuration
XboxController stick = new XboxController(0);



  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {



    rightDrive.setInverted(true);


  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
   
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
    }
  
    //This public void gives us the ability to apply a function to the power over time output of our drivetrain motors. 
    public void exponentialDrive(double left, double right) {
      leftDrive.set(-(0.09*Math.tan(1.4*left)));
      rightDrive.set(-(0.09*Math.tan(1.4*right)));

    }
  
  
    /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    drive.arcadeDrive(-stick.getLeftY(), stick.getRightX());
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
