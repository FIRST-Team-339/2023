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
        Hardware.rightBottomEncoder.reset();

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
            }
        else
            {
            Hardware.eBrakeTimerIsStopped = false;
            }

        // =========================
        // when button 5 right driver is pushed
        // Extends the eBrake piston out
        // =========================
        if (Hardware.eBrakeMomentarySwitch1.isOnCheckNow() == true)
            {
            Hardware.eBrakeMomentarySwitch2.setValue(false);
            Hardware.eBrake.setForward(true);
            }
        // =========================
        // when button 6 left driver is pushed
        // Retracts the eBrake piston and affects drive
        // =========================
        if (Hardware.eBrakeMomentarySwitch2.isOnCheckNow() == true)
            {
            // =========================
            // when the eBrake is not retracted and the joystick is moved
            // Resets the eBrake timer retracts the eBrake piston, stops all
            // drive motors,
            // and starts the eBrake timer
            // =========================
            Hardware.eBrakeMomentarySwitch1.setValue(false);
            if ((Hardware.eBrake.getForward() == true) || ((Math
                    .abs(Hardware.leftDriver.getY()) >= Hardware.PREV_DEADBAND)
                    || (Math.abs(Hardware.rightDriver
                            .getY()) >= Hardware.PREV_DEADBAND)))
                {
                Hardware.eBrakeTimer.reset();
                Hardware.eBrakeTimer.start();
                Hardware.eBrake.setForward(false);
                }
            // =========================
            // when the eBrake is retracted and the eBrake timer has passed
            // a
            // certain
            // duration
            // Reactivates the drive motors and stops the eBrake timer
            // =========================
            if ((Hardware.eBrake.getForward() == false)
                    && ((Hardware.eBrakeTimer.hasElapsed(3.0))
                            || Hardware.eBrakeTimerIsStopped == true))
                {
                Hardware.eBrakeTimer.stop();
                }
            }
        // =========================
        // when the retract button (left driver button 6) is pressed, eBrake is
        // not
        // retracted and the joystick is moved
        // Resets the eBrake timer retracts the eBrake piston, stops all drive
        // motors,
        // and starts the eBrake timer
        // =========================
        if ((Hardware.eBrake.getForward() == true) && ((Math
                .abs(Hardware.leftDriver.getY()) >= Hardware.PREV_DEADBAND)
                || (Math.abs(Hardware.rightDriver
                        .getY()) >= Hardware.PREV_DEADBAND)))
            {
            Hardware.eBrakeTimer.reset();
            Hardware.eBrakeTimer.start();
            Hardware.eBrake.setForward(false);
            }
        // =========================
        // when the eBrake is retracted and the eBrake timer has passed a
        // certain
        // duration
        // Reactivates the drive motors and stops the eBrake timer
        // =========================
        if ((Hardware.eBrake.getForward() == false)
                && ((Hardware.eBrakeTimer.hasElapsed(3.0))
                        || Hardware.eBrakeTimerIsStopped == true))
            {
            Hardware.eBrakeTimer.stop();
            }
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
            Hardware.armRaisePiston.setForward(false);
            }
        else
            {
            Hardware.armRaisePiston.setForward(true);
            }

        // Arm motor controls
        if (Hardware.rightOperator.getY() >= -Hardware.armControlDeadband
                && Hardware.rightOperator.getY() <= Hardware.armControlDeadband)
            {
            Hardware.armRaiseMotor.set(Hardware.armControlHoldSpeed);
            }
        else
            {
            if (Hardware.rightOperator.getY() < -Hardware.armControlDeadband)
                {
                Hardware.armRaiseMotor
                        .set(1.25156445 * Hardware.rightOperator.getY()
                                + 0.25156445);
                }
            if (Hardware.rightOperator.getY() > Hardware.armControlDeadband)
                {
                Hardware.armRaiseMotor
                        .set(1.25 * Hardware.rightOperator.getY() - 0.25);
                }
            }

        if (Hardware.leftOperator.getY() >= -Hardware.armLengthDeadband
                && Hardware.leftOperator.getY() <= Hardware.armLengthDeadband)
            {
            Hardware.armLengthMotor.set(Hardware.armLengthHoldSpeed);
            }
        else
            {
            if (Hardware.leftOperator.getY() < -Hardware.armLengthDeadband)
                {
                Hardware.armLengthMotor
                        .set(1.25 * Hardware.leftOperator.getY() + 0.25);
                }
            if (Hardware.leftOperator.getY() > Hardware.armLengthDeadband)
                {
                Hardware.armLengthMotor
                        .set(1.25 * Hardware.leftOperator.getY() - 0.25);
                } // end if

            }

    } // end of armControl()

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
        Hardware.cameras.switchCameras(Hardware.switchCameraViewButton10);

        // -------------------------
        // If eBrake has not overridden our ability to
        // drive, use the drivers joysticks to drive.
        // ----------------------------
        if (Hardware.eBrakeTimerIsStopped == true)
            {
            Hardware.transmission.shiftGears(Hardware.rightDriver.getTrigger(),
                    Hardware.leftDriver.getTrigger());
            Hardware.transmission.drive(Hardware.leftDriver.getY(),
                    Hardware.rightDriver.getY());
            } // if

        // --------------------------
        // control the eBrake and
        // the arm by the operator
        // ----------------------------
        armControl();
        manageEBrake();
        // --------------------------
        // all print statement and
        // individual testing function
        // --------------------------
        printStatements();
        individualTest();
    }

    public static void individualTest()
    {
        // people test functions
    } // end individualTest()

    public static void printStatements()
    {
        // ========== INPUTS ==========
        // System.out.println("eBrakeTimer " + Hardware.eBrakeTimer.get());
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

        /////////// JOYSTICK VALUES ///////////
        // System.out.println("L Joystick: " + Hardware.leftDriver.getY());
        // System.out.println("R Joystick: " + Hardware.rightDriver.getY());
        // ========== OUTPUTS ==========

        // ---------- DIGITAL ----------
        // System.out.println("disableAutoSwitch = " +
        // Hardware.disableAutoSwitch.isOn());
        // ---------- ANALOG -----------

        // ----------- CAN -------------

        /////////// MOTOR VALUES ///////////
        // System.out.println("LBottomMotor = " +
        /////////// Hardware.leftBottomMotor.get());
        // System.out.println("LTopMotor = " + Hardware.leftTopMotor.get());
        // System.out.println("RBottomMotor = " +
        /////////// Hardware.rightBottomMotor.get());
        // System.out.println("RTopMotor = " + Hardware.rightTopMotor.get());
        // System.out.println("armLengthMotor = " +
        /////////// Hardware.armLengthMotor.get());
        // System.out.println("armRaiseMotor = " +
        /////////// Hardware.armRaiseMotor.get());
        // -------- SUBSYSTEMS ---------

        // ---------- OTHER ------------

    } // end printStatements()

    // =========================================
    // class private data goes here
    // =========================================

    } // end class
