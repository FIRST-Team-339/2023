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

        Hardware.brakePistion.setForward(false);
        Hardware.brakeTimer.stop();
        Hardware.brakeTimer.reset();
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
        // Hardware.transmission.drive(Hardware.leftDriver.getY(),
        // Hardware.rightDriver.getY());

        // If button 5 on the right joystick is pressed
        if (Hardware.rightDriver.getRawButton(5))
            {
            Hardware.brakePistion.setForward(true);
            }

        // If button 6 on the left joystick is pressed
        if (Hardware.leftDriver.getRawButton(6))
            {
            // If the brake pistion is extended, or the abolute value of the left
            // joystick is greater than or equal to the previous year's deadband or the
            // abolute value of the right joystick is greater than, or equal to the previous
            // year's deadband
            if ((Hardware.brakePistion.getForward() == true)
                    || ((Math.abs(Hardware.leftDriver.getY()) >= Hardware.PREV_DEADBAND)
                            || (Math.abs(Hardware.rightDriver.getY()) >= Hardware.PREV_DEADBAND)))
                {
                Hardware.brakeTimer.reset();
                Hardware.transmission.drive(0, 0);
                Hardware.brakeTimer.start();
                Hardware.brakePistion.setForward(false);
                }
            // If the Brake Pistion is retracked, and either the brake timer has gone over
            // three seconds or is at zero
            if ((Hardware.brakePistion.getForward() == false)
                    && ((Hardware.brakeTimer.hasElapsed(1.5)) || Hardware.brakeTimer.get() == 0))
                {
                Hardware.transmission.drive(Hardware.leftDriver.getY(), Hardware.rightDriver.getY());
                Hardware.brakeTimer.stop();
                }
            }

        // If the brake pistion is extended, and either the abolute value of the left
        // joystick is greater than or equal to the previous year's deadband or the
        // abolute value of the right joystick is greater than, or equal to the previous
        // year's deadband
        if ((Hardware.brakePistion.getForward() == true)
                && ((Math.abs(Hardware.leftDriver.getY()) >= Hardware.PREV_DEADBAND)
                        || (Math.abs(Hardware.rightDriver.getY()) >= Hardware.PREV_DEADBAND)))
            {
            Hardware.brakeTimer.reset();
            Hardware.transmission.drive(0, 0);
            Hardware.brakeTimer.start();
            Hardware.brakePistion.setForward(false);
            }

        // If the Brake Pistion is retracked, and either the brake timer has gone over
        // three seconds or is at zero
        if ((Hardware.brakePistion.getForward() == false)
                && ((Hardware.brakeTimer.hasElapsed(1.5)) || Hardware.brakeTimer.get() == 0))
            {
            Hardware.transmission.drive(Hardware.leftDriver.getY(), Hardware.rightDriver.getY());
            Hardware.brakeTimer.stop();
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

        // System.out.println("delayPot = " + Hardware.delayPot.get());

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

    } // end class
