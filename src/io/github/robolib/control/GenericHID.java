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

package io.github.robolib.control;

import java.util.Arrays;

/**
 * The HID (Human Interface Device) base
 *
 * @author noriah Reuland <vix@noriah.dev>
 * @see Joystick
 */

public abstract class GenericHID {

    
    /**
     * A class representation of a Joystick Axis.
     */
    public interface Axis {
        
        /**
         * Get the Value of the Axis.
         * @return the value of the Axis.
         */
        public double get();
        
        /**
         * Invert the Axis.
         */
        public void setInverted(boolean inverted);
        
        /**
         * Set the Deadband of the Axis.
         * @param deadband the deadband value of the axis.
         */
        public void setDeadband(double deadband);
    }
    
    /**
     * A class representation of a Joystick Button.
     */
    public interface Button {
        
        /**
         * Get the Value of the Button.
         * @return the value of the Button.
         */
        public boolean get();
    }
    
    /** The m_axes. */
    protected Axis m_axes[];
    
    /** The m_btns. */
    protected Button m_btns[];
    
    /** The m_num btns. */
    protected int m_numAxes, m_numBtns;

    /**
     * Provides a middle-man in between the WPILib Library and the RoboLibJ Joysticks.
     */
    protected GenericHID(){
        this(6, 12);
    }
    
    /**
     * Provides a middle-man in between the WPILib Library and the RoboLibJ Joysticks.
     * @param numAxes the Number of Axes this Joystick will have
     * @param numBtns the Number of Buttons this Joystick will have
     */
    protected GenericHID(int numAxes, int numBtns){
        m_numAxes = numAxes;
        m_numBtns = numBtns;
    }
    
    /**
     * Get a Joystick {@link Axis}.
     * 
     * Returns an {@link Axis} instance of the requested Axis.
     * 
     * @param axis the axis to get
     * @return a {@link Axis} instance of the requested Axis
     * @see Axis
     */
    public abstract Axis getAxis(int axis);

    /**
     * Invert a Joystick {@link Axis}.
     * @param axis the axis to invert
     * @see Axis
     */
    public void invertAxis(int axis){
        getAxis(axis).setInverted(true);
    }
    
    /**
     * Set a Joystick {@link Axis} Deadband.
     *
     * @param axis the axis
     * @param deadband the deadband
     * @see Axis
     */
    public void setAxisDeadband(int axis, double deadband){
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
     * Get a Joystick {@link Button}.
     * 
     * Returns a {@link Button} instance of the requested Button.
     * 
     * @param btn the button to get
     * @return a {@link Button} instance of the requested Button
     * @see Button
     */
    public abstract Button getButton(int btn);

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
        return getButton(channel).get();
    }

    /**
     * 
     * @param pov
     * @return
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
     * From WPILibJ package edu.wpi.first.wpilibj.Joystick
     *
     * @return The direction of the vector in degrees
     *//*
    public double getDirectionDegrees() {
        return Math.toDegrees(getDirectionRadians());
    }*/
    
    /**
     * Adds the button.
     *
     * @param btn the btn
     */
    protected void addButton(final Button btn){
        m_btns = (Button[]) Arrays.copyOf(m_btns, m_numBtns + 1);
        m_btns[m_btns.length - 1] = btn;
        m_numBtns = m_btns.length;
    }
    
    /**
     * Adds the axis button.
     *
     * @param channel the channel
     * @param posThresh the pos thresh
     * @param negThresh the neg thresh
     */
    protected void addAxisButton(final int channel, final double posThresh, final double negThresh){
        addButton(new Button(){           
            public boolean get() {
                double axis = getRawAxis(channel);
                return axis >= posThresh || axis <= negThresh;
            }
        });
    }

    /**
     * The Class InvalidAxisException.
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
     * @see Axis
     */
    protected void checkAxis(int axis){
        if(0 > axis || m_numAxes < axis){
            throw new InvalidAxisException(axis);
        }
    }

    /**
     * The Class InvalidButtonException.
     */
    private class InvalidButtonException extends RuntimeException {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6918067923823042407L;
		
		/**
		 * Instantiates a new invalid button exception.
		 *
		 * @param btn the btn
		 */
		public InvalidButtonException(int btn){
            super("Button " + btn + " is Invalid, Min 1 Max " + m_numBtns);
        }
    }
    
    /**
     * Check if the button requested exists.
     *
     * @param btn the button being checked.
     * @see Button
     */
    protected void checkButton(int btn){
        if(0 > btn || m_numBtns < btn){
            throw new InvalidButtonException(btn);
        }
    }
}