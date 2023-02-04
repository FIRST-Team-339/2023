/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.HardwareInterfaces;

import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoProperty;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;;

/**
 * Class for controlling the USB cameras on the robot
 *
 * @Author Alice Marchant
 * @Written Feb 15th, 2020
 */
public class KilroyUSBCamera
    {

    /**
     * constructor
     *
     * @param twoFeeds
     *            - states whether we are using two camera feeds true = 2 USB
     *            cameras false = 1 USB cameras
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public KilroyUSBCamera(boolean twoFeeds)
        {
            // If there are two feeds, declare and set values for two cameras.
            if (twoFeeds == true)
                {
                this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
                this.cam1 = CameraServer.startAutomaticCapture("usb1", 1);
                this.server = CameraServer.getServer("serve_usb0");
                this.server1 = CameraServer.getServer("serve_usb1");
                setCameraValues(2);
                this.server1.getProperty("compression").set(COMPRESSION);
                }
            // If two feeds is false, only declare and set values for one camera
            else
                {
                this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
                this.server = CameraServer.getServer("serve_usb0");
                setCameraValues(1);
                }
        } // end constructor - overloaded

    /**
     * constructor
     *
     * @param twoFeeds
     *            - states whether we are using two camera feeds true = 2 USB
     *            cameras false = 1 USB cameras
     * @param width
     *            - the width
     * @param height
     *            - the height
     * @param FPS
     *            - the fps of the cameras
     * @param compression
     *            - the compression rate
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public KilroyUSBCamera(boolean twoFeeds, int width, int height, int FPS, int compression)
        {
            // If there are two feeds, declare and set values for two cameras.
            if (twoFeeds == true)
                {
                this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
                this.cam1 = CameraServer.startAutomaticCapture("usb1", 1);
                this.server = CameraServer.getServer("serve_usb0");
                this.server1 = CameraServer.getServer("serve_usb1");
                setCameraValues(width, height, FPS, compression, 2);
                this.server1.getProperty("compression").set(compression);
                }
            // If two feeds is false, only declare and set values for one camera
            else
                {
                this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
                this.server = CameraServer.getServer("serve_usb0");
                setCameraValues(width, height, FPS, compression, 1);
                }
        } // end constructor - overloaded

    /**
     * constructor
     *
     * @param button
     *            button used to switch views
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public KilroyUSBCamera(MomentarySwitch button)
        {
            // Declares and sets values for two cameras
            this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
            this.cam1 = CameraServer.startAutomaticCapture("usb1", 1);
            CameraServer.removeServer("serve_usb1");
            this.server = CameraServer.getServer("serve_usb0");
            setCameraValues(2);
            this.button = button;
        } // end constructor - overloaded

    /**
     * constructor
     *
     * @param button
     *            button used to switch views
     * @param width
     *            - the width
     * @param height
     *            - the height
     * @param FPS
     *            - the fps of the cameras
     * @param compression
     *            - the compression rate
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public KilroyUSBCamera(MomentarySwitch button, int width, int height, int FPS, int compression)
        {
            // Declares and sets values for two cameras
            this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
            this.cam1 = CameraServer.startAutomaticCapture("usb1", 1);

            CameraServer.removeServer("serve_usb1");
            this.server = CameraServer.getServer("serve_usb0");
            setCameraValues(width, height, FPS, compression, 2);
            this.button = button;
        } // end constructor - overloaded

    /**
     * constructor
     *
     * @param switch1
     *            - button button used to switch USB camera 2
     * @param switch2
     *            - button button used to switch USB camera 1
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public KilroyUSBCamera(MomentarySwitch switch1, MomentarySwitch switch2)
        {
            // Declares and sets values for two cameras
            this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
            this.cam1 = CameraServer.startAutomaticCapture("usb1", 1);
            CameraServer.removeServer("serve_usb1");
            this.server = CameraServer.getServer("serve_usb0");
            setCameraValues(2);
            this.switch1 = switch1;
            this.switch2 = switch2;
        } // end constructor - overloaded

    /**
     * constructor
     *
     * @param switch1
     *            - button button used to switch USB camera 2
     * @param switch2
     *            - button button used to switch USB camera 1
     * @param width
     *            - the width
     * @param height
     *            - the height
     * @param FPS
     *            - the fps of the cameras
     * @param compression
     *            - the compression rate
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public KilroyUSBCamera(MomentarySwitch switch1, MomentarySwitch switch2, int width, int height, int FPS,
            int compression)
        {
            // Declares and sets values for two cameras
            this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
            this.cam1 = CameraServer.startAutomaticCapture("usb1", 1);
            CameraServer.removeServer("serve_usb1");
            this.server = CameraServer.getServer("serve_usb0");
            setCameraValues(width, height, FPS, compression, 2);
            this.switch1 = switch1;
            this.switch2 = switch2;
        } // end constructor - overloaded

    /**
     * constructor
     *
     * @param button1
     *            button used to switch to view 2
     * @param button2
     *            button used to switch to view 1
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public KilroyUSBCamera(JoystickButton button1, JoystickButton button2)
        {
            // Declares and sets values for two cameras
            this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
            this.cam1 = CameraServer.startAutomaticCapture("usb1", 1);

            CameraServer.removeServer("serve_usb1");
            this.server = CameraServer.getServer("serve_usb0");
            setCameraValues(2);
            this.button1 = button1;
            this.button2 = button2;
        } // end constructor - overloaded

    /**
     * constructor
     *
     * @param button1
     *            button used to switch to view 2
     * @param button2
     *            button used to switch to view 1
     * @param width
     *            - the width
     * @param height
     *            - the height
     * @param FPS
     *            - the fps of the cameras
     * @param compression
     *            - the compression rate
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public KilroyUSBCamera(JoystickButton button1, JoystickButton button2, int width, int height, int FPS,
            int compression)
        {
            // Declares and sets values for two cameras
            this.cam0 = CameraServer.startAutomaticCapture("usb0", 0);
            this.cam1 = CameraServer.startAutomaticCapture("usb1", 1);
            CameraServer.removeServer("serve_usb1");
            this.server = CameraServer.getServer("serve_usb0");
            setCameraValues(width, height, FPS, compression, 2);
            this.button1 = button1;
            this.button2 = button2;
        } // end constructor - overloaded

    /**
     * Gets the compression rate of the server
     *
     * @return the compression rate
     * 
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public int getCompression()
    {
        return this.server.getProperty("compression").get();
    } // end getCompression()

    /**
     * Gets the fps of a given camera
     *
     * @param cameraNum
     *            - which camera the method gets the value for
     * @return the fps of the camera
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public double getFPS(int cameraNum)
    {
        // If camNum is zero, get the fps of cam0
        if (cameraNum == 0)
            {
            return this.cam0.getActualFPS();
            } // end if
        // If camNum is one, get the fps of cam1
        else if (cameraNum == 1)
            {
            return this.cam1.getActualFPS();
            } // end else if
        // If neither, return 0
        else
            {
            return 0.0;
            } // end else
    } // end getFPS()

    /**
     * Sets a given camera to be diplayed to the driver's station
     *
     * @param cameraNum
     *            - which camera to set the source to
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public void setCamera(int cameraNum)
    {
        // If the int passed in is 0, switch source to cam0. If int passed in is 1,
        // switch source to cam1
        if (cameraNum == 0)
            {
            this.server.setSource(this.cam0);
            } // end if
        if (cameraNum == 1)
            {
            this.server.setSource(this.cam1);
            } // end if
    } // end setCamera()

    /**
     * Sets the values for fps, resolution, and compression on the camera(s)
     *
     * @param numCameras
     *            - the amount of cameras to set the values for
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public void setCameraValues(int numCameras)
    {
        if (numCameras == 1)
            {
            this.cam0.setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
            this.cam0.setFPS(CAMERA_FPS);
            this.server.getProperty("compression").set(COMPRESSION);
            } // end if
        else if (numCameras == 2)
            {
            this.cam0.setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
            this.cam1.setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
            this.cam0.setFPS(CAMERA_FPS);
            this.cam1.setFPS(CAMERA_FPS);
            this.server.getProperty("compression").set(COMPRESSION);
            } // end else if
    } // end setCameraValues()

    /**
     * Passes in the values for fps, resolution, and compression on the camera(s)
     *
     * @param width
     *            - the width of the resolution
     * @param height
     *            - the height of the resolution
     * @param FPS
     *            - the fps of the camera(s)
     * @param compression
     *            - the compression rate of the server
     * @param numCameras
     *            - the amount of cameras to set the values for
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public void setCameraValues(int width, int height, int FPS, int compression, int numCameras)
    {
        if (numCameras == 1)
            {
            this.cam0.setResolution(width, height);
            this.cam0.setFPS(FPS);
            this.server.getProperty("compression").set(compression);
            } // end if
        else if (numCameras == 2)
            {
            this.cam0.setResolution(width, height);
            this.cam1.setResolution(width, height);
            this.cam0.setFPS(FPS);
            this.cam1.setFPS(FPS);
            this.server.getProperty("compression").set(compression);
            } // end else if
    } // end setCameraValues()

    /**
     * Toggles which camera is being displayed on the driver's station
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public void switchCameras()
    {
        // If cam1 is not null, switch cameras based on which is on at the time of
        // calling
        if (this.cam1 != null)
            {
            if (this.cam0.isEnabled() == true)
                {
                this.server.setSource(this.cam1);
                } // end if
            else
                {
                this.server.setSource(this.cam0);
                } // end else
            } // end if
    } // end switchCameras()

    /**
     * Toggles the cameras with a momentary switch
     *
     * @param button
     *            - button used to switch the cameras
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    public void switchCameras(MomentarySwitch button)
    {
        // Whenever the value of the momentary switch switches from false to true or
        // true to false, it calls the switchCameras() method
        if (button.isOnCheckNow() == true && this.firstCheck == true)
            {
            switchCameras();
            firstCheck = false;
            }
        if (button.isOnCheckNow() == false && this.firstCheck == false)
            {
            switchCameras();
            firstCheck = true;
            } // end if
    } // end switchCameras()

    /**
     * Use two buttons that each toggle the cameras
     *
     * @param switch1
     *            - button used to toggle the cameras
     * @param switch2
     *            - other button used to toggle the cameras\
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020 NOTE: This is untested and will need to be fixed
     *          before usage!!!!!!
     */
    public void switchCameras(MomentarySwitch switch1, MomentarySwitch switch2)
    {
        // Whenever the value of either momentary switch switches from false to true or
        // true to false, it calls the switchCameras() method
        // if (switch1.isOnCheckNow() == true && this.firstCheck == true &&
        // this.cam0.isEnabled() == true)
        if (switch1.isOnCheckNow() == true && this.cam0.isEnabled() == true)
            {
            switchCameras();
            this.firstCheck = false;
            } // end if
        // if (switch1.isOnCheckNow() == false && this.firstCheck == false)
            {
            // this.firstCheck = true;
            } // end if
        // if (switch2.isOnCheckNow() == true && this.firstCheck2 == true &&
        // this.cam1.isEnabled() == true)
        if (switch2.isOnCheckNow() == true && this.cam1.isEnabled() == true)
            {
            switchCameras();
            this.firstCheck2 = false;
            } // end if
        // if (switch2.isOnCheckNow() == false && this.firstCheck2 == false)
            {
            // this.firstCheck2 = true;
            } // end if
    } // end switchCameras()

    /**
     * button1 sets camera1 to be displayed to the driver's station, button2 sets
     * camera0 to be displayed to the driver's station
     *
     * @param button1
     *            - button used to switch the source to cam1
     * @param button2
     *            - button used to switch the source to cam0
     *
     * @Author Alice Marchant
     * @Written Feb 15th, 2020
     */
    //public void switchCameras(JoystickButton button1, JoystickButton button2)
    {
        // If button1 is pressed, switch source to cam1. If button2 is pressed, switch
        // source to cam0
       // if (this.cam1 != null)
            {
           // if (button1.get() == true && this.cam0.isEnabled() == true)
                {
               // this.server.setSource(this.cam1);
                } // end if
            //if (button2.get() == true && this.cam1.isEnabled() == true)
                {
                //this.server.setSource(this.cam0);
                } // end if
            } // end if
    } // end switchCameras()

    /**
     * Sets the limelight as the server source
     */
    public void setLimelight()
    {
        this.server.setSource(limelight);
        for (VideoProperty x : this.server.enumerateProperties())
            System.out.println("Name: " + x.getName() + "Val: " + x.get());
    }

    // Variables

    private UsbCamera cam0 = null;

    private UsbCamera cam1 = null;

    private HttpCamera limelight = new HttpCamera("Limelight Preveiw", "http://limelight.local:5800/");

    private VideoSink server;

    private VideoSink server1;

    private MomentarySwitch button = null;

    private MomentarySwitch switch1 = null;

    private MomentarySwitch switch2 = null;

    private JoystickButton button1 = null;

    private JoystickButton button2 = null;

    private boolean firstCheck = true;

    private boolean firstCheck2 = true;

    // Constants

    private int CAMERA_FPS = 20;

    private int COMPRESSION = 30;

    private int RESOLUTION_WIDTH = 340;

    private int RESOLUTION_HEIGHT = 240;
    // end class
    }
