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

import io.github.robolib.identifier.NumberSource;

/**
 * A class representation of a Joystick Axis.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface HIDAxis extends NumberSource {
    
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
    
    public void setRampEnd(double end);
    
    public void setBacklash(double value);
    
    public void setFineControl(double value);
}