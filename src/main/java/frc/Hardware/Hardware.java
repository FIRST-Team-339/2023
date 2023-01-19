// ====================================================================
// FILE NAME: Hardware.java (Team 339 - Kilroy)
//
// CREATED ON: Jan 2, 2011
// CREATED BY: Bob Brown
// MODIFIED ON: June 24, 2019
// MODIFIED BY: Ryan McGee
// ABSTRACT:
// This file contains all of the global definitions for the
// hardware objects in the system
//
// NOTE: Please do not release this code without permission from
// Team 339.
// ====================================================================
package frc.Hardware;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.HardwareInterfaces.Transmission.LeftRightTransmission;
import frc.HardwareInterfaces.Transmission.TransmissionBase;
import frc.HardwareInterfaces.Transmission.TransmissionBase.TransmissionType;
import frc.Utils.drive.Drive;
import frc.HardwareInterfaces.SingleThrowSwitch;
import frc.HardwareInterfaces.SixPositionSwitch;
import frc.HardwareInterfaces.DoubleSolenoid;

/**
 * ------------------------------------------------------- puts all of the
 * hardware declarations into one place. In addition, it makes them available to
 * both autonomous and teleop.
 *
 * @class HardwareDeclarations
 * @author Bob Brown
 *
 * @written Jan 2, 2011 -------------------------------------------------------
 */

public class Hardware
    {

    enum Identifier
        {
        CurrentYear, PrevYear
        };

    public static Identifier robotIdentity = Identifier.PrevYear;

    public static void initialize()
    {
        if (robotIdentity == Identifier.CurrentYear)
            {
            // ==============DIO INIT=============

            // ============ANALOG INIT============

            // ==============CAN INIT=============
            // Motor Controllers

            // Encoders

            // ==============RIO INIT==============

            // =============OTHER INIT============
            }
        else if (robotIdentity == Identifier.PrevYear)
            {
            // ==============DIO INIT=============
            sixPosSwitch = new SixPositionSwitch(13, 14, 15, 16, 17, 18);
            disableAutoSwitch = new SingleThrowSwitch(9);

            // ============ANALOG INIT============

            // ==============CAN INIT=============
            leftBottomMotor = new WPI_TalonFX(8);
            leftTopMotor = new WPI_TalonFX(9);

            leftBottomMotor.setInverted(true);
            leftTopMotor.setInverted(true);

            rightBottomMotor = new WPI_TalonFX(16);
            rightTopMotor = new WPI_TalonFX(19);

            rightBottomMotor.setInverted(false);
            rightTopMotor.setInverted(false);

            leftSideMotors = new MotorControllerGroup(leftBottomMotor, leftTopMotor);
            rightSideMotors = new MotorControllerGroup(rightBottomMotor, rightTopMotor);
            // ==============RIO INIT=============

            // =============OTHER INIT============
            transmission = new LeftRightTransmission(leftSideMotors, rightSideMotors);
            drive = new Drive(transmission, null, null, null);
            transmission.setJoystickDeadband(PREV_DEADBAND);
            transmission.setAllGearPercentages(PREV_GEAR1_MAX_SPEED, PREV_GEAR2_MAX_SPEED, PREV_GEAR3_MAX_SPEED);

            drive = new Drive(transmission, null, null, null);

            breakTestPistion = new DoubleSolenoid(4, 5);

            }
    }

    // **********************************************************
    // CAN DEVICES
    // **********************************************************
    public static MotorController leftBottomMotor = null;
    public static MotorController leftTopMotor = null;
    public static MotorController rightBottomMotor = null;
    public static MotorController rightTopMotor = null;
    // **********************************************************
    // DIGITAL I/O
    // **********************************************************
    public static SixPositionSwitch sixPosSwitch = null;
    public static SingleThrowSwitch disableAutoSwitch = null;
    // **********************************************************
    // ANALOG I/O
    // **********************************************************

    // **********************************************************
    // PNEUMATIC DEVICES
    // **********************************************************
    public static Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);
    public static DoubleSolenoid breakTestPistion = null;

    // **********************************************************
    // roboRIO CONNECTIONS CLASSES
    // **********************************************************

    public static PowerDistribution pdp = new PowerDistribution();

    // **********************************************************
    // DRIVER STATION CLASSES
    // **********************************************************

    public static DriverStation driverStation = DriverStation.getInstance();

    public static Joystick leftDriver = new Joystick(0);
    public static Joystick rightDriver = new Joystick(1);
    public static Joystick leftOperator = new Joystick(2);
    public static Joystick rightOperator = new Joystick(3);

    // **********************************************************
    // Kilroy's Ancillary classes
    // **********************************************************

    // ------------------------------------
    // Utility classes
    // ------------------------------------

    // ------------------------------------
    // Drive system
    // ------------------------------------
    public static MotorControllerGroup leftSideMotors = null;
    public static MotorControllerGroup rightSideMotors = null;

    public static LeftRightTransmission transmission = null;
    public static Drive drive = null;

    // ------------------------------------------
    // Vision stuff
    // ----------------------------

    // -------------------
    // Subassemblies
    // -------------------

    public final static double PREV_DEADBAND = 0.2;
    private final static double PREV_GEAR1_MAX_SPEED = 0.3;
    private final static double PREV_GEAR2_MAX_SPEED = 0.5;
    private final static double PREV_GEAR3_MAX_SPEED = 0.7;
    } // end class
