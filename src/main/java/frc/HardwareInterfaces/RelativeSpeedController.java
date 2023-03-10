// ====================================================================
// FILE NAME: RelativeSpeedController.java (Team 339 - Kilroy)
//
// CREATED ON: Jan 15, 2011
// MODIFIED ON:
// MODIFIED BY:
// ABSTRACT:
// This class scales a dimensionless fraction (usually between -1.0
// and 1.0) to an absolute velocity (usually between zero and some
// maximum speed). For example, use this to control a CAN Jaguar
// in velocity mode with a joystick.
//
// NOTE: Please do not release this code without permission from
// Team 339.
// ====================================================================

package frc.HardwareInterfaces;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

// -------------------------------------------------------
/**
 * This class scales a dimensionless fraction (usually between -1.0 and 1.0) to
 * an absolute velocity (usually between zero and some maximum speed). For
 * example, use this to control a CAN Jaguar in velocity mode with a joystick.
 *
 * @class RelativeSpeedController
 * @author Josh Shields
 * @written Jan 15, 2011 -------------------------------------------------------
 */
public class RelativeSpeedController implements MotorController
    {
    /**
     * -------------------------------------------------------
     *
     * @description The speed controller to send velocity commands to
     * @author Josh Shields
     * @written Jan 15, 2011 -------------------------------------------------------
     */
    private final MotorController speedController;

    /**
     * -------------------------------------------------------
     *
     * @description The maximum speed that a command of 1.0 should represent
     * @author Josh Shields
     * @written Jan 15, 2011 -------------------------------------------------------
     */
    private final double maxSpeed;

    /**
     * @description Whether or not this motor controller is inverted.
     * @author Noah Golmant
     * @written 10 Jan 2016
     */
    private boolean isInverted = false;

    // -------------------------------------------------------
    /**
     * constructor
     *
     * @method RelativeSpeedController
     * @param speedController
     *            The speed controller to control with velocity commands
     * @param maxSpeed
     *            The maximum speed that a command of 1.0 should represent
     * @author Josh Shields
     * @written Jan 15, 2011 -------------------------------------------------------
     */
    public RelativeSpeedController(final MotorController speedController, // The
            // speed
            // controller
            // to
            // control
            // with
            // velocity
            // commands
            final double maxSpeed) // The maximum speed that a command of 1.0
                                   // should represent
        {
            this.speedController = speedController;
            this.maxSpeed = maxSpeed;
        } // end constructor

    // -------------------------------------------------------
    /**
     * Disables the speed controller
     *
     * @method disable
     * @author Josh Shields
     * @written Jan 15, 2011 -------------------------------------------------------
     */
    @Override
    public void disable()
    {
        this.speedController.disable();
    } // end disable()

    // -------------------------------------------------------
    /**
     * returns the latest velocity to the caller
     *
     * @method get
     * @return The last velocity sent to the speed controller, as a fraction of the
     *         maximum speed (-1.0 to 1.0)
     * @author Josh Shields
     * @written Jan 15, 2011 -------------------------------------------------------
     */
    @Override
    public double get()
    {
        return this.speedController.get() / this.maxSpeed;
    } // end get()

    // -------------------------------------------------------
    /**
     * sets tbe speed of the controller as a fraction of the max speed (-1.0 to
     * 1.0). This method is called based on the PID loops calculations
     *
     * @method pidWrite
     * @param output
     *            The speed to send to the speed controller as a fraction of the
     *            maximum speed (-1.0 to 1.0)
     * @author Josh Shields
     * @written Jan 15, 2011 -------------------------------------------------------
     */
    // TODO ---------------McGee fix below function------------------
    // @Override
    // public void pidWrite(final double output)
    // {
    // this.set(output);
    // } // end set
    // TODO ---------------McGee fix--------------------------------

    // -------------------------------------------------------
    /**
     * sets tbe speed of the controller as a fraction of the max speed (-1.0 to 1.0)
     *
     * @method set
     * @param speed
     *            The speed to send to the speed controller as a fraction of the
     *            maximum speed (-1.0 to 1.0)
     * @author Josh Shields
     * @written Jan 15, 2011 -------------------------------------------------------
     */
    @Override
    public void set(final double speed)
    {
        this.speedController.set(speed * this.maxSpeed);
    } // end set()

    /**
     * @description Gets whether or not this speed controller's motor is inverted.
     * @author Noah Golmant
     * @written 10 Jan 2016
     */
    @Override
    public boolean getInverted()
    {
        return this.isInverted;
    } // end getInverted()

    /**
     * @description Sets whether or not this speed controller's motor is inverted.
     * @author Noah Golmant
     * @written 10 Jan 2016
     */
    @Override
    public void setInverted(boolean val)
    {
        this.isInverted = val;
    } // end setInverted()

    // @Override
    public void stopMotor()
    {
        // TODO Auto-generated method stub

    } // end stopMotor()

    } // end class
