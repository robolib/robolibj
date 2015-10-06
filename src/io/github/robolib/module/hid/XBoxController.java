/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

import io.github.robolib.util.MathUtils;


/**
 * A better Joystick implementation for the XBox Controller.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class XBoxController extends Joystick {

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
    
    /** Magnitude Left Stick Axis ID. */
    public static final int AXIS_MAG_LEFT = 6;
    
    /** Magnitude Right Stick Axis ID. */
    public static final int AXIS_MAG_RIGHT = 7;
    
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
        super(port, 8, 10);
        setAxisInverted(AXIS_Y_LEFT);
        setAxisInverted(AXIS_Y_RIGHT);
        setAxis(AXIS_MAG_LEFT, new MagAxis(AXIS_X_LEFT, AXIS_Y_LEFT));
        setAxis(AXIS_MAG_RIGHT, new MagAxis(AXIS_X_RIGHT, AXIS_Y_RIGHT));
    }
    
    /**
     * Get the Left X Axis.
     *
     * @return the left X axis
     */
    public HIDAxis getAxisLeftX(){return getAxis(AXIS_X_LEFT);}
    
    /**
     * Get the Left Y Axis.
     *
     * @return the right Y axis
     */
    public HIDAxis getAxisLeftY(){return getAxis(AXIS_Y_LEFT);}
    
    /**
     * Get the Left Trigger Axis.
     *
     * @return the trigger axis
     */
    public HIDAxis getAxisLeftTrigger(){return getAxis(AXIS_TRIGGER_LEFT);}
    
    /**
     * Get the Right Trigger Axis.
     *
     * @return the trigger axis
     */
    public HIDAxis getAxisRightTrigger(){return getAxis(AXIS_TRIGGER_RIGHT);}

    /**
     * Get the Right X Axis.
     *
     * @return the right x axis
     */
    public HIDAxis getAxisRightX(){return getAxis(AXIS_X_RIGHT);}
    
    /**
     * Get the Right Y Axis.
     *
     * @return the right Y axis
     */
    public HIDAxis getAxisRightY(){return getAxis(AXIS_Y_RIGHT);}
    
    /**
     * Get the Left Stick Magnitude Axis.
     *
     * @return the left stick Magnitude axis.
     */
    public HIDAxis getAxisLeftMagnitude(){return getAxis(AXIS_MAG_LEFT);}
    
    /**
     * Get the Right Stick Magnitude Axis.
     *
     * @return the right stick Magnitude axis.
     */
    public HIDAxis getAxisRightMagnitude(){return getAxis(AXIS_MAG_RIGHT);}
    
    /**
     * Get the A Button.
     *
     * @return the A button
     */
    public HIDButton getButtonA(){return getButton(BTN_A);}
    
    /**
     * Get the B Button.
     *
     * @return the B button
     */
    public HIDButton getButtonB(){return getButton(BTN_B);}
    
    /**
     * Get the X Button.
     *
     * @return the X button
     */
    public HIDButton getButtonX(){return getButton(BTN_X);}

    /**
     * Get the Y Button.
     *
     * @return the Y button
     */
    public HIDButton getButtonY(){return getButton(BTN_Y);}
    
    /**
     * Get the Left Shoulder Button.
     *
     * @return the Left Shoulder button
     */
    public HIDButton getButtonLeftShoulder(){return getButton(BTN_SHOULDER_LEFT);}
    
    /**
     * Get the Right Shoulder Button.
     *
     * @return the Right Shoulder button
     */
    public HIDButton getButtonRightShoulder(){return getButton(BTN_SHOULDER_RIGHT);}
    
    /**
     * Get the Start Button.
     *
     * @return the Start button
     */
    public HIDButton getButtonStart(){return getButton(BTN_START);}
    
    /**
     * Get the Select Button.
     *
     * @return the Select button
     */
    public HIDButton getButtonSelect(){return getButton(BTN_SELECT);}
    
    /**
     * Get the Left Stick Button.
     *
     * @return the Left Stick button
     */
    public HIDButton getButtonLeftStick(){return getButton(BTN_STICK_LEFT);}
    
    /**
     * Get the Right Stick Button.
     *
     * @return the Right Stick button
     */
    public HIDButton getButtonRightStick(){return getButton(BTN_STICK_RIGHT);}

    /**
     * Get the Left X Axis value.
     *
     * @return the left X axis value
     */
    public double getAxisLeftXValue(){return getRawAxis(AXIS_X_LEFT);}
    
    /**
     * Get the Left Y Axis value.
     *
     * @return the right Y axis value
     */
    public double getAxisLeftYValue(){return getRawAxis(AXIS_Y_LEFT);}
    
    /**
     * Get the Left Trigger Axis value.
     *
     * @return the trigger axis value
     */
    public double getAxisLeftTriggerValue(){return getRawAxis(AXIS_TRIGGER_LEFT);}
    
    /**
     * Get the Right Trigger Axis value.
     *
     * @return the trigger axis value
     */
    public double getAxisRightTriggerValue(){return getRawAxis(AXIS_TRIGGER_RIGHT);}

    /**
     * Get the Right X Axis value.
     *
     * @return the right x axis value
     */
    public double getAxisRightXValue(){return getRawAxis(AXIS_X_RIGHT);}
    
    /**
     * Get the Right Y Axis value.
     *
     * @return the right Y axis value
     */
    public double getAxisRightYValue(){return getRawAxis(AXIS_Y_RIGHT);}
    
    /**
     * Get the Left Stick Magnitude Axis value.
     *
     * @return the left stick Magnitude axis value.
     */
    public double getAxisLeftValue(){return getRawAxis(AXIS_MAG_LEFT);}
    
    /**
     * Get the Right Stick Magnitude Axis value.
     *
     * @return the right stick Magnitude axis value.
     */
    public double getAxisRightValue(){return getRawAxis(AXIS_MAG_RIGHT);}

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
    public boolean getButtonAValue(){return getRawButton(BTN_A);}
    
    /**
     * Get the B Button value.
     *
     * @return the B button value
     */
    public boolean getButtonBValue(){return getRawButton(BTN_B);}
    
    /**
     * Get the X Button value.
     *
     * @return the X button value
     */
    public boolean getButtonXValue(){return getRawButton(BTN_X);}

    /**
     * Get the Y Button value.
     *
     * @return the Y button value
     */
    public boolean getButtonYValue(){return getRawButton(BTN_Y);}
    
    /**
     * Get the Left Shoulder Button value.
     *
     * @return the Left Shoulder button value
     */
    public boolean getButtonLeftShoulderValue(){return getRawButton(BTN_SHOULDER_LEFT);}
    
    /**
     * Get the Right Shoulder Button value.
     *
     * @return the Right Shoulder button value
     */
    public boolean getButtonRightShoulderValue(){return getRawButton(BTN_SHOULDER_RIGHT);}
    
    /**
     * Get the Start Button value.
     *
     * @return the Start button value
     */
    public boolean getButtonStartValue(){return getRawButton(BTN_START);}
    
    /**
     * Get the Select Button value.
     *
     * @return the Select button value
     */
    public boolean getButtonSelectValue(){return getRawButton(BTN_SELECT);}
    
    /**
     * Get the Left Stick Button value.
     *
     * @return the Left Stick button value
     */
    public boolean getButtonLeftStickValue(){return getRawButton(BTN_STICK_LEFT);}
    
    /**
     * Get the Right Stick Button value.
     *
     * @return the Right Stick button value
     */
    public boolean getButtonRightStickValue(){return getRawButton(BTN_STICK_RIGHT);}
    
    /**
     * Get the direction of the vector formed by the left stick and its origin
     * in radians
     *
     * @return The direction of the vector in radians of the left stick
     */
    public double getLeftDirectionRadians() {
        return Math.atan2(getAxisLeftXValue(), getAxisLeftYValue());
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
     * Get the direction of the vector formed by the right stick and its origin
     * in radians
     *
     * @return The direction of the vector in radians of the right stick
     */
    public double getRightDirectionRadians() {
        return Math.atan2(getAxisRightXValue(), getAxisRightYValue());
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
    
    private final class MagAxis implements HIDAxis {
        
        private final int m_a1, m_a2;
        private double m_deadband;
        
        MagAxis(int axis1, int axis2){
            m_a1 = axis1;
            m_a2 = axis2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double get(){
            double out = MathUtils.pythagorize(getRawAxis(m_a1), getRawAxis(m_a2));
            return (Math.abs(out) >= m_deadband ? out : 0.0);
        }

        /**
         * Does Nothing
         */
        @Override
        public void setInverted(boolean inverted){}

        /**
         * {@inheritDoc}
         */
        @Override
        public void setDeadband(double value){
            m_deadband = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setRampEnd(double end) {
            // TODO Auto-generated method stub
            
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setBacklash(double value) {
            // TODO Auto-generated method stub
            
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setFineControl(double value) {
            // TODO Auto-generated method stub
            
        }
        
    }

}
