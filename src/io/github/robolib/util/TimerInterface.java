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

package io.github.robolib.util;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface TimerInterface {
    /**
     * Get the current time from the timer. If the clock is running it is derived from
     * the current system clock the start time stored in the timer class. If the clock
     * is not running, then return the time when it was last stopped.
     *
     * @return Current time value for this timer in seconds
     */
    public double get();
    
    /**
     * Reset the timer by setting the time to 0.
     * Make the timer startTime the current time so new requests will be relative now
     */
    public void reset();
    
    /**
     * Start the timer running.
     * Just set the running flag to true indicating that all time requests should be
     * relative to the system clock.
     */
    public void start();
    
    /**
     * Stop the timer.
     * This computes the time as of now and clears the running flag, causing all
     * subsequent time requests to be read from the accumulated time rather than
     * looking at the system clock.
     */
    public void stop();
    
    
    /**
     * Check if the period specified has passed and if it has, advance the start
     * time by that period. This is useful to decide if it's time to do periodic
     * work without drifting later by the time it took to get around to checking.
     *
     * @param period The period to check for (in seconds).
     * @return If the period has passed.
     */
    public boolean hasPeriodPassed(double period);
}
