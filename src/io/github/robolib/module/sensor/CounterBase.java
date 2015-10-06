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

package io.github.robolib.module.sensor;

import io.github.robolib.identifier.CounterSource;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class CounterBase implements CounterSource {
    
    /**
     * Possible Encoding types
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum EncodingType {
        k1X,
        k2X,
        k4X;
    }
    
    protected int m_index;
    
    /**
     * @return the Counter's FPGA index
     */
    public int getFPGAIndex(){
        return m_index;
    }
    
    /**
     * Read the current counter value. Read the value at this instant. It may
     * still be running, so it reflects the current value. Next time it is read,
     * it might have a different value.
     */
    public int get(){
        return getCount();
    }
    
    /**
     * Read the current counter value. Read the value at this instant. It may
     * still be running, so it reflects the current value. Next time it is read,
     * it might have a different value.
     */
    public abstract int getCount();
    
    /**
     * Reset the count to zero
     */
    public abstract void reset();
    
    /**
     * Get the time between the last two edges counted
     * @return the time beteween the last two ticks in seconds
     */
    abstract double getPeriod();
    
    /**
     * Set the maximum time between edges to be considered stalled
     * @param max the maximum period in seconds
     */
    abstract void setMaxPeriod(double max);
    
    /**
     * Determine if the counter is not moving
     * @return true if the counter has not changed for the max period
     */
    abstract boolean getStopped();
    
    /**
     * Determine which direction the counter is going
     * @return true for one direction, false for the other
     */
    abstract boolean getDirection();
    
}
