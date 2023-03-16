/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/
// ====================================================================
// FILE NAME: Teleop.java (Team 339 - Kilroy)
//
// CREATED ON: Jan 13, 2015
// CREATED BY: Nathanial Lydick
// MODIFIED ON: June 20, 2019
// MODIFIED BY: Ryan McGee
// ABSTRACT:
// This file is where almost all code for Kilroy will be
// written. All of these functions are functions that should
// override methods in the base class (IterativeRobot). The
// functions are as follows:
// -----------------------------------------------------
// Init() - Initialization code for teleop mode
// should go here. Will be called each time the robot enters
// teleop mode.
// -----------------------------------------------------
// Periodic() - Periodic code for teleop mode should
// go here. Will be called periodically at a regular rate while
// the robot is in teleop mode.
// -----------------------------------------------------
//
// ====================================================================
package frc.robot;

import java.io.ObjectInputStream.GetField;

import org.opencv.features2d.FlannBasedMatcher;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.HIDType;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.Hardware.Hardware;
import frc.HardwareInterfaces.KilroyUSBCamera;
import frc.HardwareInterfaces.Potentiometer;
import frc.HardwareInterfaces.Transmission.LeftRightTransmission;
import frc.HardwareInterfaces.Transmission.TransmissionBase;
import frc.Utils.Dashboard;
import frc.Utils.Dashboard.AutoModeDash;
import frc.Utils.Dashboard.DriveGear;

/**
 * This class contains all of the user code for the Autonomous part of the
 * match, namely, the Init and Periodic code
 *
 * @author Nathanial Lydick
 * @written Jan 13, 2015
 */
