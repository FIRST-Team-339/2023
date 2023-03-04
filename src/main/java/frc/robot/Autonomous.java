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

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import frc.Hardware.Hardware;
import frc.Utils.drive.Drive;

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
        delayTime = Hardware.delayPot.get(0, MAX_DELAY_SECONDS_PREV_YEAR);
        Hardware.drive.setGear(0);
        Hardware.drive.setGearPercentage(0, AUTO_GEAR);
        Hardware.drive.setBrakeStoppingDistance(7.5);
        // Hardware.testGyro.reset();

        if (Hardware.disableAutoSwitch.isOn() == true)
            {
            // added delay potentionmeter working

            // =========================
            // when in the six position switch is in a certain it will do
            // one of
            // the following
            // =========================
            System.out.println("Six Position Switch value: "
                    + Hardware.sixPosSwitch.getPosition());
            switch (Hardware.sixPosSwitch.getPosition())
                {
                // =========================
                // Postition 1: when the robot is in the shorter length of
                // the
                // community it will be placed 4 inches away from the line,
                // drive forward 140 inches, and stop 16 inches away from
                // the
                // game piece then stops
                // =========================
                case 0:
                    autoPath = AUTO_PATH.DRIVE_ONLY_FORWARD;
                    break;
                // =========================
                // Position 2: The robot is placed 3 inches away from the
                // line
                // of the long side of the community and 8 inches away from
                // the
                // charging station, drive 44 inches forward, turns 90
                // degrees
                // in a direction that will be controlled or just stops by a
                // double throw switch, then drive 44 inches and stops
                // =========================
                case 1:
                    autoPath = AUTO_PATH.DRIVE_TURN_DRIVE;
                    break;
                // =========================
                //
                // =========================
                case 2:
                    autoPath = AUTO_PATH.DISABLE;
                    break;
                // drive onto platform
                // put front bumper of robot in front of the scoring zone,
                // behind the community line, and in front of the charge station
                // 1. The robot will accelerate

                // =========================
                //
                // =========================
                case 3:
                    autoPath = AUTO_PATH.DISABLE;
                    break;
                // =========================
                //
                // =========================
                case 4:
                    autoPath = AUTO_PATH.DISABLE;
                    break;
                // =========================
                //
                // =========================
                case 5:
                    autoPath = AUTO_PATH.DISABLE;
                    break;
                // =========================
                //
                // =========================
                default:
                    autoPath = AUTO_PATH.DISABLE;
                    break;

                }
            }
        else
            {
            autoPath = AUTO_PATH.DISABLE;
            }

        driveOnlyForwardState = DRIVE_ONLY_FORWARD_STATE.INIT;
        driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.INIT;

        Hardware.drive.setGearPercentage(0, 0.3);
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
        switch (autoPath)
            {
            case DRIVE_ONLY_FORWARD:
                if (driveOnlyForward() == true)
                    {
                    autoPath = AUTO_PATH.DISABLE;
                    }
                break;

            case DRIVE_TURN_DRIVE:
                if (driveTurnDrive() == true)
                    {
                    autoPath = AUTO_PATH.DISABLE;
                    }
                break;

            case DISABLE:
                Hardware.drive.stop();
                break;

            default:
                break;
            }
    }

    // =====================================================================
    // Path Methods
    // =====================================================================

    /**
     * Long drive forward for autonomous.
     *
     * @author Bryan Fernandez
     * @written February 18, 2023
     */
    private static boolean driveOnlyForward()
    {
        switch (driveOnlyForwardState)
            {
            case INIT:
                Hardware.autoTimer.start();
                driveOnlyForwardState = DRIVE_ONLY_FORWARD_STATE.DELAY;
                return false;
            case DELAY:
                if (Hardware.autoTimer.get() >= delayTime)
                    {
                    driveOnlyForwardState = DRIVE_ONLY_FORWARD_STATE.DRIVE;
                    }
                return false;
            case DRIVE:
                if (Math.abs(Hardware.rightBottomEncoder.getDistance()) < 133.5)
                    {
                    Hardware.drive.accelerateProportionaly(LEFT_SPEED,
                            RIGHT_SPEED, 2);
                    }
                else
                    {
                    driveOnlyForwardState = DRIVE_ONLY_FORWARD_STATE.STOP;
                    }
                return false;
            case STOP:
                if (Hardware.drive.brake(Drive.BrakeType.AFTER_DRIVE))
                    {
                    driveOnlyForwardState = DRIVE_ONLY_FORWARD_STATE.END;
                    }
                return false;
            case END:
                Hardware.drive.stop();
                return false;
            default:
                return false;
            }
    }

    /**
     * Short drives forward and turns for autonomous.
     *
     * @author Bryan Fernandez
     * @written February 18, 2023
     */
    private static boolean driveTurnDrive()
    {
        switch (driveTurnDriveState)
            {
            case INIT:
                System.out.println("Started " + driveTurnDriveState);
                Hardware.autoTimer.start();
                driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.DELAY;
                return false;

            case DELAY:
                System.out.println("Started " + driveTurnDriveState);
                if (Hardware.autoTimer.get() >= delayTime)
                    {
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.DRIVE_ONE_ACCEL;
                    }
                return false;

            case DRIVE_ONE_ACCEL:
                System.out.println("Started " + driveTurnDriveState);
                if (Hardware.drive.accelerateProportionaly(LEFT_SPEED,
                        RIGHT_SPEED, 2) == true)
                    {
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.DRIVE_ONE_DRIVE;
                    }
                return false;

            case DRIVE_ONE_DRIVE:
                if (Hardware.drive.driveStraightInches(FIRST_STOP_DISTANCE,
                        -DRIVE_ONE_DRIVE_SPEED, DRIVE_ONE_DRIVE_ACCELERATE,
                        true))
                    {
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.STOP_ONE;
                    }
                return false;

            case STOP_ONE:
                System.out.println("Started " + driveTurnDriveState);
                if (Hardware.drive.brake(Drive.BrakeType.AFTER_DRIVE) == true)
                    {
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.DECIDE_NEXT;
                    }
                return false;

            case DECIDE_NEXT:
                if (Hardware.leftRightNoneSwitch.getPosition() == Value.kOff)
                    {
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.DRIVE_TWO_ACCEL;
                    }
                else
                    {
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.TURN;
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.END;
                    }
                return false;

            case TURN:
                System.out.println("Started " + driveTurnDriveState);
                System.out.println(
                        Math.abs(Hardware.rightBottomEncoder.getDistance()));
                /*
                 * switch (Hardware.leftRightNoneSwitch.getPosition()) { case
                 * kOff: driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.STOP_TWO;
                 * break;
                 * 
                 * case kForward: //
                 * Hardware.drive.accelerateProportionaly(0.22, -0.22, // 9); if
                 * (Hardware.drive.turnDegrees(90, 0.22, 0.99, false)) {
                 * driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.STOP_TURN; }
                 * break;
                 * 
                 * case kReverse: //
                 * Hardware.drive.accelerateProportionaly(-0.22, 0.22, // 9); if
                 * (Hardware.drive.turnDegrees(90, 0.22, 0.99, false)) {
                 * driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.STOP_TURN; }
                 * break;
                 * 
                 * default: break;
                 * 
                 * }
                 */

                // if (Hardware.leftRightNoneSwitch
                // .getPosition() == Relay.Value.kOff)
                // {
                // driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.END;
                // }

                if (Hardware.leftRightNoneSwitch
                        .getPosition() == Relay.Value.kForward)
                    {
                    if (Hardware.drive.turnDegrees(90, -0.22, 0.99, false))
                        {
                        driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.STOP_TURN;
                        }
                    }

                if (Hardware.leftRightNoneSwitch
                        .getPosition() == Relay.Value.kReverse)
                    {
                    if (Hardware.drive.turnDegrees(-90, -0.22, 0.99, false))
                        {
                        driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.STOP_TURN;
                        }
                    }

                return false;

            case STOP_TURN:
                System.out.println("Started " + driveTurnDriveState);
                // Hardware.drive.brake(Drive.BrakeType.AFTER_TURN);
                driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.END;
                return false;

            case DRIVE_TWO_ACCEL:
                System.out.println("Started " + driveTurnDriveState);
                if (Hardware.drive.accelerateProportionaly(-0.22, -0.22,
                        2) == true)
                    {
                    Hardware.drive.resetEncoders();
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.DRIVE_TWO_DRIVE;
                    }
                return false;

            case DRIVE_TWO_DRIVE:
                System.out.println("Started " + driveTurnDriveState);
                if (Math.abs(Hardware.rightBottomEncoder.getDistance()) < 44)
                    {
                    Hardware.transmission.driveRaw(LEFT_SPEED, RIGHT_SPEED);
                    }
                else
                    {
                    driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.STOP_TWO;
                    }
                return false;

            case STOP_TWO:
                System.out.println("Started " + driveTurnDriveState);
                // Hardware.drive.brake(Drive.BrakeType.AFTER_DRIVE);
                driveTurnDriveState = DRIVE_TURN_DRIVE_STATE.END;
                return false;

            case END:
                System.out.println("Started " + driveTurnDriveState);
                Hardware.drive.stop();
                return false;

            default:
                return false;
            }
    }

    private static enum AUTO_PATH
        {
        DRIVE_ONLY_FORWARD, DRIVE_TURN_DRIVE, DISABLE;
        }

    private static enum DRIVE_ONLY_FORWARD_STATE
        {
        INIT, DELAY, DRIVE, STOP, END;
        }

    private static enum DRIVE_TURN_DRIVE_STATE
        {
        INIT, DELAY, DRIVE_ONE_ACCEL, DRIVE_ONE_DRIVE, STOP_ONE, DECIDE_NEXT, TURN, STOP_TURN, DRIVE_TWO_ACCEL, DRIVE_TWO_DRIVE, STOP_TWO, END;
        }

    private static AUTO_PATH autoPath;

    private static DRIVE_ONLY_FORWARD_STATE driveOnlyForwardState;

    private static DRIVE_TURN_DRIVE_STATE driveTurnDriveState;

    private static double delayTime;
    /*
     * ============================================================== Constants
     * ==============================================================
     */
    private static final double MAX_DELAY_SECONDS_PREV_YEAR = 5.0;

    private static final double AUTO_GEAR = 1;

    private static final double LEFT_SPEED = -0.22;

    private static final double RIGHT_SPEED = -0.22;

    private static final double DRIVE_ONE_DRIVE_SPEED = 0.3;

    private static final double DRIVE_ONE_DRIVE_ACCELERATE = 0.5;

    private static final double FIRST_STOP_DISTANCE = 44.0;

    }