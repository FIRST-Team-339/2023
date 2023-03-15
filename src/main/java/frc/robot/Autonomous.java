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
import frc.Utils.Dashboard;
import frc.Utils.Dashboard.AutoModeDash;
import frc.Utils.Dashboard.DriveGear;
import frc.Utils.drive.Drive;
import frc.Utils.drive.Drive.BrakeType;

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
        Hardware.eBrakePiston.setForward(false);
        Hardware.eBrakeTimer.stop();
        Hardware.eBrakeTimer.reset();
        Hardware.rightBottomEncoder.reset();
        Hardware.leftBottomEncoder.reset();
        delayTime = Hardware.delayPot.get(0, MAX_DELAY_SECONDS_CURRENT_YEAR);
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
            // System.out.println("init.switch = "
            // + Hardware.sixPosSwitch.getPosition());
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
                    autoPath = AUTO_PATH.SW1_DRIVE_ONLY_FORWARD;
                    AUTO_MODE_DASH = AutoModeDash.Mode1;
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
                    autoPath = AUTO_PATH.SW2_DRIVE_TURN_DRIVE;
                    AUTO_MODE_DASH = AutoModeDash.Mode2;
                    break;
                // =========================
                //
                // =========================
                case 2:
                    autoPath = AUTO_PATH.SW3_DRIVE_OVER_CHARGING_STATION;
                    AUTO_MODE_DASH = AutoModeDash.Mode3;
                    break;
                // =========================
                //
                // =========================
                case 3:
                    autoPath = AUTO_PATH.DISABLE;
                    AUTO_MODE_DASH = AutoModeDash.Mode4;
                    break;
                // =========================
                //
                // =========================
                case 4:
                    autoPath = AUTO_PATH.DISABLE;
                    AUTO_MODE_DASH = AutoModeDash.Mode5;
                    break;
                // =========================
                //
                // =========================
                case 5:
                    autoPath = AUTO_PATH.DISABLE;
                    AUTO_MODE_DASH = AutoModeDash.Mode6;
                    break;
                // =========================
                //
                // =========================
                default:
                    autoPath = AUTO_PATH.DISABLE;
                    AUTO_MODE_DASH = AutoModeDash.Disabled;
                    break;

                } // end switch
            } // end if
        else
            {
            autoPath = AUTO_PATH.DISABLE;
            AUTO_MODE_DASH = AutoModeDash.Disabled;
            } // ens else

        sw1_driveOnlyForwardState = SW1_DRIVE_ONLY_FORWARD_STATE.INIT;
        sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.INIT;
        sw3_driveOnChargingStationState = SW3_DRIVE_ON_CHARGING_STATION_STATE.INIT;

        Hardware.drive.setGearPercentage(0, 1.0);
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
        // System.out.println("periodic.switch = " + autoPath);
        updateDashboard();
        switch (autoPath)
            {
            case SW1_DRIVE_ONLY_FORWARD:
                if (sw1_driveOnlyForward() == true)
                    {
                    autoPath = AUTO_PATH.DISABLE;
                    AUTO_MODE_DASH = AutoModeDash.Completed;
                    } // if
                break;

            case SW2_DRIVE_TURN_DRIVE:
                if (sw2_driveTurnDrive() == true)
                    {
                    autoPath = AUTO_PATH.DISABLE;
                    AUTO_MODE_DASH = AutoModeDash.Completed;
                    } // if
                break;

            case SW3_DRIVE_OVER_CHARGING_STATION:
                if (sw3_driveOnChargingStation() == true)
                    {
                    autoPath = AUTO_PATH.DISABLE;
                    AUTO_MODE_DASH = AutoModeDash.Completed;
                    }
                break;
            } // switch
    } // end periodic()

    // =====================================================================
    // Path Methods
    // =====================================================================

    /**
     * Long drive forward for autonomous.
     *
     * @author Bryan Fernandez
     * @written February 18, 2023
     */
    private static boolean sw1_driveOnlyForward()
    {
        // System.out.println(
        // "sw1_driveOnlyForward.switch = " + sw1_driveOnlyForwardState);
        switch (sw1_driveOnlyForwardState)
            {
            // ---------------------------
            // initialize everything we need for
            // this run
            // ---------------------------
            case INIT:
                Hardware.autoTimer.start();
                sw1_driveOnlyForwardState = SW1_DRIVE_ONLY_FORWARD_STATE.DELAY;
                return false;
            // ---------------------------
            // Delay if we set the pot to delay
            // ---------------------------
            case DELAY:
                if (Hardware.autoTimer.get() >= delayTime)
                    {
                    sw1_driveOnlyForwardState = SW1_DRIVE_ONLY_FORWARD_STATE.DRIVE_ONE_DRIVE;
                    Hardware.autoTimer.stop();
                    Hardware.autoTimer.reset();
                    } // if
                return false;

            // ---------------------------
            // Drive XX inches, accelerating first
            // using the gyro as the way to keep
            // us straight as we drive.
            // When we have gone the inches, get
            // ready for the braking actions and
            // reset the encoders - just in case
            // ---------------------------
            case DRIVE_ONE_DRIVE:
                if (Hardware.drive.driveStraightInches(SW1_DRIVE_ONLY_INCHES,
                        DRIVE_ONE_DRIVE_SPEED, MAX_ACCEL_TIME, false))
                    {
                    sw1_driveOnlyForwardState = SW1_DRIVE_ONLY_FORWARD_STATE.STOP;
                    Hardware.leftBottomEncoder.reset();
                    Hardware.rightBottomEncoder.reset();
                    Hardware.drive.setMaxBrakeIterations(3);
                    Hardware.drive.setBrakeDeadband(1, BrakeType.AFTER_DRIVE);
                    } // if
                return false;

            // ---------------------------
            // Now actually perform the braking
            // action. When complete, STOP
            // ---------------------------
            case STOP:
                if (Hardware.drive.brake(Drive.BrakeType.AFTER_DRIVE) == true)
                    {
                    sw1_driveOnlyForwardState = SW1_DRIVE_ONLY_FORWARD_STATE.END;
                    } // if
                return false;

            // ---------------------------
            // Stop the motors and let everything
            // know that we are done.
            // ---------------------------
            case END:
            default:
                Hardware.drive.stop();
                return true;
            } // switch
    } // end sw1_driveOnlyForward()

    /**
     * Short drives forward and turns for autonomous.
     *
     * @author Bryan Fernandez
     * @written February 18, 2023
     */
    private static boolean sw2_driveTurnDrive()
    {
        // System.out
        // .println("driveTurnDrive.switch = " + sw2_driveTurnDriveState);
        switch (sw2_driveTurnDriveState)
            {
            // ---------------------------
            // initialize everything we need for
            // this run
            // ---------------------------
            case INIT:
                Hardware.autoTimer.start();
                sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.DELAY;
                return false;

            // ---------------------------
            // Delay if we set the pot to delay
            // ---------------------------
            case DELAY:
                if (Hardware.autoTimer.get() >= delayTime)
                    {
                    sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.DRIVE_ONE_DRIVE;

                    Hardware.autoTimer.stop();
                    Hardware.autoTimer.reset();
                    } // if
                return false;

            // ---------------------------
            // Drive XX inches, accelerating first
            // using the gyro as the way to keep
            // us straight as we drive.
            // When we have gone the inches, get
            // ready for the braking actions and
            // reset the encoders - just in case
            // ---------------------------
            case DRIVE_ONE_DRIVE:
                if (Hardware.drive.driveStraightInches(SW2_FIRST_STOP_DISTANCE,
                        DRIVE_ONE_DRIVE_SPEED, MAX_ACCEL_TIME, false))
                    {
                    sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.STOP_ONE;
                    Hardware.leftBottomEncoder.reset();
                    Hardware.rightBottomEncoder.reset();
                    Hardware.drive.setMaxBrakeIterations(3);
                    Hardware.drive.setBrakeDeadband(1, BrakeType.AFTER_DRIVE);
                    } // if
                return false;

            // ---------------------------
            // Now actually perform the braking
            // action. When complete, STOP
            // ---------------------------
            case STOP_ONE:
                if (Hardware.drive.brake(Drive.BrakeType.AFTER_DRIVE) == true)
                    {
                    sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.DECIDE_NEXT;
                    } // if
                return false;

            // ---------------------------
            // Decide whether or not to turn
            // ---------------------------
            case DECIDE_NEXT:
                if (Hardware.leftRightNoneSwitch.getPosition() == Value.kOff)
                    {
                    sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.STOP_TWO;
                    } // if
                else
                    {
                    sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.TURN;
                    Hardware.leftBottomEncoder.reset();
                    Hardware.rightBottomEncoder.reset();
                    } // else
                return false;
            // ---------------------
            // perform the turn here - either
            // left or right
            // ---------------------
            case TURN:
                // ----------------------------
                // This is a Left turn
                // -------------------------
                if (Hardware.leftRightNoneSwitch
                        .getPosition() == Relay.Value.kReverse)
                    {
                    if (Hardware.drive.turnDegrees(-90, LEFT_ACCEL_SPEED,
                            MAX_ACCEL_TIME, false) == true)
                        {
                        sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.STOP_TURN;
                        } // if
                    } // if
                // ----------------------
                // right turn
                // -------------------
                if (Hardware.leftRightNoneSwitch
                        .getPosition() == Relay.Value.kForward)
                    {
                    if (Hardware.drive.turnDegrees(90, RIGHT_ACCEL_SPEED,
                            MAX_ACCEL_TIME, false) == true)
                        {
                        sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.STOP_TURN;
                        } // if
                    } // if
                return false;
            // ---------------------
            // we have completed the turn
            // ---------------------
            case STOP_TURN:
                // Hardware.drive.brake(Drive.BrakeType.AFTER_TURN);
                sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.DRIVE_TWO_DRIVE;
                return false;
            // -----------------------
            // drive straight the number of
            // inches requested
            // -----------------------
            case DRIVE_TWO_DRIVE:
                if (Hardware.drive.driveStraightInches(SW2_SECOND_STOP_DISTANCE,
                        DRIVE_ONE_DRIVE_SPEED, MAX_ACCEL_TIME, false) == true)
                    {
                    sw2_driveTurnDriveState = SW2_DRIVE_TURN_DRIVE_STATE.STOP_TWO;
                    Hardware.leftBottomEncoder.reset();
                    Hardware.rightBottomEncoder.reset();
                    Hardware.drive.setMaxBrakeIterations(3);
                    Hardware.drive.setBrakeDeadband(1, BrakeType.AFTER_DRIVE);
                    } // if
                return false;

            // --------------------
            // stop all motors
            // --------------------
            case STOP_TWO:
            case END:
            default:
                Hardware.drive.stop();
                return true;
            } // switch
    } // end sw2_driveTurnDrive()

    private static boolean sw3_driveOnChargingStation()
    {
        // System.out.println("sw3_driveOnChargingStation.switch = "
        // + sw3_driveOnChargingStationState);
        switch (sw3_driveOnChargingStationState)

            {
            // ----------------------
            // initialize everything we need for
            // this run
            // ----------------------
            case INIT:
                Hardware.autoTimer.start();
                Hardware.drive.setDriveStraightConstant(0.01);
                sw3_driveOnChargingStationState = SW3_DRIVE_ON_CHARGING_STATION_STATE.DELAY;
                return false;
            // ----------------------
            // Delay if we set pot to delay
            // ----------------------

            case DELAY:
                if (Hardware.autoTimer.get() >= delayTime)
                    {
                    sw3_driveOnChargingStationState = SW3_DRIVE_ON_CHARGING_STATION_STATE.DRIVE_ONE_DRIVE;
                    Hardware.autoTimer.stop();
                    Hardware.autoTimer.reset();
                    } // if
                return false;
            // ---------------------------
            // Drive XX inches, accelerating first
            // using the gyro as the way to keep
            // us straight as we drive.
            // When we have gone the inches, get
            // ready for the braking actions and
            // reset the encoders - just in case
            // ---------------------------

            case DRIVE_ONE_DRIVE:
                if (Hardware.drive.driveStraightInches(
                        SW3_DRIVE_OVER_CHARGING_STATION,
                        SW3_DRIVE_ONE_DRIVE_SPEED, MAX_ACCEL_TIME,
                        false) == true)
                    {
                    sw3_driveOnChargingStationState = SW3_DRIVE_ON_CHARGING_STATION_STATE.STOP_ONE;
                    Hardware.leftBottomEncoder.reset();
                    Hardware.rightBottomEncoder.reset();
                    Hardware.drive.setMaxBrakeIterations(3);
                    Hardware.drive.setBrakeDeadband(1, BrakeType.AFTER_DRIVE);
                    } // if
                return false;

            // ---------------------------
            // Now actually perform the braking
            // action. When complete, STOP
            // ---------------------------
            case STOP_ONE:
                if (Hardware.drive.brake(Drive.BrakeType.AFTER_DRIVE) == true)
                    {
                    sw3_driveOnChargingStationState = SW3_DRIVE_ON_CHARGING_STATION_STATE.DRIVE_TWO_DRIVE;
                    } // if
                return false;

            // ------------------
            // Drive XX inches forward until redLightSensor reads true
            // If redLightSensor reads true, the robot will move YY inches
            // forward
            // If robot drives XX inches and redLightSensor doesn't turn on, the
            // robot will stop
            // ------------------
            case DRIVE_TWO_DRIVE:
                if (Hardware.drive.driveStraightInches(
                        SW3_DRIVE_TOWARDS_CHARGING_STATION,
                        SW3_DRIVE_TWO_DRIVE_SPEED, MAX_ACCEL_TIME,
                        false) == true)
                    {
                    if (Hardware.redLightSensor.isOn() == true)
                        {
                        Hardware.drive.driveStraightInches(
                                SW3_DRIVE_ON_CHARGING_STATION,
                                SW3_DRIVE_TWO_DRIVE_SPEED, MAX_ACCEL_TIME,
                                false);
                        }
                    sw3_driveOnChargingStationState = SW3_DRIVE_ON_CHARGING_STATION_STATE.STOP_TWO;
                    Hardware.leftBottomEncoder.reset();
                    Hardware.rightBottomEncoder.reset();
                    Hardware.drive.setMaxBrakeIterations(3);
                    Hardware.drive.setBrakeDeadband(1, BrakeType.AFTER_DRIVE);
                    }
                return false;
            case STOP_TWO:
            case END:
            default:
                Hardware.drive.stop();
                return true;
            } // switch
    } // end sw3_driveOnChargingStation()

    private static void updateDashboard()
    {
        // GYRO
        Dashboard.updateGyroInd();

        // GEARS
        if (sw3_driveOnChargingStationState == SW3_DRIVE_ON_CHARGING_STATION_STATE.DRIVE_ONE_DRIVE)
            {
            Dashboard.updateDriveGearInd(DriveGear.Reverse);
            }
        else
            {
            Dashboard.updateDriveGearInd(DriveGear.Drive1);
            }

        // AUTO
        // System.out.println("Auto Path = " + autoPath);
        // System.out.println("Auto Dash = " + AUTO_MODE_DASH);
        switch (AUTO_MODE_DASH)
            {
            case Mode2:
                if (Hardware.leftRightNoneSwitch
                        .getPosition() == Relay.Value.kReverse)
                    {
                    Dashboard.updateAutoModeInd(AUTO_MODE_DASH, "Left Turn");
                    }
                else
                    if (Hardware.leftRightNoneSwitch
                            .getPosition() == Relay.Value.kForward)
                        {
                        Dashboard.updateAutoModeInd(AUTO_MODE_DASH,
                                "Right Turn");
                        }
                    else
                        {
                        Dashboard.updateAutoModeInd(AUTO_MODE_DASH,
                                "No Turn/Stop");
                        }
                break;

            default:
                Dashboard.updateAutoModeInd(AUTO_MODE_DASH);
                break;
            }

        // UTILS
        Dashboard.updateEBrakeEngagedInd(Hardware.eBrakePiston.getForward());
        Dashboard.updateReplaceBatteryInd();
        Dashboard.updateClawClosedInd(Hardware.clawPiston.getForward());
    }

    private static enum AUTO_PATH
        {
        SW1_DRIVE_ONLY_FORWARD, SW2_DRIVE_TURN_DRIVE, SW3_DRIVE_OVER_CHARGING_STATION, DISABLE;
        }

    private static enum SW1_DRIVE_ONLY_FORWARD_STATE
        {
        INIT, DELAY, DRIVE_ONE_DRIVE, STOP, END;
        }

    private static enum SW2_DRIVE_TURN_DRIVE_STATE
        {
        INIT, DELAY, DRIVE_ONE_DRIVE, STOP_ONE, DECIDE_NEXT, TURN, STOP_TURN, DRIVE_TWO_DRIVE, STOP_TWO, END;
        }

    private static enum SW3_DRIVE_ON_CHARGING_STATION_STATE
        {
        INIT, DELAY, DRIVE_ONE_DRIVE, STOP_ONE, DRIVE_TWO_DRIVE, STOP_TWO, END;
        }

    private static AUTO_PATH autoPath;

    private static SW1_DRIVE_ONLY_FORWARD_STATE sw1_driveOnlyForwardState;

    private static SW2_DRIVE_TURN_DRIVE_STATE sw2_driveTurnDriveState;

    private static SW3_DRIVE_ON_CHARGING_STATION_STATE sw3_driveOnChargingStationState;

    private static double delayTime = 0.0;

    // ==============================================================
    // Constants
    // ==============================================================

    private static final double MAX_DELAY_SECONDS_CURRENT_YEAR = 5.0;

    private static final double AUTO_GEAR = 1;

    private static final double MAX_ACCEL_TIME = 0.1;

    private static final double LEFT_ACCEL_SPEED = -0.15;

    private static final double RIGHT_ACCEL_SPEED = -0.15;

    private static final double DRIVE_ONE_DRIVE_SPEED = -0.25;

    private static final double SW1_DRIVE_ONLY_INCHES = 120.0;

    private static final double SW2_FIRST_STOP_DISTANCE = 44.0;

    private static AutoModeDash AUTO_MODE_DASH = AutoModeDash.Disabled;

    private static final double SW2_SECOND_STOP_DISTANCE = 50.0;

    private static final double SW3_DRIVE_OVER_CHARGING_STATION = 146.0;

    private static final double SW3_DRIVE_TOWARDS_CHARGING_STATION = 46.0;

    private static final double SW3_DRIVE_ON_CHARGING_STATION = 52.0;

    private static final double SW3_DRIVE_ONE_DRIVE_SPEED = 0.2;

    private static final double SW3_DRIVE_TWO_DRIVE_SPEED = -0.2;

    } // end Autonomous