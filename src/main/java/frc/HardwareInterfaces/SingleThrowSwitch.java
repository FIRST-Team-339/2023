// ====================================================================
// FILE NAME: SingleThrowSwitch.java (Team 339 - Kilroy)
//
// CREATED ON: Sep 19, 2009
// CREATED BY: Bob Brown
// MODIFIED ON:
// MODIFIED BY:
// ABSTRACT:
// This class Enhances the Digital Input class. It adds a member
// function isOn() which will return a true if the switch is on.
// All other member functions from the base class are unchanged.
//
// REMEMBER: This class includes all functions normally found in
// the WPI DigitalInput class.
//
// NOTE: Please do not release this code without permission from
// Team 339.
// ====================================================================

package frc.HardwareInterfaces;

import edu.wpi.first.wpilibj.DigitalInput;

// -------------------------------------------------------
/**
 * This class Enhances the Digital Input class. It adds a member function isOn()
 * which will return a true if the switch is on. All other member functions from
 * the base class are unchanged.
 *
 * @class SingleThrowSwitch
 * @author Bob Brown
 * @written Sep 19, 2009 -------------------------------------------------------
 */
public class SingleThrowSwitch extends DigitalInput
    {
    // -------------------------------------------------------
    /**
     * Create an instance of a Single Throw Switch class. Creates a digital input
     * given a channel and uses the default module.
     *
     * @method SingleThrowSwitch()
     * @param channel
     *            the port for the digital input
     * @author Bob Brown
     * @written Sep 18 2009 -------------------------------------------------------
     */
    public SingleThrowSwitch(final int channel)
        {
            super(channel);
        } // end constructor

    // ------------------------------------------------------
    /**
     * This function overrides the parent function get() but is marked as deprecated
     * so users will not call get() in place of isOn()
     *
     * @method get()
     * @author Will Stuckey
     * @see isOn()
     * @return false
     * @deprecated -------------------------------------------------------
     */
    @Deprecated
    @Override
    public boolean get()
    {
        return false;
    } // end get()

    // -------------------------------------------------------
    /**
     * Create an instance of a Single Throw Switch class. Creates a digital input
     * given an channel and module.
     *
     * @method SingleThrowSwitch()
     * @param slot
     *            the slot where the digital module is located
     * @param channel
     *            the port for the digital input
     * @author Bob Brown
     * @written Sep 18 2009 -------------------------------------------------------
     */

    /*
     * public SingleThrowSwitch (final int slot, final int channel) { super(slot,
     * channel); } // end constructor
     */

    // -------------------------------------------------------
    /**
     * this function gets the denotation that the switch is perceived as backwards
     *
     * @method getInverted
     * @return the value for inversion
     * @author Bob Brown
     * @written Jan 24 2023 --------------------------------------------------------
     */
    public boolean getInverted()
    {
        return this.isInverted;
    } // end getInverted()

    // -------------------------------------------------------
    /**
     * This function returns whether or not the switch is on or not.
     *
     * @method isOn
     * @return is on or not. Works even if not plugged in
     * @author Bob Brown
     * @written Sep 19, 2009 -------------------------------------------------------
     */
    public boolean isOn()
    {
        if (this.isInverted == false)
            {
            return (!super.get());
            } // if not inverted
        else
            {
            return (super.get());
            } // if inverted
    } // end isOn

    // -------------------------------------------------------
    /**
     * this function saves the denotation that the switch is perceived as backwards
     *
     * @method setInverted
     * @return the new value for inversion
     * @author Bob Brown
     * @written Jan 24 2023 --------------------------------------------------------
     */
    public boolean setInverted(boolean inversion)
    {
        this.isInverted = inversion;
        return inversion;
    } // end setInverted()

    private boolean isInverted = false;
    } // end class
