// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // Declare TalonFX motor controllers
  private final TalonFX frontLeft = new TalonFX(2);
  private final TalonFX frontRight = new TalonFX(3);
  private final TalonFX backLeft = new TalonFX(4);
  private final TalonFX backRight = new TalonFX(1);

  // Declare Joystick
  private Joystick driveJoystick;

  // Declare customMechDrive instance
  private customMechDrive driveTrain;

  @Override
  public void robotInit() {
    // Initialize Joystick
    driveJoystick = new Joystick(0);

    // Initialize custom drive train with TalonFX motor controllers
    driveTrain = new customMechDrive(frontLeft, frontRight, backLeft, backRight);

    // Configure TalonFX motor controllers
    frontLeft.setInverted(true);
    frontRight.setInverted(false);
    backLeft.setInverted(false);
    backRight.setInverted(false);

    // Optionally set deadbands for customJoystick if used
    // joystick = new customJoystick(0);
    // joystick.setDeadbandCartesian(0.2); // Example deadband for Cartesian movement
    // joystick.setDeadbandRotation(0.2); // Example deadband for rotation
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    // Pass joystick inputs to the drive train  
    driveTrain.drive(driveJoystick.getX(), driveJoystick.getY(), driveJoystick.getZ());
    // Uncomment and use if customJoystick is added
    // driveTrain.drive(joystick.getXNormalized(), joystick.getYNormalized(), joystick.getRotNormalized());
    //JOYSTICK BUILT IN ALL GOOD 
    // System.out.println("X: " + -driveJoystick.getX());
    // System.out.println("Y: " + -driveJoystick.getY());
    // System.out.println("Rot: " + -driveJoystick.getZ());
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
    driveTrain.stopMotors();
  }

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
