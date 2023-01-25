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
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.Hardware.Hardware;
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
     * User Initialization code for teleop mode should go here. Will be called once
     * when the robot enters teleop mode.
     *
     * @author Nathanial Lydick
     * @written Jan 13, 2015
     */
    public static void init()
    {
        Hardware.drive.setGear(0);

        Hardware.eBrake.setForward(false);
        Hardware.eBrakeTimer.stop();
        Hardware.eBrakeTimer.reset();
    } // end init()

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
        if (Hardware.eBrakeTimer.get() <= 0.001)
            {
            Hardware.eBrakeTimerIsStopped = true;
            }
        else
            {
            Hardware.eBrakeTimerIsStopped = false;
            }

        // ================= OPERATOR CONTROLS ================

        Hardware.cameras.switchCameras(Hardware.switchCameraViewButton10);
        Hardware.cameras.switchCameras(Hardware.switchCameraViewButton9);
        // ================== DRIVER CONTROLS =================

        Hardware.transmission.shiftGears(Hardware.rightDriver.getTrigger(), Hardware.leftDriver.getTrigger());
        // Hardware.transmission.drive(Hardware.leftDriver.getY(),
        // Hardware.rightDriver.getY());

        // =========================
        // when button 5 right driver is pushed
        // Extends the eBrake piston out
        // =========================
        if (Hardware.rightDriver.getRawButton(5) == true)
            {
            Hardware.eBrake.setForward(true);
            }

        // =========================
        // when button 6 left driver is pushed
        // Retracts the eBrake piston and affects drive
        // =========================
        if (Hardware.leftDriver.getRawButton(6) == true)
            {
            // =========================
            // when the eBrake is not retracted and the joystick is moved
            // Resets the eBrake timer retracts the eBrake piston, stops all drive motors,
            // and starts the eBrake timer
            // =========================
            if ((Hardware.eBrake.getForward() == true)
                    || ((Math.abs(Hardware.leftDriver.getY()) >= Hardware.PREV_DEADBAND)
                            || (Math.abs(Hardware.rightDriver.getY()) >= Hardware.PREV_DEADBAND)))
                {
                Hardware.eBrakeTimer.reset();
                Hardware.transmission.drive(0, 0);
                Hardware.eBrakeTimer.start();
                Hardware.eBrake.setForward(false);
                }
            // =========================
            // when the eBrake is retracted and the eBrake timer has passed a certain
            // duration
            // Reactivates the drive motors and stops the eBrake timer
            // =========================
            if ((Hardware.eBrake.getForward() == false)
                    && ((Hardware.eBrakeTimer.hasElapsed(1.5)) || Hardware.eBrakeTimerIsStopped == true))
                {
                Hardware.transmission.drive(Hardware.leftDriver.getY(), Hardware.rightDriver.getY());
                Hardware.eBrakeTimer.stop();
                }
            }
        // =========================
        // when the retract button (left driver button 6) is pressed, eBrake is not
        // retracted and the joystick is moved
        // Resets the eBrake timer retracts the eBrake piston, stops all drive motors,
        // and starts the eBrake timer
        // =========================
        if ((Hardware.eBrake.getForward() == true) && ((Math.abs(Hardware.leftDriver.getY()) >= Hardware.PREV_DEADBAND)
                || (Math.abs(Hardware.rightDriver.getY()) >= Hardware.PREV_DEADBAND)))
            {
            Hardware.eBrakeTimer.reset();
            Hardware.transmission.drive(0, 0);
            Hardware.eBrakeTimer.start();
            Hardware.eBrake.setForward(false);
            }
        // =========================
        // when the eBrake is retracted and the eBrake timer has passed a certain
        // duration
        // Reactivates the drive motors and stops the eBrake timer
        // =========================
        if ((Hardware.eBrake.getForward() == false)
                && ((Hardware.eBrakeTimer.hasElapsed(1.5)) || Hardware.eBrakeTimerIsStopped == true))
            {
            Hardware.transmission.drive(Hardware.leftDriver.getY(), Hardware.rightDriver.getY());
            Hardware.eBrakeTimer.stop();
            }

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

        // ---------- DIGITAL ----------

        // Encoder Distances

        // Encoder Raw Values

        // Switch Values

        /////////// SIX POSITION SWITCH ///////////
        // System.out.println("Six Position Switch value: " +
        /////////// Hardware.sixPosSwitch.getPosition());

        /////////// DISABLE AUTO SWITCH ///////////
        // System.out.println("Disable Auto Switch value: " +
        /////////// Hardware.disableAutoSwitch.isOn());

        // ---------- ANALOG -----------

        // System.out.println("delayPot = " + Hardware.delayPot.get());

        // ----------- CAN -------------

        // -------- SUBSYSTEMS ---------

        // ---------- OTHER ------------

        /////////// JOYSTICK VALUES ///////////
        // System.out.println("L Joystick: " + Hardware.leftDriver.getY());
        // System.out.println("R Joystick: " + Hardware.rightDriver.getY());

        // ========== OUTPUTS ==========

        // ---------- DIGITAL ----------
        System.out.println("disableAutoSwitch = " + Hardware.disableAutoSwitch.isOn());
        // ---------- ANALOG -----------

        // ----------- CAN -------------

        /////////// MOTOR VALUES ///////////
        // System.out.println("LBottomMotor = " + Hardware.leftBottomMotor.get());
        // System.out.println("LTopMotor = " + Hardware.leftTopMotor.get());
        // System.out.println("RBottomMotor = " + Hardware.rightBottomMotor.get());
        // System.out.println("RTopMotor = " + Hardware.rightTopMotor.get());

        // -------- SUBSYSTEMS ---------

        // ---------- OTHER ------------

    } // end printStatements()

    // =========================================
    // class private data goes here
    // =========================================

    } // end class
