package frc.Utils;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Dashboard Class
 * 
 * houses the main functions needed to update parts of the dashboard
 * 
 * @author Jacob Fisher
 * @written March 11, 2023
 */
public class Dashboard
    {
    public static enum DriveGear
        {
        Drive3, Drive2, Drive1, Reverse
        }

    public static enum AutoModeDash
        {
        Init, Teleop, Disabled, Completed, Mode1, Mode2, Mode3, Mode4, Mode5, Mode6
        }

    /**
     * Initialize dashboard values/reset
     */
    public static void init()
    {
        // GYRO
        updateGyroInd();

        // GEARS
        updateDriveGearInd(DriveGear.Drive1);

        // AUTO
        updateAutoModeInd(AutoModeDash.Init);

        // UTILS
        updateEBrakeEngagedInd(false);
        updateReplaceBatteryInd();
        updateClawClosedInd(false);
    }

    /**
     * Updates the gyro value
     * 
     * UPDATE: CURRENTLY GYRO DOESNT WORK SO IT WILL RETURN 0
     */
    public static double updateGyroInd()
    {
        // double gyroAngle = Hardware.gyro.getAngle();
        double gyroAngle = 0.0;

        SmartDashboard.putNumber("Gyro", gyroAngle);

        return gyroAngle;
    }

    /**
     * Update drive gear on dashboard (D3, D2, D1, R)
     * 
     * @param gear
     */
    public static void updateDriveGearInd(DriveGear gear)
    {
        switch (gear)
            {
            case Drive3:
                SmartDashboard.putBoolean("Gear3", true);
                SmartDashboard.putBoolean("Gear2", false);
                SmartDashboard.putBoolean("Gear1", false);
                SmartDashboard.putBoolean("GearR", false);
                break;

            case Drive2:
                SmartDashboard.putBoolean("Gear3", false);
                SmartDashboard.putBoolean("Gear2", true);
                SmartDashboard.putBoolean("Gear1", false);
                SmartDashboard.putBoolean("GearR", false);
                break;

            case Drive1:
                SmartDashboard.putBoolean("Gear3", false);
                SmartDashboard.putBoolean("Gear2", false);
                SmartDashboard.putBoolean("Gear1", true);
                SmartDashboard.putBoolean("GearR", false);
                break;

            case Reverse:
                SmartDashboard.putBoolean("Gear3", false);
                SmartDashboard.putBoolean("Gear2", false);
                SmartDashboard.putBoolean("Gear1", false);
                SmartDashboard.putBoolean("GearR", true);
                break;
            }
    }

    /**
     * Update auto mode/path for the dashboard
     * 
     * @param dashMode
     * @param leftRightNoneSetting
     */
    public static void updateAutoModeInd(AutoModeDash dashMode)
    {
        internalUpdateAutoModeInd(dashMode, "");
    }

    /**
     * Update auto mode/path for the dashboard
     *
     * @note Use this if the auto mode uses an Left/Right/None switch
     * 
     * @param dashMode
     * @param leftRightNoneSetting
     */
    public static void updateAutoModeInd(AutoModeDash dashMode,
            String leftRightNoneSetting)
    {
        internalUpdateAutoModeInd(dashMode, leftRightNoneSetting);
    }

    /**
     * Update auto mode/path for the dashboard
     * 
     * @param dashMode
     */
    private static void internalUpdateAutoModeInd(AutoModeDash dashMode,
            String leftRightNoneSetting)
    {
        switch (dashMode)
            {
            case Init:
                SmartDashboard.putString("AutoMode", "");

                SmartDashboard.putBoolean("AutoMode0", true);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", false);
                break;

            case Teleop:
                SmartDashboard.putString("AutoMode", "N/A (Teleop Enabled)");

                SmartDashboard.putBoolean("AutoMode0", true);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", false);
                break;

            case Disabled:
                SmartDashboard.putString("AutoMode",
                        "N/A (Auto Disable Switch = true)");

                SmartDashboard.putBoolean("AutoMode0", true);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", false);
                break;

            case Completed:
                SmartDashboard.putString("AutoMode",
                        "N/A (Autonomous Completed)");

                SmartDashboard.putBoolean("AutoMode0", true);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", false);

            case Mode1:
                SmartDashboard.putString("AutoMode", "Drive Forward ONLY");

                SmartDashboard.putBoolean("AutoMode0", false);
                SmartDashboard.putBoolean("AutoMode1", true);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", false);
                break;

            case Mode2:
                SmartDashboard.putString("AutoMode", "Drive - Turn - Drive");

                SmartDashboard.putBoolean("AutoMode0", false);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", true);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", false);
                break;

            case Mode3:
                SmartDashboard.putString("AutoMode",
                        "Drive - Charging Station");

                SmartDashboard.putBoolean("AutoMode0", false);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", true);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", false);
                break;

            case Mode4:
                SmartDashboard.putString("AutoMode",
                        "WARNING: This mode is not set to do anything");

                SmartDashboard.putBoolean("AutoMode0", false);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", true);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", false);
                break;

            case Mode5:
                SmartDashboard.putString("AutoMode",
                        "WARNING: This mode is not set to do anything");

                SmartDashboard.putBoolean("AutoMode0", false);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", true);
                SmartDashboard.putBoolean("AutoMode6", false);
                break;

            case Mode6:
                SmartDashboard.putString("AutoMode",
                        "WARNING: This mode is not set to do anything");

                SmartDashboard.putBoolean("AutoMode0", false);
                SmartDashboard.putBoolean("AutoMode1", false);
                SmartDashboard.putBoolean("AutoMode2", false);
                SmartDashboard.putBoolean("AutoMode3", false);
                SmartDashboard.putBoolean("AutoMode4", false);
                SmartDashboard.putBoolean("AutoMode5", false);
                SmartDashboard.putBoolean("AutoMode6", true);
                break;
            }
    }

    /**
     * Updates the indicator if the E brake is engaged for the dashboard
     * 
     * @param value
     */
    public static void updateEBrakeEngagedInd(boolean value)
    {
        SmartDashboard.putBoolean("EBrakeEngaged", value);
    }

    /**
     * Updates the indicator if the battery should be replaced (recommendation)
     * for the dashboard
     * 
     * @return
     */
    public static double updateReplaceBatteryInd()
    {
        double batteryLevel = RobotController.getBatteryVoltage();

        if (batteryLevel < 11.5)
            {
            SmartDashboard.putBoolean("ReplaceBattery", true);
            }
        else
            {
            SmartDashboard.putBoolean("ReplaceBattery", false);
            }

        return batteryLevel;
    }

    /**
     * Updates the indicator if the claw is closed
     * 
     * @param value
     */
    public static void updateClawClosedInd(boolean value)
    {
        SmartDashboard.putBoolean("ClawClosed", value);
    }
    }
