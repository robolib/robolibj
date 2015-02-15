/*
 * Copyright (c) 2015 noriah <vix@noriah.dev>.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 */

package io.github.robolib.module.hid;

import java.util.Arrays;

/**
 * The HID (Human Interface Device) base
 *
 * @author noriah Reuland <vix@noriah.dev>
 * @see Joystick
 */

public abstract class GenericHID {
    
    /** The m_axes. */
    protected HIDAxis m_axes[];
    
    /** The m_btns. */
    protected HIDButton m_btns[];
    
    /** The m_num btns. */
    protected int m_numAxes, m_numBtns;

    /**
     * RoboLibJ Joysticks.
     */
    protected GenericHID(){
        this(6, 12);
    }
    
    /**
     * RoboLibJ Joysticks.
     * @param numAxes the Number of Axes this Joystick will have
     * @param numBtns the Number of Buttons this Joystick will have
     */
    protected GenericHID(int numAxes, int numBtns){
        m_numAxes = numAxes;
        m_axes = new HIDAxis[numAxes];
        m_numBtns = numBtns;
        m_btns = new HIDButton[numBtns];
    }
    
    /**
     * Get a Joystick {@link HIDAxis}.
     * 
     * Returns an {@link HIDAxis} instance of the requested Axis.
     * 
     * @param axis the axis to get
     * @return a {@link HIDAxis} instance of the requested Axis
     * @see HIDAxis
     */
    public final HIDAxis getAxis(int axis) {
        checkAxis(axis);
        return m_axes[axis];
    }
    
    /**
     * Set a Joystick {@link HIDAxis}.
     * 
     * Returns an {@link HIDAxis} instance of the requested Axis.
     * 
     * @param index the axis to set
     * @param axis the new {@link HIDAxis}
     * @see HIDAxis
     */
    public final void setAxis(int index, HIDAxis axis){
        checkButton(index);
        m_axes[index] = axis;
    }
    
    /**
     * Get a Joystick {@link HIDAxis}.
     * 
     * Sets the id to a {@link HIDAxis} instance of the requested Axis.
     * 
     * @param axis the axis to get
     * @return a {@link HIDAxis} instance of the requested Axis
     * @see HIDAxis
     */

    /**
     * Invert a Joystick {@link HIDAxis}.
     * @param axis the axis to invert
     * @see HIDAxis
     */
    public final void setAxisInverted(int axis){
        getAxis(axis).setInverted(true);
    }
    
    /**
     * Set a Joystick {@link HIDAxis} Deadband.
     *
     * @param axis the axis
     * @param deadband the deadband
     * @see HIDAxis
     */
    public final void setAxisDeadband(int axis, double deadband){
        getAxis(axis).setDeadband(deadband);
    }
    
    /**
     * Get the x position of HID
     * @return the x position
     *//*
    public abstract double getX();
    
    *//**
     * Get the y position of the HID
     * @return the y position
     *//*
    public abstract double getY();*/
    
    /**
     * Get a Joystick {@link HIDButton}.
     * 
     * Returns a {@link HIDButton} instance of the requested Button.
     * 
     * @param button the button to get
     * @return a {@link HIDButton} instance of the requested Button
     * @see HIDButton
     */
    public final HIDButton getButton(int button) {
        checkButton(button);
        return m_btns[button];
    }
    
    /**
     * Set a Joystick {@link HIDButton}.
     * 
     * Sets the id to a {@link HIDButton} instance of the requested Button.
     * 
     * @param index the button to get
     * @param button the new {@link HIDButton}
     * @see HIDButton
     */
    public final void setButton(int index, HIDButton button){
        checkButton(index);
        m_btns[index] = button;
    }

    /**
     * Get the raw axis
     * @param channel index of the axis
     * @return the raw value of the selected axis
     */
    public final double getRawAxis(int channel) {
        return getAxis(channel).get();
    }

    /**
     * Is the given button pressed
     * @param channel which button number
     * @return true if the button is pressed
     */
    public final boolean getRawButton(int channel) {
        return getButton(channel).getState();
    }

    /**
     * 
     * @param pov
     * @return the pov value
     */
    public int getPOV(int pov) {
        return 0;
    }
    
    /**
     * Get the magnitude of the direction vector formed by the joystick's
     * current position relative to its origin
     *
     * @return The magnitude of the direction vector
     *//*
    public double getMagnitude() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    *//**
     * Get the direction of the vector formed by the joystick and its origin
     * in radians
     * 
     *
     * @return The direction of the vector in radians
     *//*
    public double getDirectionRadians() {
        return Math.atan2(getX(), -getY());
    }

    *//**
     * Get the direction of the vector formed by the joystick and its origin
     * in degrees
     *
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
     *
     * @return The direction of the vector in degrees
     *//*
    public double getDirectionDegrees() {
        return Math.toDegrees(getDirectionRadians());
    }*/
    
    /**
     * Adds the button.
     *
     * @param button the btn
     */
    protected final void addButton(final HIDButton button){
        m_btns = Arrays.copyOf(m_btns, m_numBtns + 1);
        m_btns[m_btns.length - 1] = button;
        m_numBtns = m_btns.length;
    }
    
    /**
     * Adds the axis button.
     *
     * @param channel the channel
     * @param posThresh the pos thresh
     * @param negThresh the neg thresh
     */
    protected final void addAxisButton(final int channel, final double posThresh, final double negThresh){
        addButton(new HIDButton(){           
            @Override
            public boolean getState() {
                double axis = getRawAxis(channel);
                return axis >= posThresh || axis <= negThresh;
            }
        });
    }

    /**
     * The Class InvalidAxisException.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    private class InvalidAxisException extends RuntimeException {
        
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1820453100088416409L;
		
		/**
		 * Instantiates a new invalid axis exception.
		 *
		 * @param axis the axis
		 */
		public InvalidAxisException(int axis){
            super("Axis " + axis + " is Invalid, Min 1 Max " + m_numAxes);
        }
    }
    
    /**
     * Check if the axis requested exists.
     *
     * @param axis the axis being checked
     * @see HIDAxis
     */
    protected final void checkAxis(int axis){
        if(0 > axis || m_numAxes < axis){
            throw new InvalidAxisException(axis);
        }
    }

    /**
     * The Class InvalidButtonException.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    private class InvalidButtonException extends RuntimeException {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6918067923823042407L;
		
		/**
		 * Instantiates a new invalid button exception.
		 *
		 * @param button the btn
		 */
		public InvalidButtonException(int button){
            super("Button " + button + " is Invalid, Min 1 Max " + m_numBtns);
        }
    }
    
    /**
     * Check if the button requested exists.
     *
     * @param button the button being checked.
     * @see HIDButton
     */
    protected final void checkButton(int button){
        if(0 > button || m_numBtns < button){
            throw new InvalidButtonException(button);
        }
    }
}