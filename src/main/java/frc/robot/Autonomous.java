/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/
// ====================================================================
// FILE NAME: Autonomous.java (Team 339 - Kilroy)
//
// CREATED ON: Jan 13, 2015
// CREATED BY: Nathanial Lydick
// MODIFIED ON:
// MODIFIED BY:
// ABSTRACT:
// This file is where almost all code for Kilroy will be
// written. Some of these functions are functions that should
// override methods in the base class (IterativeRobot). The
// functions are as follows:
// -----------------------------------------------------
// Init() - Initialization code for autonomous mode
// should go here. Will be called each time the robot enters
// autonomous mode.
// -----------------------------------------------------
// Periodic() - Periodic code for autonomous mode should
// go here. Will be called periodically at a regular rate while
// the robot is in autonomous mode.
// -----------------------------------------------------
//
// NOTE: Please do not release this code without permission from
// Team 339.
// ====================================================================
package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import frc.Hardware.Hardware;

/**
 * An Autonomous class. This class <b>beautifully</b> uses state machines in
 * order to periodically execute instructions during the Autonomous period.
 *
 * This class contains all of the user code for the Autonomous part of the
 * match, namely, the Init and Periodic code
 *
 *
 * @author Michael Andrzej Klaczynski
 * @written at the eleventh stroke of midnight, the 28th of January, Year of our
 *          LORD 2016. Rewritten ever thereafter.
 *
 * @author Nathanial Lydick
 * @written Jan 13, 2015
 */
public class Autonomous
    {

    /**
     * User Initialization code for autonomous mode should go here. Will run
     * once when the autonomous first starts, and will be followed immediately
     * by periodic().
     */
    public static void init()
    {
        Hardware.eBrake.setForward(false);
        Hardware.eBrakeTimer.stop();
        Hardware.eBrakeTimer.reset();
        Hardware.rightBottomEncoder.reset();
    } // end Init

    /**
     * User Periodic code for autonomous mode should go here. Will be called
     * periodically at a regular rate while the robot is in autonomous mode.
     *
     * @author Nathanial Lydick
     * @written Jan 13, 2015
     *
     *          FYI: drive.stop cuts power to the motors, causing the robot to
     *          coast. drive.brake results in a more complete stop. Meghan
     *          Brown; 10 February 2019
     *
     */

    public static void periodic()
    {

        if (Hardware.disableAutoSwitch.isOn() == false)
            {
            // added delay potentionmeter working
            double delayTime;
            delayTime = Hardware.delayPot.get(0, 270);
            Timer.delay(delayTime);
            // =========================
            // when in the six position switch is in a certain it will do one of
            // the following
            // =========================
            switch (Hardware.sixPosSwitch.getPosition())
                {
                // =========================
                // Postition 1: when the robot is in the shorter length of the
                // community it will be placed 4 inches away from the line,
                // drive forward 140 inches, and stop 16 inches away from the
                // game piece then stops
                // =========================
                case 1:
                    break;
                // =========================
                // Position 2: The robot is placed 3 inches away from the line
                // of the long side of the community and 8 inches away from the
                // charging station, drive 44 inches forward, turns 90 degrees
                // in a direction that will be controlled or just stops by a
                // double throw switch, then drive 44 inches and stops
                // =========================
                case 2:
                    break;
                // =========================
                //
                // =========================
                case 3:
                    break;
                // =========================
                //
                // =========================
                case 4:
                    break;
                // =========================
                //
                // =========================
                case 5:
                    break;
                case 6:
                    break;
                // =========================
                //
                // =========================
                default:
                    break;

                }
            }
        else
            {
            // code goes here
            }

        // added delay potentionmeter working
        double delayTime;
        delayTime = Hardware.delayPot.get(0, 270);
        Timer.delay(delayTime);
        // =========================
        // when in the six position switch is in a certain it will do one of the
        // following
        // =========================
        switch (Hardware.sixPosSwitch.getPosition())
            {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                break;

            }
        // }
    }

    // =====================================================================
    // Path Methods
    // =====================================================================

    /*
     * ============================================================== Constants
     * ==============================================================
     */
    }
