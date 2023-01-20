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
import edu.wpi.first.wpilibj.buttons.JoystickButton;
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

        // ================= OPERATOR CONTROLS ================

        // Hardware.cameras.switchCameras(Hardware.switchCameraViewButton10);
        // if (Hardware.rightOperator.getRawButton(10) == true)
        // {
        // Hardware.cameras.switchCameras();
        // }
        // ================== DRIVER CONTROLS =================

        Hardware.transmission.shiftGears(Hardware.rightDriver.getTrigger(), Hardware.leftDriver.getTrigger());
        Hardware.transmission.drive(Hardware.leftDriver.getY(), Hardware.rightDriver.getY());

        if (Hardware.rightDriver.getRawButton(5))
            {
            Hardware.breakTestPiston.setForward(true);
            }

        if (Hardware.leftDriver.getRawButton(6))
            {
            Hardware.breakTestPiston.setForward(false);
            }

        if ((Hardware.breakTestPiston.getReverse() == false)
                && ((Math.abs(Hardware.rightDriver.getY()) >= Hardware.PREV_DEADBAND)
                        || ((Math.abs(Hardware.leftDriver.getY()) >= Hardware.PREV_DEADBAND))))
            {
            Hardware.breakTestPiston.setForward(false);
            // testTimer.start();

            }

        if (Hardware.rightDriver.getRawButton(2) == true)
            {
            printStatements();
            }
        individualTest();
    } // end periodic()

    public static void individualTest()
    {
        // people test functions
    } // end individualTest()

    public static void printStatements()
    {
        // ========== INPUTS ==========
            {

            }
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

        System.out.println("delayPot = " + Hardware.delayPot.get());

        // ----------- CAN -------------

        // -------- SUBSYSTEMS ---------

        // ---------- OTHER ------------

        /////////// JOYSTICK VALUES ///////////
        // System.out.println("L Joystick: " + Hardware.leftDriver.getY());
        // System.out.println("R Joystick: " + Hardware.rightDriver.getY());

        // ========== OUTPUTS ==========

        // ---------- DIGITAL ----------

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
    private static Timer testTimer = new Timer();

    } // end class
