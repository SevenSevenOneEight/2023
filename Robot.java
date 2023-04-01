// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
//import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.cscore.UsbCamera;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

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
//XboxController arm = new XboxController(1);

//Subsystem Configuration
WPI_TalonFX extendArm = new WPI_TalonFX(2);
WPI_TalonFX swingArm = new WPI_TalonFX(3);
MotorController vacuum = new CANSparkMax(1, MotorType.kBrushless);

//Variable for Autonomous
double autoStart = 0;
boolean goForAuto = false;

//Fixed position variables
//String position = "Neutral";

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    rightDrive.setInverted(true);

    UsbCamera camera = CameraServer.startAutomaticCapture();
    UsbCamera camera1 = CameraServer.startAutomaticCapture(1);


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
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

     autoStart = Timer.getFPGATimestamp();
     goForAuto = SmartDashboard.getBoolean("Go For Auto", true);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
      vacuum.set(1);
     
  
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
double autoTimeElapsed = Timer.getFPGATimestamp() - autoStart;
    if (goForAuto){
      if (autoTimeElapsed < 1.5){
        frontLeft.set(.65);
        rearLeft.set(.65);
        frontRight.set(-.65);
        rearRight.set(-.65);
      }
      else {
        frontLeft.set(0);
        rearLeft.set(0);
        frontRight.set(0);
        rearRight.set(0);
      }
    }
  
  }

  /** This function is called once when teleop is enabled. */

  public void exponentialDrive(double left, double right) {
    leftDrive.set(-(0.02*Math.tan(1.4*left)));
    rightDrive.set(-(0.02*Math.tan(1.4*right)));
    swingArm.set(-(0.05*Math.tan(1.4*right)));
    extendArm.set(-(0.05*Math.tan(1.4*right)));
  }

  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    drive.arcadeDrive(-stick.getLeftY(), stick.getRightX());

    if (stick.getLeftBumper()){
      extendArm.set(.25);
    }

      else if (stick.getRightBumper()){
        extendArm.set(-.25);
      }

        else {
          extendArm.set(0);
        }

    if (stick.getAButton()){
      swingArm.set(0.75);
    }
    
      else if (stick.getBButton()){
        swingArm.set(-0.75);
      }
    
        else {
          swingArm.set(0);
        }

    if (stick.getXButtonPressed()){
      vacuum.set(1);
    }
      else if (stick.getYButtonPressed()){
        vacuum.set(0);
      }
             
                   
    // if (stick.getYButton()){
    //   swingArm.set(-1);

          // try {
          //   Thread.sleep(1000);  
          // } catch (InterruptedException e){
          //   e.printStackTrace();
          // }

    //       swingArm.set(0);
    // }
    // if (stick.getYButtonPressed()){
    //   if (position == "Neutral"){
    //     swingArm.set(1.0);

    //     try {
    //       Thread.sleep(1000);  
    //     } catch (InterruptedException e){
    //       e.printStackTrace();
    //     }

    //     position = "high";
    //   } else if (position == "Medium"){
    //     swingArm.set(1.0);
        
    //     try {
    //       Thread.sleep(1000);  
    //     } catch (InterruptedException e){
    //         e.printStackTrace();
    //     }

    //     position = "high";
    //   } else if (position == "Low"){
    //     swingArm.set(1.0);

    //     try {
    //       Thread.sleep(1000);  
    //     } catch (InterruptedException e){
    //       e.printStackTrace();
    //     }

    //     position = "high";
    //   } else{
    //     swingArm.set(0);
    //   }
    // }else{
    //   swingArm.set(0);
  
    // }
  
                  

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
