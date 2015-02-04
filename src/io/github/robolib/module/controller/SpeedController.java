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

package io.github.robolib.module.controller;

import io.github.robolib.identifier.PIDSink;
import io.github.robolib.identifier.SpeedSink;

/**
 * The Interface SpeedController. A SpeedController is usually a motor
 * controller connected to a CIM or other FRC legal motors. Most are PWM
 * controlled, but there are the new ones, such as the TalonSRX, which
 * run on CAN as well as the old Jaguars with CAN interface.
 * 
 * You might think im missing a set(double) here, but this is a <b>Speed
 * Controller</b>. That means we are setting speed, not just anything.
 * I have found that being specific in code, when needed, does wonder for
 * those that don't understand it.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface SpeedController extends PIDSink, SpeedSink {
        
    /**
     * Common interface for getting the current set speed of a speed
     * controller.
     *
     * @return The current set speed.  Value is between -1.0 and 1.0.
     */
    double getSpeed();

    /**
     * Common interface for setting the speed of a speed controller.
     * 
     *
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     * @param syncGroup The update group to add this Set() to, pending
     * UpdateSyncGroup(). If 0, update immediately.
     * @deprecated Replaced by {@link #setSpeed(double)}, only here for
     * CANJaguar compatibility.
     */
    public default void set(double speed, byte syncGroup){
        setSpeed(speed);
    }
    
    /**
     * Set the speed of the SpeedController
     * 
     * @param speed the speed to set
     */
    void setSpeed(double speed);
    
    /**
     * Set the inverted state of the speed controller.
     * 
     * @param inverted true to invert the values sent to the motor.
     */
    void setInverted(boolean inverted);
    
    /**
     * Stop/Disable the motor.
     */
    void stopMotor();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public default void pidWrite(double value){
        setSpeed(value);
    }

}
