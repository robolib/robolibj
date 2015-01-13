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

package org.team2583.robolib.control;


/**
 * A better Joystick implementation for the XBox Controller.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class XBoxController extends Joystick {

    /** Left X Axis ID. */
    public static final int AXIS_LEFT_X = 0;
    
    /** Left Y Axis ID. */
    public static final int AXIS_LEFT_Y = 1;
    
    /** Triggers Axis ID. */
    public static final int AXIS_TRIGGERS = 2;
    
    /** Right X Axis ID. */
    public static final int AXIS_RIGHT_X = 3;
    
    /** Right Y Axis ID. */
    public static final int AXIS_RIGHT_Y = 4;
    
    /** Hat X Axis ID. */
    public static final int AXIS_HAT_X = 5;
    
    /** Hat Y Axis ID. */
    public static final int AXIS_HAT_Y = 6;

    /** A Button ID. */
    public static final int BTN_A = 0;
    
    /** B Button ID. */
    public static final int BTN_B = 1;
    
    /** X Button ID. */
    public static final int BTN_X = 2;
    
    /** Y Button ID. */
    public static final int BTN_Y = 3;
    
    /** Left Shoulder Button ID. */
    public static final int BTN_LEFT_SHOULDER = 4;
    
    /** Right Shoulder Button ID. */
    public static final int BTN_RIGHT_SHOULDER = 5;
    
    /** Start Button ID. */
    public static final int BTN_START = 7;
    
    /** Back Button ID. */
    public static final int BTN_SELECT = 6;
    
    /** Left Stick Button ID. */
    public static final int BTN_LEFT_STICK = 8;
    
    /** Right Stick Button ID. */
    public static final int BTN_RIGHT_STICK = 9;
    
    /** Left X Axis Button ID. */
    public static final int BTN_AXIS_LEFT_X = AXIS_LEFT_X + 12;
    
    /** Left Y Axis Button ID. */
    public static final int BTN_AXIS_LEFT_Y = AXIS_LEFT_Y + 12;
    
    /** Triggers Axis Button ID. */
    public static final int BTN_AXIS_TRIGGERS = AXIS_TRIGGERS + 12;
    
    /** Right X Axis Button ID. */
    public static final int BTN_AXIS_RIGHT_X = AXIS_RIGHT_X + 12;
    
    /** Right Y Axis Button ID. */
    public static final int BTN_AXIS_RIGHT_Y = AXIS_RIGHT_Y + 12;
    
    /** Hat X Axis Button ID. */
    public static final int BTN_AXIS_HAT_X = AXIS_HAT_X + 12;

    /**
     * A better Joystick implementation for the XBox Controller.
     *
     * @param port The Joystick Number this XBoxController is on.
     */
    public XBoxController(final Stick port){
        super(port, 7, 10);
        //invertAxis(AXIS_LEFT_Y);
        //invertAxis(AXIS_RIGHT_Y);
        //invertAxis(AXIS_TRIGGERS);
    }
    
    /**
     * Get the Left X Axis.
     *
     * @return the left X axis value
     */
    public double getLeftX(){return getRawAxis(AXIS_LEFT_X);}
    
    /**
     * Get the Left Y Axis.
     *
     * @return the right Y axis value
     */
    public double getLeftY(){return getRawAxis(AXIS_LEFT_Y);}
    
    /**
     * Get the Trigger Axis.
     *
     * @return the trigger axis value
     */
    public double getTriggers(){return getRawAxis(AXIS_TRIGGERS);}

    /**
     * Get the Right X Axis.
     *
     * @return the right x axis value
     */
    public double getRightX(){return getRawAxis(AXIS_RIGHT_X);}
    
    /**
     * Get the Right Y Axis.
     *
     * @return the right Y axis value
     */
    public double getRightY(){return getRawAxis(AXIS_RIGHT_Y);}

    /**
     * Get the D-Pad/Hat X Axis.
     *
     * @return the d-pad X axis value
     */
    public double getHatX(){return getRawAxis(AXIS_HAT_X);}
    
    /**
     * Get the D-Pad/Hat Y Axis.
     * 
     * @return the d-pad Y axis value
     */
    public double getHatY(){return getRawAxis(AXIS_HAT_Y);}
    
    /**
     * Get the A Button.
     *
     * @return the A button value
     */
    public boolean btnA(){return getRawButton(BTN_A);}
    
    /**
     * Get the B Button.
     *
     * @return the B button value
     */
    public boolean btnB(){return getRawButton(BTN_B);}
    
    /**
     * Get the X Button.
     *
     * @return the X button value
     */
    public boolean btnX(){return getRawButton(BTN_X);}

    /**
     * Get the Y Button.
     *
     * @return the Y button value
     */
    public boolean btnY(){return getRawButton(BTN_Y);}
    
    /**
     * Get the Left Shoulder Button.
     *
     * @return the Left Shoulder button value
     */
    public boolean btnLeftShoulder(){return getRawButton(BTN_LEFT_SHOULDER);}
    
    /**
     * Get the Right Shoulder Button.
     *
     * @return the Right Shoulder button value
     */
    public boolean btnRightShoulder(){return getRawButton(BTN_RIGHT_SHOULDER);}
    
    /**
     * Get the Start Button.
     *
     * @return the Start button value
     */
    public boolean btnStart(){return getRawButton(BTN_START);}
    
    /**
     * Get the Select Button.
     *
     * @return the Select button value
     */
    public boolean btnSelect(){return getRawButton(BTN_SELECT);}
    
    /**
     * Get the Left Stick Button.
     *
     * @return the Left Stick button value
     */
    public boolean btnLeftStick(){return getRawButton(BTN_LEFT_STICK);}
    
    /**
     * Get the Right Stick Button.
     *
     * @return the Right Stick button value
     */
    public boolean btnRightStick(){return getRawButton(BTN_RIGHT_STICK);}
    
    /**
     * Get the magnitude of the direction vector formed by the left stick's
     * current position relative to its origin
     * 
     * From WPILibJ package edu.wpi.first.wpilibj.Joystick
     *
     * @return The magnitude of the direction vector of the left stick
     */
    public double getLeftMagnitude() {
        return Math.sqrt(Math.pow(getRightX(), 2) + Math.pow(getLeftY(), 2));
    }

    /**
     * Get the direction of the vector formed by the left stick and its origin
     * in radians
     * 
     * From WPILibJ package edu.wpi.first.wpilibj.Joystick
     *
     * @return The direction of the vector in radians of the left stick
     */
    public double getLeftDirectionRadians() {
        return Math.atan2(getLeftX(), getLeftY());
    }

    /**
     * Get the direction of the vector formed by the left stick and its origin
     * in degrees
     *
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
     * 
     * From WPILibJ package edu.wpi.first.wpilibj.Joystick
     *
     * @return The direction of the vector in degrees of the left stick
     */
    public double getLeftDirectionDegrees() {
        return Math.toDegrees(getLeftDirectionRadians());
    }

    /**
     * Get the magnitude of the direction vector formed by the right stick's
     * current position relative to its origin
     * 
     * From WPILibJ package edu.wpi.first.wpilibj.Joystick
     *
     * @return The magnitude of the direction vector of the right stick
     */
    public double getRightMagnitude() {
        return Math.sqrt(Math.pow(getRightX(), 2) + Math.pow(getRightY(), 2));
    }

    /**
     * Get the direction of the vector formed by the right stick and its origin
     * in radians
     * 
     * From WPILibJ package edu.wpi.first.wpilibj.Joystick
     *
     * @return The direction of the vector in radians of the right stick
     */
    public double getRightDirectionRadians() {
        return Math.atan2(getRightX(), getRightY());
    }

    /**
     * Get the direction of the vector formed by the right stick and its origin
     * in degrees
     *
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
     * 
     * From WPILibJ package edu.wpi.first.wpilibj.Joystick
     *
     * @return The direction of the vector in degrees of the right stick
     */
    public double getRightDirectionDegrees() {
        return Math.toDegrees(getRightDirectionRadians());
    }

}