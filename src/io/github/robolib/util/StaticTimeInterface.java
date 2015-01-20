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
public interface StaticTimeInterface {
    
    /**
     * Return the system clock time in seconds. Return the time from the
     * FPGA hardware clock in seconds since the FPGA started.
     *
     * @return Robot running time in seconds.
     */
    double getFPGATimestamp();
    
    /**
     * Return the approximate match time
     * The FMS does not currently send the official match time to the robots
     * This returns the time since the enable signal sent from the Driver Station
     * At the beginning of autonomous, the time is reset to 0.0 seconds
     * At the beginning of teleop, the time is reset to +15.0 seconds
     * If the robot is disabled, this returns 0.0 seconds
     * Warning: This is not an official time (so it cannot be used to argue with referees)
     * @return Match time in seconds since the beginning of autonomous
     */
    double getMatchTime();
    
    /**
     * Pause the thread for a specified time. Pause the execution of the
     * thread for a specified period of time given in seconds. Motors will
     * continue to run at their last assigned values, and sensors will continue
     * to update. Only the task containing the wait will pause until the wait
     * time is expired.
     *
     * @param seconds Length of time to pause
     */
    void delay(final double seconds);
    
    /**
     * Create a new Timer for use with the Timer
     * 
     * 
     * @return new TimerInterface timer
     */
    TimerInterface newTimer();
}