public class Teleop
    {

    /**
     * User Initialization code for teleop mode should go here. Will be called
     * once when the robot enters teleop mode.
     *
     * @author Nathanial Lydick
     * @written Jan 13, 2015
     */
    public static void init()
    {
        Hardware.drive.setGear(0);
        Hardware.drive.setGearPercentage(0, Hardware.CURRENT_GEAR1_MAX_SPEED);
        Hardware.rightBottomEncoder.reset();
        Hardware.leftBottomEncoder.reset();
        Hardware.armRaiseButton.setValue(true);
        // Piston position setting
        Hardware.armRaisePiston.setForward(true);
        Hardware.clawPiston.setForward(true);

        if (Hardware.eBrakePiston.getForward() == true)
            {
            Hardware.eBrakePiston.setForward(false);
            }

        // Camera Settings
        Hardware.cameras.setCamera(0);

    } // end init()

    /**
     * manage ebrake section used to seperate ebrake from rest of code so we
     * dont get confused or lost in the code
     * 
     * and edited code to make the ebrake stay down until specified fixed
     * continual fire
     *
     * 
     * @author Michael Lynch
     * @written febuary 9 2023
     * 
     *          (in 2023 code)
     */

    // =============== manage ebrake ===============
    private static void manageEBrake()
    {

        if (Hardware.eBrakeTimer.get() <= 0.001)
            {
            Hardware.eBrakeTimerIsStopped = true;
            } // if
        else
            {
            Hardware.eBrakeTimerIsStopped = false;
            } // else

        if (Hardware.eBrakeJoystickTimer.get() <= 0.001)
            {
            Hardware.eBrakeJoystickTimerIsStopped = true;
            } // if
        else
            {
            Hardware.eBrakeJoystickTimerIsStopped = false;
            } // else

        // =========================
        // when button 5 right driver is pushed
        // Extends the eBrake piston out
        // =========================
        if (Hardware.rightDriver.getRawButtonPressed(5) == true)
            {
            Hardware.eBrakeMomentarySwitch2.setValue(false);
            Hardware.eBrakePiston.setForward(true);
            // Hardware.transmission.drive(0, 0);
            Hardware.eBrakeJoystickTimer.reset();
            Hardware.eBrakeJoystickTimer.start();
            } // if

        // =========================
        // when button 6 left driver is pushed
        // Retracts the eBrake piston and affects drive
        // =========================
        if (Hardware.rightDriver.getRawButtonPressed(6) == true)
            {
            // =========================
            // when the retract button (left driver button 6) is pressed, eBrake
            // is not retracted and the joystick is moved Resets the eBrake
            // timer retracts the eBrake piston, stops all
            // drive motors, and starts the eBrake timer
            // =========================
            Hardware.eBrakeJoystickTimer.stop();
            Hardware.eBrakeMomentarySwitch1.setValue(false);
            if ((Hardware.eBrakePiston.getForward() == true) || (((Math
                    .abs(Hardware.leftDriver.getY()) >= Hardware.eBrakeDeadband)
                    || (Math.abs(Hardware.rightDriver
                            .getY()) >= Hardware.eBrakeDeadband))
                    && ((Hardware.eBrakeJoystickTimer.hasElapsed(2) == true)
                            || (Hardware.eBrakeJoystickTimerIsStopped == true))))
                {
                // Hardware.eBrakeTimer.reset();
                Hardware.transmission.drive(0, 0);
                // Hardware.eBrakeTimer.start();
                Hardware.eBrakePiston.setForward(false);
                Hardware.eBrakeMomentarySwitch1.setValue(false);
                Hardware.eBrakeMomentarySwitch2.setValue(true);
                }
            // =========================
            // when the eBrake is retracted and the eBrake timer has passed a
            // certain duration, reactivates the drive motors and stops the
            // eBrake timer
            // =========================
            if ((Hardware.eBrakePiston.getForward() == false)
                    && (((Hardware.eBrakeTimer
                            .hasElapsed(Hardware.eBrakeDelayTime) == true)
                            || (Hardware.eBrakeTimerIsStopped == true))
                            && ((Hardware.eBrakeJoystickTimer
                                    .hasElapsed(2) == true)
                                    || (Hardware.eBrakeJoystickTimerIsStopped == true))))
                {
                Hardware.transmission.drive(Hardware.leftDriver.getY(),
                        Hardware.rightDriver.getY());
                Hardware.eBrakeTimer.stop();
                } // if
            } // if
        // =========================
        // when the eBrake is not retracted and the joystick is moved
        // Resets the eBrake timer, retracts the eBrake piston, stops all
        // drive motors,
        // and starts the eBrake timer
        // =========================
        if ((Hardware.eBrakePiston.getForward() == true) && (((Math
                .abs(Hardware.leftDriver.getY()) >= Hardware.eBrakeDeadband)
                || (Math.abs(Hardware.rightDriver
                        .getY()) >= Hardware.eBrakeDeadband))
                && ((Hardware.eBrakeJoystickTimer.hasElapsed(2) == true)
                        || (Hardware.eBrakeJoystickTimerIsStopped == true))))
            {
            // Hardware.eBrakeTimer.reset();
            Hardware.transmission.drive(0, 0);
            // Hardware.eBrakeTimer.start();
            Hardware.eBrakePiston.setForward(false);
            Hardware.eBrakeMomentarySwitch1.setValue(false);
            Hardware.eBrakeMomentarySwitch2.setValue(true);
            } // if
        // =========================
        // when the eBrake is retracted and the eBrake timer has passed a
        // certain
        // duration
        // Reactivates the drive motors and stops the eBrake timer
        // =========================
        if ((Hardware.eBrakePiston.getForward() == false)
                && (((Hardware.eBrakeTimer
                        .hasElapsed(Hardware.eBrakeDelayTime) == true)
                        || (Hardware.eBrakeTimerIsStopped == true))
                        && ((Hardware.eBrakeJoystickTimer.hasElapsed(2) == true)
                                || (Hardware.eBrakeJoystickTimerIsStopped == true))))
            {
            Hardware.transmission.drive(Hardware.leftDriver.getY(),
                    Hardware.rightDriver.getY());
            Hardware.eBrakeTimer.stop();
            } // if
    } // end of manage ebrake()

    /**
     * Arm control and claw control code goes here.
     *
     * @author Kaelyn Atkins
     * @written February 9, 2023
     */
    private static void armControl()
    {
        // Checks if claw trigger button has been pressed and sets the claw
        // piston to
        // the opposite direction each time it is pressed

        if (Hardware.clawTriggerButton.isOnCheckNow() == true)

            {
            Hardware.clawPiston.setForward(false);
            }
        else
            {
            Hardware.clawPiston.setForward(true);
            }

        // Checks if arm raise button has been pressed and sets the arm raise
        // piston to
        // the opposite direction each time it is pressed
        if (Hardware.armRaiseButton.isOnCheckNow() == true)
            {
            Hardware.armRaisePiston.setForward(true);
            }
        else
            {
            Hardware.armRaisePiston.setForward(false);
            }

        // -----------------
        // Arm motor controls
        // ------------------

        // If right operator Y value is between -0.2 and +0.2 then the
        // armRaiseMotor will equal the armControlHoldSpeed
        if (Hardware.rightOperator.getY() >= -Hardware.armControlDeadband
                && Hardware.rightOperator.getY() <= Hardware.armControlDeadband)
            {
            Hardware.armRaiseMotor.set(Hardware.armControlHoldSpeed);
            } // end if
        else
            {
            // If right operator Y value is less than the armControlDeadband
            // then the ArmRaiseMotor will equal the equation below
            if (Hardware.rightOperator.getY() < -Hardware.armControlDeadband)
                {
                Hardware.armRaiseMotor.set(((-Hardware.armRaiseMaxSpeedDown
                        + Hardware.armRaiseMinSpeedNegative)
                        / (-Hardware.maxJoystickOperatorValue
                                + Hardware.minJoystickOperatorValue))
                        * (Hardware.rightOperator.getY()
                                + Hardware.minJoystickOperatorValue)
                        - Hardware.armRaiseMinSpeedNegative);

                } // end if
            // If right operator Y value is greater than the armControlDeadband
            // then the ArmRaiseMotor will equal the equation below
            if (Hardware.rightOperator.getY() > Hardware.armControlDeadband)
                {
                Hardware.armRaiseMotor.set(((Hardware.armRaiseMaxSpeedUp
                        - Hardware.armRaiseMinSpeedPositive)
                        / (Hardware.maxJoystickOperatorValue
                                - Hardware.minJoystickOperatorValue))
                        * (Hardware.rightOperator.getY()
                                - Hardware.minJoystickOperatorValue)
                        + Hardware.armRaiseMinSpeedPositive);
                } // end if
            } // end else
        // If left operator Y value is between -0.2 and +0.2 then the
        // armLengthMotor will equal the armLengthHoldSpeed
        if (Hardware.leftOperator.getY() >= -Hardware.armLengthDeadband
                && Hardware.leftOperator.getY() <= Hardware.armLengthDeadband)
            {
            Hardware.armLengthMotor.set(Hardware.armLengthHoldSpeed);
            } // end if
        else
            {
            // If left operator Y value is less than the armLengthDeadband then
            // the ArmLengthMotor will equal the equation below
            if (Hardware.leftOperator.getY() < -Hardware.armLengthDeadband)
                {
                Hardware.armLengthMotor.set(((-Hardware.armLengthMaxSpeed
                        + Hardware.armLengthMinSpeed)
                        / (-Hardware.maxJoystickOperatorValue
                                + Hardware.minJoystickOperatorValue))
                        * (Hardware.leftOperator.getY()
                                + Hardware.minJoystickOperatorValue)
                        - Hardware.armLengthMinSpeed);
                } // end if
            // If left operator Y value is greater than the armLengthDeadband
            // then the ArmLengthMotor will equal the equation below
            if (Hardware.leftOperator.getY() > Hardware.armLengthDeadband)
                {
                Hardware.armLengthMotor.set(((Hardware.armLengthMaxSpeed
                        - Hardware.armLengthMinSpeed)
                        / (Hardware.maxJoystickOperatorValue
                                - Hardware.minJoystickOperatorValue))
                        * (Hardware.leftOperator.getY()
                                - Hardware.minJoystickOperatorValue)
                        + Hardware.armLengthMinSpeed);
                } // end if

            } // end else

    } // end of armControl()

    public static void updateDashboard()
    {
        // GYRO
        Dashboard.updateGyroInd();

        // GEARS
        int currentGear = Hardware.drive.getCurrentGear();
        if (Hardware.leftDriver.getY() > 0.2
                && Hardware.rightDriver.getY() > 0.2)
            {
            Dashboard.updateDriveGearInd(DriveGear.Reverse);
            }
        else
            {
            if (currentGear == 0)
                {
                Dashboard.updateDriveGearInd(DriveGear.Drive1);
                }
            else
                if (currentGear == 1)
                    {
                    Dashboard.updateDriveGearInd(DriveGear.Drive2);
                    }
                else
                    if (currentGear == 2)
                        {
                        Dashboard.updateDriveGearInd(DriveGear.Drive3);
                        }
            }

        // AUTO
        Dashboard.updateAutoModeInd(AutoModeDash.Teleop);

        // UTILS
        Dashboard.updateEBrakeEngagedInd(Hardware.eBrakePiston.getForward());
        Dashboard.updateReplaceBatteryInd();
        Dashboard.updateClawClosedInd(Hardware.clawPiston.getForward());
    }

    /**
     * User Periodic code for teleop mode should go here. Will be called
     * periodically at a regular rate while the robot is in teleop mode.
     *
     * @author Nathanial Lydick
     * @written Jan 13, 2015
     */

    public static void periodic()
    {
        // =============== AUTOMATED SUBSYSTEMS ===============

        // ================= OPERATOR CONTROLS ================

        // ---------------------------
        // manage the camera view
        // ---------------------------

        // Hardware.cameras.switchCameras(Hardware.switchCameraViewButton10);
        // Hardware.cameras.setCamera(0);
        // if (Hardware.armRaiseEncoder
        // .getRaw() <= (cameraSwitchPoint - cameraDeadBand))
        // {
        // Hardware.cameras.setCamera(1);
        // }
        // else
        // if (Hardware.armRaiseEncoder
        // .getRaw() >= (cameraSwitchPoint + cameraDeadBand))
        // {
        // Hardware.cameras.setCamera(0);
        // }

        // -------------------------
        // If eBrake has not overridden our ability to
        // drive, use the drivers joysticks to drive.
        // ----------------------------
        if (((Hardware.eBrakeTimerIsStopped == true) || (Hardware.eBrakeTimer
                .hasElapsed(Hardware.eBrakeDelayTime) == true))
                && ((Hardware.eBrakeJoystickTimer.hasElapsed(2) == true)
                        || (Hardware.eBrakeJoystickTimerIsStopped == true)))
            {
            Hardware.transmission.shiftGears(Hardware.rightDriver.getTrigger(),
                    Hardware.leftDriver.getTrigger());
            Hardware.transmission.drive(Hardware.leftDriver.getY(),
                    Hardware.rightDriver.getY());
            } // if
        else
            {
            Hardware.transmission.drive(0, 0);
            }

        // --------------------------
        // control the eBrake and
        // the arm by the operator
        // ----------------------------
        armControl();

        manageEBrake();

        // --------------------------
        // update dashboard values
        // --------------------------
        updateDashboard();

        // --------------------------
        // all print statement and
        // individual testing function
        // --------------------------
        printStatements();

        individualTest();

    } // end periodic()

    public static void individualTest()
    {
        // people test functions
    } // end individualTest()

    public static void printStatements()
    {
        // ========== INPUTS ==========
        // System.out.println("eBrakeTimer: " + Hardware.eBrakeTimer.get());
        // System.out.println("eBrakeJoyStickTimer has passed " + 2 + " seconds:
        // "
        // + Hardware.eBrakeJoystickTimer.hasElapsed(2));
        // System.out.println(
        // "eBrakeJoyStickTimer: " + Hardware.eBrakeJoystickTimer.get());
        // System.out.println("clawPiston = " + Hardware.clawPiston.get());
        // System.out.println("armPiston = " + Hardware.armRaisePiston.get());
        // ---------- DIGITAL ----------

        // Encoder Distances
        // System.out.println("LB encoder DIST = "
        // + Hardware.leftBottomEncoder.getDistance());
        // System.out.println("RB encoder DIST = "
        // + Hardware.rightBottomEncoder.getDistance());

        // Encoder Raw Values
        // System.out.println(
        // "LB encoder RAW = " + Hardware.leftBottomEncoder.getRaw());
        // System.out.println(
        // "RB encoder RAW = " + Hardware.rightBottomEncoder.getRaw());
        // Switch Values

        /////////// SIX POSITION SWITCH ///////////
        // System.out.println("Six Position Switch value: "
        // + Hardware.sixPosSwitch.getPosition());

        /////////// DISABLE AUTO SWITCH ///////////
        // System.out.println("Disable Auto Switch value: "
        // + Hardware.disableAutoSwitch.isOn());

        // ----------------LeftRightNone Switch -----------
        // System.out.println(
        // "LRNone SW = " + Hardware.leftRightNoneSwitch.getPosition());

        // ---------- ANALOG -----------
        // System.out.println("delayPot = " + Hardware.delayPot.get(0.0, 5.0));

        // -------- SUBSYSTEMS ---------

        // ---------- OTHER ------------
        // System.out.println("ARE " + Hardware.armRaiseEncoder.getRaw());
        /////////// JOYSTICK VALUES ///////////
        // System.out.println("L Joystick: " + Hardware.leftDriver.getY());
        // System.out.println("R Joystick: " + Hardware.rightDriver.getY());
        // ========== OUTPUTS ==========

        // ---------- DIGITAL ----------
        // System.out.println("disableAutoSwitch = " +
        // Hardware.disableAutoSwitch.isOn());
        // System.out.println("RedLight = " + Hardware.redLightSensor.isOn());
        // ---------- ANALOG -----------

        // ----------- CAN -------------

        /////////// MOTOR VALUES ///////////
        // System.out.println("LBottomMotor = " +
        /////////// Hardware.leftBottomMotor.get());
        // System.out.println("LTopMotor = " + Hardware.leftTopMotor.get());
        // System.out.println("RBottomMotor = " +
        /////////// Hardware.rightBottomMotor.get());
        // System.out.println("RTopMotor = " + Hardware.rightTopMotor.get());
        // System.out.println("LeMotor = " + Hardware.armLengthMotor.get()
        // + " Y = " + Hardware.leftOperator.getY());
        // System.out.println("RaMotor = " + Hardware.armRaiseMotor.get() + " Y
        /////////// = "
        // + Hardware.rightOperator.getY());

        // -------- SUBSYSTEMS ---------

        // ---------- OTHER ------------

    } // end printStatements()

    // =========================================
    // class private data goes here
    // =========================================
    private static double cameraDeadBand = 4.00;
    private static double cameraSwitchPoint = -70.00;
    private static double eBrakeHoldSpeed = 0.1;
    } // end class
