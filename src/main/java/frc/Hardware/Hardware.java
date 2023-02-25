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

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.playingwithfusion.CANVenom;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsBase;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.HardwareInterfaces.Transmission.LeftRightTransmission;
import frc.HardwareInterfaces.Transmission.TransmissionBase;
import frc.HardwareInterfaces.Transmission.TransmissionBase.TransmissionType;
import frc.Utils.drive.Drive;
import frc.HardwareInterfaces.SingleThrowSwitch;
import frc.HardwareInterfaces.SixPositionSwitch;
import frc.HardwareInterfaces.DoubleSolenoid;
import frc.HardwareInterfaces.DoubleThrowSwitch;
import frc.HardwareInterfaces.KilroyEncoder;
import frc.HardwareInterfaces.KilroyUSBCamera;
import frc.HardwareInterfaces.LightSensor;
import frc.HardwareInterfaces.MomentarySwitch;
import frc.HardwareInterfaces.Potentiometer;

/**
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
                        disableAutoSwitch = new SingleThrowSwitch(10);
                        sixPosSwitch = new SixPositionSwitch(13, 14, 15, 16, 17,
                                        18);
                        redLightSensor = new LightSensor(
                                        CURRENT_REDLIGHTSENSOR_PORT);
                        leftRightNoneSwitch = new DoubleThrowSwitch(11, 12);
                        // ============ANALOG INIT============

                        // ==============CAN INIT=============
                        // Motor Controllers
                        leftBottomMotor = new WPI_TalonFX(8);
                        leftTopMotor = new WPI_TalonFX(9);

                        leftBottomMotor.setInverted(true);
                        leftTopMotor.setInverted(true);

                        rightBottomMotor = new WPI_TalonFX(16);
                        rightTopMotor = new WPI_TalonFX(19);

                        rightBottomMotor.setInverted(false);
                        rightTopMotor.setInverted(false);

                        rightBottomEncoder = new KilroyEncoder(
                                        (WPI_TalonFX) rightBottomMotor);
                        rightBottomEncoder.setDistancePerPulse(
                                        CURRENT_DISTANCE_PER_PULSE);
                        rightBottomEncoder.reset();
                        rightBottomEncoder.setReverseDirection(false);

                        leftBottomEncoder = new KilroyEncoder(
                                        (WPI_TalonFX) leftBottomMotor);
                        leftBottomEncoder.setDistancePerPulse(
                                        CURRENT_DISTANCE_PER_PULSE);
                        leftBottomEncoder.reset();
                        leftBottomEncoder.setReverseDirection(false);

                        leftSideMotors = new MotorControllerGroup(
                                        leftBottomMotor, leftTopMotor);
                        rightSideMotors = new MotorControllerGroup(
                                        rightBottomMotor, rightTopMotor);

                        armLengthMotor = new WPI_VictorSPX(23);
                        armRaiseMotor = new CANVenom(21);

                        armLengthMotor.setInverted(false);
                        armRaiseMotor.setInverted(false);

                        // Encoders
                        // ==============RIO INIT==============

                        // =============OTHER INIT============

                        transmission = new LeftRightTransmission(leftSideMotors,
                                        rightSideMotors);
                        transmission.setJoystickDeadband(CURRENT_DEADBAND);
                        transmission.setAllGearPercentages(
                                        CURRENT_GEAR1_MAX_SPEED,
                                        CURRENT_GEAR2_MAX_SPEED,
                                        CURRENT_GEAR3_MAX_SPEED);
                        drive = new Drive(transmission, leftBottomEncoder,
                                        rightBottomEncoder, null);

                        eBrakeTimer = new Timer();

                        autoTimer = new Timer();

                        delayPot = new Potentiometer(CURRENT_DELAY_POT_PORT);

                        armControlHoldSpeed = CURRENT_ARM_CONTROL_HOLD_SPEED;
                        armLengthHoldSpeed = CURRENT_ARM_LENGTH_HOLD_SPEED;
                        armControlDeadband = CURRENT_ARM_CONTROL_DEADBAND;
                        armLengthDeadband = CURRENT_ARM_LENGTH_DEADBAND;

                        clawPiston = new DoubleSolenoid(CURRENT_CLAW_FWD_PORT,
                                        CURRENT_CLAW_REV_PORT);
                        eBrake = new DoubleSolenoid(CURRENT_EBRAKE_FWD_PORT,
                                        CURRENT_EBRAKE_REV_PORT);
                        armRaisePiston = new DoubleSolenoid(
                                        CURRENT_ARM_RAISE_FWD_PORT,
                                        CURRENT_ARM_RAISE_REV_PORT);
                        clawPiston.setForward(true);
                        eBrake.setForward(true);
                        armRaisePiston.setForward(true);

                        eBrakeDelayTime = CURRENT_EBRAKETIMER_DELAY;
                        eBrakeDeadband = CURRENT_DEADBAND;
                        } // end of current year

                if (robotIdentity == Identifier.PrevYear)
                        {
                        // ==============DIO INIT=============
                        sixPosSwitch = new SixPositionSwitch(13, 14, 15, 16, 17,
                                        18);
                        disableAutoSwitch = new SingleThrowSwitch(10);
                        disableAutoSwitch.setInverted(false);

                        redLightSensor = new LightSensor(
                                        PREV_REDLIGHTSENSOR_PORT);
                        leftRightNoneSwitch = new DoubleThrowSwitch(11, 12);

                        // ============ANALOG INIT============

                        // ==============CAN INIT=============
                        leftBottomMotor = new WPI_TalonFX(8);
                        leftTopMotor = new WPI_TalonFX(9);

                        leftBottomMotor.setInverted(true);
                        leftTopMotor.setInverted(true);

                        rightBottomMotor = new WPI_TalonFX(16);
                        rightTopMotor = new WPI_TalonFX(19);

                        rightBottomMotor.setInverted(false);
                        rightTopMotor.setInverted(false);

                        rightBottomEncoder = new KilroyEncoder(
                                        (WPI_TalonFX) rightBottomMotor);
                        rightBottomEncoder.setDistancePerPulse(
                                        PREV_DISTANCE_PER_PULSE);
                        rightBottomEncoder.reset();
                        rightBottomEncoder.setReverseDirection(true);

                        leftBottomEncoder = new KilroyEncoder(
                                        (WPI_TalonFX) leftBottomMotor);
                        leftBottomEncoder.setDistancePerPulse(
                                        PREV_DISTANCE_PER_PULSE);
                        leftBottomEncoder.reset();
                        leftBottomEncoder.setReverseDirection(true);

                        leftSideMotors = new MotorControllerGroup(
                                        leftBottomMotor, leftTopMotor);

                        rightSideMotors = new MotorControllerGroup(
                                        rightBottomMotor, rightTopMotor);

                        armLengthMotor = new WPI_VictorSPX(26);
                        armRaiseMotor = new WPI_TalonFX(18);

                        armLengthMotor.setInverted(true);
                        armRaiseMotor.setInverted(true);
                        // ==============RIO INIT=============

                        // =============OTHER INIT============
                        transmission = new LeftRightTransmission(leftSideMotors,
                                        rightSideMotors);
                        transmission.setJoystickDeadband(PREV_DEADBAND);
                        transmission.setAllGearPercentages(PREV_GEAR1_MAX_SPEED,
                                        PREV_GEAR2_MAX_SPEED,
                                        PREV_GEAR3_MAX_SPEED);

                        drive = new Drive(transmission, leftBottomEncoder,
                                        rightBottomEncoder, null);

                        eBrakeTimer = new Timer();

                        autoTimer = new Timer();

                        delayPot = new Potentiometer(PREV_DELAY_POT_PORT);

                        clawPiston = new DoubleSolenoid(PREV_CLAW_FWD_PORT,
                                        PREV_CLAW_REV_PORT);
                        eBrake = new DoubleSolenoid(PREV_EBRAKE_FWD_PORT,
                                        PREV_EBRAKE_REV_PORT);
                        armRaisePiston = new DoubleSolenoid(
                                        PREV_ARM_RAISE_FWD_PORT,
                                        PREV_ARM_RAISE_REV_PORT);
                        clawPiston.setForward(true);
                        eBrake.setForward(false);
                        armRaisePiston.setForward(true);

                        armControlHoldSpeed = PREV_ARM_CONTROL_HOLD_SPEED;
                        armLengthHoldSpeed = PREV_ARM_LENGTH_HOLD_SPEED;
                        armControlDeadband = PREV_ARM_CONTROL_DEADBAND;
                        armLengthDeadband = PREV_ARM_LENGTH_DEADBAND;

                        eBrakeDelayTime = PREV_EBRAKETIMER_DELAY;
                        eBrakeDeadband = PREV_DEADBAND;
                        } // end of previous year
                // ---------------------------------
                // required for both years
                // ----------------------------------
                eBrakeTimer.stop();
                eBrakeTimer.reset();
        } // end initialize()

        // CAN DEVICES
        // **********************************************************
        public static MotorController leftBottomMotor = null;
        public static MotorController leftTopMotor = null;
        public static MotorController rightBottomMotor = null;
        public static MotorController rightTopMotor = null;

        public static KilroyEncoder rightBottomEncoder = null;
        public static KilroyEncoder leftBottomEncoder = null;
        public static MotorController armLengthMotor = null;
        public static MotorController armRaiseMotor = null;
        // **********************************************************
        // DIGITAL I/O
        // **********************************************************
        public static SixPositionSwitch sixPosSwitch = null;
        public static SingleThrowSwitch disableAutoSwitch = null;
        public static DoubleThrowSwitch leftRightNoneSwitch = null;
        public static LightSensor redLightSensor = null;
        // **********************************************************
        // ANALOG I/O
        // **********************************************************
        public static Potentiometer delayPot = null;
        public static Potentiometer tenTurnPot = null;

        // **********************************************************
        // DRIVER STATION CLASSES
        // **********************************************************

        public static Joystick leftDriver = new Joystick(0);
        public static Joystick rightDriver = new Joystick(1);
        public static Joystick leftOperator = new Joystick(2);
        public static Joystick rightOperator = new Joystick(3);

        // **********************************************************
        // PNEUMATIC DEVICES
        // **********************************************************
        public static Compressor compressor = new Compressor(
                        PneumaticsModuleType.CTREPCM);
        public static DoubleSolenoid eBrake = null;
        public static DoubleSolenoid clawPiston = null;
        public static DoubleSolenoid armRaisePiston = null;

        public static MomentarySwitch clawTriggerButton = new MomentarySwitch(
                        rightOperator, 1, false);
        public static MomentarySwitch armRaiseButton = new MomentarySwitch(
                        leftOperator, 4, false);

        // **********************************************************
        // roboRIO CONNECTIONS CLASSES
        // **********************************************************

        public static PowerDistribution pdp = new PowerDistribution();

        // **********************************************************
        // Kilroy's Ancillary classes
        // **********************************************************

        // ------------------------------------
        // Utility classes
        // ------------------------------------
        public static Timer eBrakeTimer = null;
        public static Boolean eBrakeTimerIsStopped = true;
        public static Timer autoTimer = null;
        public static MomentarySwitch eBrakeMomentarySwitch1 = new MomentarySwitch(
                        rightDriver, 5, false);
        public static MomentarySwitch eBrakeMomentarySwitch2 = new MomentarySwitch(
                        leftDriver, 6, false);

        // ------------------------------------
        // Drive system
        // ------------------------------------
        public static MotorControllerGroup leftSideMotors = null;
        public static MotorControllerGroup rightSideMotors = null;

        public static LeftRightTransmission transmission = null;
        public static Drive drive = null;

        // ------------------------------------------
        // Vision stuff
        // ----------------------------
        public static KilroyUSBCamera cameras = new KilroyUSBCamera(true);
        public static MomentarySwitch switchCameraViewButton10 = new MomentarySwitch(
                        rightOperator, 10, false);
        // public static MomentarySwitch switchCameraViewButton11 = new
        // MomentarySwitch(
        // rightOperator, 11, false);

        // -------------------
        // Subassemblies
        // -------------------
        // arm control variables
        public static double armControlHoldSpeed;
        public static double armLengthHoldSpeed;
        public static double armControlDeadband;
        public static double armLengthDeadband;
        public static double eBrakeDelayTime;
        public static double eBrakeDeadband;
        // --------------------
        // Previous year's constants
        // --------------------
        public final static double PREV_DEADBAND = 0.2;
        public final static double PREV_ARM_CONTROL_HOLD_SPEED = 0.1;
        public final static double PREV_ARM_LENGTH_HOLD_SPEED = 0.0;
        public final static double PREV_ARM_CONTROL_DEADBAND = 0.2;
        public final static double PREV_ARM_LENGTH_DEADBAND = 0.2;
        private final static double PREV_EBRAKETIMER_DELAY = 5.0;
        private final static double PREV_GEAR1_MAX_SPEED = 0.3;
        private final static double PREV_GEAR2_MAX_SPEED = 0.5;
        private final static double PREV_GEAR3_MAX_SPEED = 0.7;
        private final static int PREV_DELAY_POT_PORT = 2;
        private final static double PREV_DISTANCE_PER_PULSE = 0.000760062738772;
        private final static int PREV_EBRAKE_FWD_PORT = 4;
        private final static int PREV_EBRAKE_REV_PORT = 5;
        private final static int PREV_ARM_RAISE_FWD_PORT = 0;
        private final static int PREV_ARM_RAISE_REV_PORT = 1;
        private final static int PREV_CLAW_FWD_PORT = 6;
        private final static int PREV_CLAW_REV_PORT = 7;
        private final static int PREV_REDLIGHTSENSOR_PORT = 7;
        // --------------------
        // Current year's constants
        // --------------------
        public final static double CURRENT_DEADBAND = 0.2;
        public final static double CURRENT_ARM_CONTROL_HOLD_SPEED = 0.0;
        public final static double CURRENT_ARM_LENGTH_HOLD_SPEED = 0.0;
        public final static double CURRENT_ARM_CONTROL_DEADBAND = 0.2;
        public final static double CURRENT_ARM_LENGTH_DEADBAND = 0.2;
        public final static double CURRENT_EBRAKETIMER_DELAY = 1.5;
        private final static double CURRENT_GEAR1_MAX_SPEED = 0.25;
        private final static double CURRENT_GEAR2_MAX_SPEED = 0.5;
        private final static double CURRENT_GEAR3_MAX_SPEED = 0.7;
        private final static int CURRENT_DELAY_POT_PORT = 1;
        private final static double CURRENT_DISTANCE_PER_PULSE = 0.01;
        private final static int CURRENT_EBRAKE_FWD_PORT = 4;
        private final static int CURRENT_EBRAKE_REV_PORT = 5;
        private final static int CURRENT_ARM_RAISE_FWD_PORT = 0;
        private final static int CURRENT_ARM_RAISE_REV_PORT = 1;
        private final static int CURRENT_CLAW_FWD_PORT = 6;
        private final static int CURRENT_CLAW_REV_PORT = 7;
        private final static int CURRENT_REDLIGHTSENSOR_PORT = 7;

        } // end class
