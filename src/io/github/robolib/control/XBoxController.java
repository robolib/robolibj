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


/**
 * A better Joystick implementation for the XBox Controller.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class XBoxController extends Joystick {

    /** Left X Axis ID. */
    public static final int AXIS_X_LEFT = 0;
    
    /** Left Y Axis ID. */
    public static final int AXIS_Y_LEFT = 1;
    
    /** Left Trigger Axis ID. */
    public static final int AXIS_TRIGGER_LEFT = 2;
    
    /** Right Trigger Axis ID. */
    public static final int AXIS_TRIGGER_RIGHT = 3;
    
    /** Right X Axis ID. */
    public static final int AXIS_X_RIGHT = 4;
    
    /** Right Y Axis ID. */
    public static final int AXIS_Y_RIGHT = 5;
    
    /** Hat X Axis ID. */
    public static final int AXIS_HAT_X = 6;
    
    /** Hat Y Axis ID. */
    public static final int AXIS_HAT_Y = 7;

    /** A Button ID. */
    public static final int BTN_A = 0;
    
    /** B Button ID. */
    public static final int BTN_B = 1;
    
    /** X Button ID. */
    public static final int BTN_X = 2;
    
    /** Y Button ID. */
    public static final int BTN_Y = 3;
    
    /** Left Shoulder Button ID. */
    public static final int BTN_SHOULDER_LEFT = 4;
    
    /** Right Shoulder Button ID. */
    public static final int BTN_SHOULDER_RIGHT = 5;
    
    /** Start Button ID. */
    public static final int BTN_START = 7;
    
    /** Back Button ID. */
    public static final int BTN_SELECT = 6;
    
    /** Left Stick Button ID. */
    public static final int BTN_STICK_LEFT = 8;
    
    /** Right Stick Button ID. */
    public static final int BTN_STICK_RIGHT = 9;
    
    /** Left X Axis Button ID. */
    public static final int BTN_AXIS_X_LEFT = AXIS_X_LEFT + 12;
    
    /** Left Y Axis Button ID. */
    public static final int BTN_AXIS_Y_LEFT = AXIS_Y_LEFT + 12;
    
    /** Left Trigger Axis Button ID. */
    public static final int BTN_AXIS_TRIGGER_LEFT = AXIS_TRIGGER_LEFT + 12;
    
    /** Right Trigger Axis Button ID. */
    public static final int BTN_AXIS_TRIGGER_RIGHT = AXIS_TRIGGER_RIGHT + 12;
    
    /** Right X Axis Button ID. */
    public static final int BTN_AXIS_X_RIGHT = AXIS_X_RIGHT + 12;
    
    /** Right Y Axis Button ID. */
    public static final int BTN_AXIS_Y_RIGHT = AXIS_Y_RIGHT + 12;
    
    /** Hat X Axis Button ID. */
    public static final int BTN_AXIS_HAT_X = AXIS_HAT_X + 12;

    /**
     * A better Joystick implementation for the XBox Controller.
     *
     * @param port The Joystick Number this XBoxController is on.
     */
    public XBoxController(final Stick port){
        super(port, 6, 10);
        invertAxis(AXIS_Y_LEFT);
        invertAxis(AXIS_Y_RIGHT);
        //invertAxis(AXIS_TRIGGERS);
    }
    
    /**
     * Get the Left X Axis.
     *
     * @return the left X axis value
     */
    public double getLeftX(){return getRawAxis(AXIS_X_LEFT);}
    
    /**
     * Get the Left Y Axis.
     *
     * @return the right Y axis value
     */
    public double getLeftY(){return getRawAxis(AXIS_Y_LEFT);}
    
    /**
     * Get the Trigger Axis.
     *
     * @return the trigger axis value
     */
    public double getLeftTrigger(){return getRawAxis(AXIS_TRIGGER_LEFT);}
    
    /**
     * Get the Trigger Axis.
     *
     * @return the trigger axis value
     */
    public double getRightTrigger(){return getRawAxis(AXIS_TRIGGER_LEFT);}

    /**
     * Get the Right X Axis.
     *
     * @return the right x axis value
     */
    public double getRightX(){return getRawAxis(AXIS_X_RIGHT);}
    
    /**
     * Get the Right Y Axis.
     *
     * @return the right Y axis value
     */
    public double getRightY(){return getRawAxis(AXIS_Y_RIGHT);}

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
    public boolean btnLeftShoulder(){return getRawButton(BTN_SHOULDER_LEFT);}
    
    /**
     * Get the Right Shoulder Button.
     *
     * @return the Right Shoulder button value
     */
    public boolean btnRightShoulder(){return getRawButton(BTN_SHOULDER_RIGHT);}
    
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
    public boolean btnLeftStick(){return getRawButton(BTN_STICK_LEFT);}
    
    /**
     * Get the Right Stick Button.
     *
     * @return the Right Stick button value
     */
    public boolean btnRightStick(){return getRawButton(BTN_STICK_RIGHT);}
    
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