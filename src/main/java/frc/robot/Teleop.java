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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Hardware.Hardware;
import frc.Utils.Dashboard;
import frc.Utils.Dashboard.AutoModeDash;

/**
 * This class contains all of the user code for the Autonomous part of the
 * match, namely, the Init and Periodic code
 *
 * @author Nathanial Lydick
 * @written Jan 13, 2015
 */
public class Teleop
    {
    // Accepts "ARCADE" or "DIFF"
    private static double maxSpeed;

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
        
            // Hardware.drive.setGearPercentage(0,
            // Hardware.DEMO_MODE_GEAR_MAX_SPEED);
            Hardware.drive.setGearPercentage(0,
                    Hardware.CURRENT_GEAR1_MAX_SPEED);
            Hardware.clawPiston.setForward(false);
            Hardware.clawTriggerButton.setValue(true);
            
        Hardware.leftBottomMotor.set(0.0);
        Hardware.rightBottomMotor.set(0.0);

        Hardware.rightBottomEncoder.reset();
        Hardware.leftBottomEncoder.reset();
        // Piston position setting
        Hardware.armRaiseButton.setValue(true);
        Hardware.armRaisePiston.setForward(true);

        maxSpeed = SmartDashboard.getNumber("DB/Slider 0", 2.5) / 5;
        // System.out.println(Hardware.demoModeGearPercent);

    } // end init()


 

    

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

        
        

        // -----------------
        // Arm motor controls
        // ------------------

        // If right operator Y value is between -0.2 and +0.2 then the
        // armRaiseMotor will equal the armControlHoldSpeed
        if (Hardware.rightOperator.getY() >= -Hardware.armControlDeadband
                && Hardware.rightOperator.getY() <= Hardware.armControlDeadband)
            {
            Hardware.armRaiseMotor.set(Hardware.armControlHoldSpeed);
            }
        else
            {
            // If right operator Y value is less than the armControlDeadband
            // then the ArmRaiseMotor will equal the equation below

            
            if ((Hardware.rightOperator.getY() < -Hardware.armControlDeadband))
                {
                Hardware.armRaiseMotor.set(((-Hardware.armRaiseMaxSpeedDown
                        + Hardware.armRaiseMinSpeedNegative)
                        / (-Hardware.maxJoystickOperatorValue
                                + Hardware.minJoystickOperatorValue))
                        * (Hardware.rightOperator.getY()
                                + Hardware.minJoystickOperatorValue)
                        - Hardware.armRaiseMinSpeedNegative);

                } // end if
            // If right operator Y value is greater than the
            // armControlDeadband
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
        

    } // end of armControl()

    private static void updateDashboard()
    {
        // GYRO
        Dashboard.updateGyroInd();
       
        // AUTO
        Dashboard.updateAutoModeInd(AutoModeDash.Teleop);

        // UTILS
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

        armControl();

                // DIFF DRIVE
                Hardware.transmission.drive(
                        Hardware.leftDriver.getY() * maxSpeed,
                        -Hardware.rightDriver.getY() * maxSpeed);
                
        // --------------------------
        // update dashboard values
        // --------------------------
        updateDashboard();

        // --------------------------
        // all print statement and
        // individual testing function
        // --------------------------
        printStatements();


    } // end periodic()



    public static void printStatements()
    {
            {
            // ========== INPUTS ==========
            // System.out.println("eBrakeTimer: " + Hardware.eBrakeTimer.get());
            // System.out.println("eBrakeJoyStickTimer has passed " + 2 + "
            // seconds:
            // "
            // + Hardware.eBrakeJoystickTimer.hasElapsed(eBrakeHoldTime));
            // System.out.println(
            // "eBrakeJoyStickTimer: " + Hardware.eBrakeJoystickTimer.get());
            // System.out.println("clawPiston = " + Hardware.clawPiston.get());
            // System.out.println("armPiston = " +
            // Hardware.armRaisePiston.get());
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
            // System.out.println("delayPot = " + Hardware.delayPot.get(0.0,
            // 5.0));
            // System.out.println("delayPot = " + Hardware.delayPot.get(270.0));

            // -------- SUBSYSTEMS ---------

            // ---------- OTHER ------------
            // System.out.println("ARE = " + Hardware.armRaiseEncoder.getRaw());
            /////////// JOYSTICK VALUES ///////////
            // System.out.println("L Joystick: " + Hardware.leftDriver.getY());
            // System.out.println("R Joystick: " + Hardware.rightDriver.getY());

            // System.out.println("Gyro angle: " + Hardware.agyro.getAngle());

            // System.out.println("Accel x, z " + Hardware.accelerometer.getX()
            // + " " + Hardware.accelerometer.getZ() + " "
            // + Hardware.accelerometerInitialZ);
            // ========== OUTPUTS ==========

            // ---------- DIGITAL ----------
            // System.out.println("disableAutoSwitch = " +
            // Hardware.disableAutoSwitch.isOn());
            // System.out.println("RedLight = " +
            // Hardware.redLightSensor.isOn());
            // System.out.println(
            // "Bottom Limit Switch = " + Hardware.bottomArmSwitch.isOn());
            // System.out.println(
            // "Top Limit Switch = " + Hardware.topArmSwitch.isOn());
            // ---------- ANALOG -----------

            // ----------- CAN -------------

            /////////// MOTOR VALUES ///////////
            // System.out.println(
            // "LBottomMotor Voltage= " + Hardware.leftBottomMotor.get());
            // System.out.println("LTopMotor = " + Hardware.leftTopMotor.get());
            // System.out.println("RBottomMotor Voltage = "
            // + Hardware.rightBottomMotor.get());
            // System.out.println("RTopMotor = " +
            /////////// Hardware.rightTopMotor.get());
            // System.out.println("LeMotor = " + Hardware.armLengthMotor.get()
            // + " Y = " + Hardware.leftOperator.getY());
            // System.out.println("RaMotor = " + Hardware.armRaiseMotor.get() +
            /////////// " Y
            /////////// = "
            // + Hardware.rightOperator.getY());

            // -------- SUBSYSTEMS ---------

            // ---------- OTHER ------------

            }
    } // end printStatements()


    } // end class
