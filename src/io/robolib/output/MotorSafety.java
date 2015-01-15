/*
 * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package io.robolib.output;

// TODO: Auto-generated Javadoc
/**
 * The Interface MotorSafety.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public interface MotorSafety {
    
    /** The Constant SAFETY_TIMEOUT_DEFAULT. */
    public static final double SAFETY_TIMEOUT_DEFAULT = 0.1;
    
    /** The Constant SAFETY_TIMEOUT_STRICT. */
    public static final double SAFETY_TIMEOUT_STRICT = 0.05;
    
    /** The Constant SAFETY_TIMEOUT_LOOSE. */
    public static final double SAFETY_TIMEOUT_LOOSE = 0.5;
    
    /**
     * Checks if is alive.
     *
     * @return true, if is alive
     */
    boolean isAlive();
    
    /**
     * Sets the safety expiration.
     *
     * @param timeout the new safety expiration
     */
    void setSafetyExpiration(double timeout);
    
    /**
     * Gets the safety expiration.
     *
     * @return the safety expiration
     */
    double getSafetyExpiration();
    
    /**
     * Stop motor.
     */
    void stopMotor();
    
    /**
     * Enable safety.
     *
     * @param enabled the enabled
     */
    void enableSafety(boolean enabled);
    
    /**
     * Checks if is safety enabled.
     *
     * @return true, if is safety enabled
     */
    boolean isSafetyEnabled();
    
    /**
     * Gets the description.
     *
     * @return the description
     */
    String getDescription();
}
