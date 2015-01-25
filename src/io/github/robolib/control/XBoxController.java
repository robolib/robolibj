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
    public static final int POV_HAT = 0;

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
    public static final int BTN_AXIS_X_LEFT = AXIS_X_LEFT + 10;
    
    /** Left Y Axis Button ID. */
    public static final int BTN_AXIS_Y_LEFT = AXIS_Y_LEFT + 10;
    
    /** Left Trigger Axis Button ID. */
    public static final int BTN_AXIS_TRIGGER_LEFT = AXIS_TRIGGER_LEFT + 10;
    
    /** Right Trigger Axis Button ID. */
    public static final int BTN_AXIS_TRIGGER_RIGHT = AXIS_TRIGGER_RIGHT + 10;
    
    /** Right X Axis Button ID. */
    public static final int BTN_AXIS_X_RIGHT = AXIS_X_RIGHT + 10;
    
    /** Right Y Axis Button ID. */
    public static final int BTN_AXIS_Y_RIGHT = AXIS_Y_RIGHT + 10;
    /**
     * A better Joystick implementation for the XBox Controller.
     *
     * @param port The Joystick Number this XBoxController is on.
     */
    public XBoxController(final JSID port){
        super(port, 6, 10);
        setAxisInverted(AXIS_Y_LEFT);
        setAxisInverted(AXIS_Y_RIGHT);
        //invertAxis(AXIS_TRIGGERS);
    }
    
    /**
     * Get the Left X Axis.
     *
     * @return the left X axis
     */
    public HIDAxis getAxis_LeftX(){return getAxis(AXIS_X_LEFT);}
    
    /**
     * Get the Left Y Axis.
     *
     * @return the right Y axis
     */
    public HIDAxis getAxis_LeftY(){return getAxis(AXIS_Y_LEFT);}
    
    /**
     * Get the Left Trigger Axis.
     *
     * @return the trigger axis
     */
    public HIDAxis getAxis_LeftTrigger(){return getAxis(AXIS_TRIGGER_LEFT);}
    
    /**
     * Get the Right Trigger Axis.
     *
     * @return the trigger axis
     */
    public HIDAxis getAxis_RightTrigger(){return getAxis(AXIS_TRIGGER_RIGHT);}

    /**
     * Get the Right X Axis.
     *
     * @return the right x axis
     */
    public HIDAxis getAxis_RightX(){return getAxis(AXIS_X_RIGHT);}
    
    /**
     * Get the Right Y Axis.
     *
     * @return the right Y axis
     */
    public HIDAxis getAxis_RightY(){return getAxis(AXIS_Y_RIGHT);}
    
    /**
     * Get the A Button.
     *
     * @return the A button
     */
    public HIDButton getButton_A(){return getButton(BTN_A);}
    
    /**
     * Get the B Button.
     *
     * @return the B button
     */
    public HIDButton getButton_B(){return getButton(BTN_B);}
    
    /**
     * Get the X Button.
     *
     * @return the X button
     */
    public HIDButton getButton_X(){return getButton(BTN_X);}

    /**
     * Get the Y Button.
     *
     * @return the Y button
     */
    public HIDButton getButton_Y(){return getButton(BTN_Y);}
    
    /**
     * Get the Left Shoulder Button.
     *
     * @return the Left Shoulder button
     */
    public HIDButton getButton_LeftShoulder(){return getButton(BTN_SHOULDER_LEFT);}
    
    /**
     * Get the Right Shoulder Button.
     *
     * @return the Right Shoulder button
     */
    public HIDButton getButton_RightShoulder(){return getButton(BTN_SHOULDER_RIGHT);}
    
    /**
     * Get the Start Button.
     *
     * @return the Start button
     */
    public HIDButton getButton_Start(){return getButton(BTN_START);}
    
    /**
     * Get the Select Button.
     *
     * @return the Select button
     */
    public HIDButton getButton_Select(){return getButton(BTN_SELECT);}
    
    /**
     * Get the Left Stick Button.
     *
     * @return the Left Stick button
     */
    public HIDButton getButton_LeftStick(){return getButton(BTN_STICK_LEFT);}
    
    /**
     * Get the Right Stick Button.
     *
     * @return the Right Stick button
     */
    public HIDButton getButton_RightStick(){return getButton(BTN_STICK_RIGHT);}

    /**
     * Get the Left X Axis value.
     *
     * @return the left X axis value
     */
    public double getRawAxis_LeftX(){return getRawAxis(AXIS_X_LEFT);}
    
    /**
     * Get the Left Y Axis value.
     *
     * @return the right Y axis value
     */
    public double getRawAxis_LeftY(){return getRawAxis(AXIS_Y_LEFT);}
    
    /**
     * Get the Left Trigger Axis value.
     *
     * @return the trigger axis value
     */
    public double getRawAxis_LeftTrigger(){return getRawAxis(AXIS_TRIGGER_LEFT);}
    
    /**
     * Get the Right Trigger Axis value.
     *
     * @return the trigger axis value
     */
    public double getRawAxis_RightTrigger(){return getRawAxis(AXIS_TRIGGER_RIGHT);}

    /**
     * Get the Right X Axis value.
     *
     * @return the right x axis value
     */
    public double getRawAxis_RightX(){return getRawAxis(AXIS_X_RIGHT);}
    
    /**
     * Get the Right Y Axis value.
     *
     * @return the right Y axis value
     */
    public double getRawAxis_RightY(){return getRawAxis(AXIS_Y_RIGHT);}

    /**
     * Get the D-Pad/Hat POV Vale.
     *
     * @return the d-pad pov value
     */
    public int getHat(){return getPOV(POV_HAT);}
    
    /**
     * Get the A Button value.
     *
     * @return the A button value
     */
    public boolean getRawButton_A(){return getRawButton(BTN_A);}
    
    /**
     * Get the B Button value.
     *
     * @return the B button value
     */
    public boolean getRawButton_B(){return getRawButton(BTN_B);}
    
    /**
     * Get the X Button value.
     *
     * @return the X button value
     */
    public boolean getRawButton_X(){return getRawButton(BTN_X);}

    /**
     * Get the Y Button value.
     *
     * @return the Y button value
     */
    public boolean getRawButton_Y(){return getRawButton(BTN_Y);}
    
    /**
     * Get the Left Shoulder Button value.
     *
     * @return the Left Shoulder button value
     */
    public boolean getRawButton_LeftShoulder(){return getRawButton(BTN_SHOULDER_LEFT);}
    
    /**
     * Get the Right Shoulder Button value.
     *
     * @return the Right Shoulder button value
     */
    public boolean getRawButton_RightShoulder(){return getRawButton(BTN_SHOULDER_RIGHT);}
    
    /**
     * Get the Start Button value.
     *
     * @return the Start button value
     */
    public boolean getRawButton_Start(){return getRawButton(BTN_START);}
    
    /**
     * Get the Select Button value.
     *
     * @return the Select button value
     */
    public boolean getRawButton_Select(){return getRawButton(BTN_SELECT);}
    
    /**
     * Get the Left Stick Button value.
     *
     * @return the Left Stick button value
     */
    public boolean getRawButton_LeftStick(){return getRawButton(BTN_STICK_LEFT);}
    
    /**
     * Get the Right Stick Button value.
     *
     * @return the Right Stick button value
     */
    public boolean getRawButton_RightStick(){return getRawButton(BTN_STICK_RIGHT);}
    
    /**
     * Get the magnitude of the direction vector formed by the left stick's
     * current position relative to its origin
     *
     * @return The magnitude of the direction vector of the left stick
     */
    public double getLeftMagnitude() {
        return Math.sqrt(Math.pow(getRawAxis_RightX(), 2) + Math.pow(getRawAxis_LeftY(), 2));
    }

    /**
     * Get the direction of the vector formed by the left stick and its origin
     * in radians
     *
     * @return The direction of the vector in radians of the left stick
     */
    public double getLeftDirectionRadians() {
        return Math.atan2(getRawAxis_LeftX(), getRawAxis_LeftY());
    }

    /**
     * Get the direction of the vector formed by the left stick and its origin
     * in degrees
     *
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
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
     * @return The magnitude of the direction vector of the right stick
     */
    public double getRightMagnitude() {
        return Math.sqrt(Math.pow(getRawAxis_RightX(), 2) + Math.pow(getRawAxis_RightY(), 2));
    }

    /**
     * Get the direction of the vector formed by the right stick and its origin
     * in radians
     *
     * @return The direction of the vector in radians of the right stick
     */
    public double getRightDirectionRadians() {
        return Math.atan2(getRawAxis_RightX(), getRawAxis_RightY());
    }

    /**
     * Get the direction of the vector formed by the right stick and its origin
     * in degrees
     *
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
     *
     * @return The direction of the vector in degrees of the right stick
     */
    public double getRightDirectionDegrees() {
        return Math.toDegrees(getRightDirectionRadians());
    }

}