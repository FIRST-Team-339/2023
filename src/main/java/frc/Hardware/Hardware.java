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

import frc.HardwareInterfaces.KilroyEncoder;
import frc.HardwareInterfaces.Transmission.LeftRightTransmission;
import frc.Utils.drive.Drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.simulation.JoystickSim;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

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

            // ============ANALOG INIT============

            // ==============CAN INIT=============
            // Motor Controllers
            leftBottomMotor = new WPI_TalonFX(8);
            leftBottomMotor.setInverted(true);
            leftTopMotor = new WPI_TalonFX(9);
            leftTopMotor.setInverted(true);

            rightBottomMotor = new WPI_TalonFX(16);
            rightBottomMotor.setInverted(true);
            rightTopMotor = new WPI_TalonFX(19);
            rightTopMotor.setInverted(false);

            leftDriveGroup = new MotorControllerGroup(leftBottomMotor, leftTopMotor);
            rightDriveGroup = new MotorControllerGroup(rightBottomMotor, rightTopMotor);

            // Encoders
            leftSideEncoder = new KilroyEncoder((WPI_TalonFX) leftTopMotor);
            rightSideEncoder = new KilroyEncoder((WPI_TalonFX) rightBottomMotor);

            // ==============RIO INIT=============

            // =============OTHER INIT============

            transmission = new LeftRightTransmission(leftDriveGroup, rightDriveGroup);

            drive = new Drive(transmission, leftSideEncoder, rightSideEncoder, null);
            drive.setJoystickDeadband(PREV_DEADBAND);
            transmission.setAllGearPercentages(FIRSTGEAR_PERCENTAGE_PREVYEAR, SECONDGEAR_PERCENTAGE_PREVYEAR,
                    THIRDGEAR_PERCENTAGE_PREVYEAR);

            } // end if
    } // end teleop()

    // **********************************************************
    // CAN DEVICES
    // **********************************************************

    public static MotorController leftBottomMotor = null;
    public static MotorController leftTopMotor = null;
    public static MotorController rightBottomMotor = null;
    public static MotorController rightTopMotor = null;

    public static MotorControllerGroup leftDriveGroup = null;
    public static MotorControllerGroup rightDriveGroup = null;

    public static KilroyEncoder leftSideEncoder = null;
    public static KilroyEncoder rightSideEncoder = null;

    // **********************************************************
    // DIGITAL I/O
    // **********************************************************

    // **********************************************************
    // ANALOG I/O
    // **********************************************************

    // **********************************************************
    // PNEUMATIC DEVICES
    // **********************************************************

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

    public static LeftRightTransmission transmission = null;

    public static Drive drive = null;

    public static final double CURRENT_DEADBAND = 0.2;
    public static final double PREV_DEADBAND = 0.2;

    // Gear Variables

    private static final double FIRSTGEAR_PERCENTAGE_PREVYEAR = 0.3; // FIRSTGEAR_PERCENTAGE_PREVYEAR
    public static final double FIRSTGEAR_PERCENTAGE_CURRENTYEAR = 0.3; // FIRSTGEAR_PERCENTAGE_CURRENTYEAR

    private static final double SECONDGEAR_PERCENTAGE_PREVYEAR = 0.5; // SECONDGEAR_PERCENTAGE_PREVYEAR
    private static final double SECONDGEAR_PERCENTAGE_CURRENTYEAR = 0.5; // SECONDGEAR_PERCENTAGE_CURRENTYEAR

    private static final double THIRDGEAR_PERCENTAGE_PREVYEAR = 0.7; // THIRDGEAR_PERCENTAGE_PREVYEAR
    private static final double THIRDGEAR_PERCENTAGE_CURRENTYEAR = 0.7; // THIRDGEAR_PERCENTAGE_CURRENTYEAR

    // ------------------------------------------
    // Vision stuff
    // ----------------------------

    // -------------------
    // Subassemblies
    // -------------------

    } // end class
