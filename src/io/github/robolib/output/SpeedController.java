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

package io.github.robolib.output;

import io.github.robolib.pid.PIDOutput;

/**
 * The Interface SpeedController.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface SpeedController extends MotorSafety, PIDOutput {
    
    /**
     * Common interface for getting the current set speed of a speed controller.
     *
     * @return The current set speed.  Value is between -1.0 and 1.0.
     */
    double get();

    /**
     * Common interface for setting the speed of a speed controller.
     *
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     * @param syncGroup The update group to add this Set() to, pending UpdateSyncGroup().  If 0, update immediately.
     */
    public default void set(double speed, byte syncGroup){
        setSpeed(speed);
    }

    /**
     * Common interface for setting the speed of a speed controller.
     *
     * @param speed The speed to set.  Value should be between -1.0 and 1.0.
     */
    public default void set(double speed){
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
     * {@inheritDoc}
     */
    public default void pidWrite(double value){
        setSpeed(value);
    }
    
    /**
     * {@inheritDoc}
     */
    public default void stopMotor(){       
        setSpeed(0.0);
    }
}
