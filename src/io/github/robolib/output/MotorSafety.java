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

/**
 * The Interface MotorSafety.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface MotorSafety {
    
    /** The Constant SAFETY_TIMEOUT_DEFAULT. */
    public static final double SAFETY_TIMEOUT_DEFAULT = 0.1;
    
    /** The Constant SAFETY_TIMEOUT_STRICT. */
    public static final double SAFETY_TIMEOUT_STRICT = 0.05;
    
    /** The Constant SAFETY_TIMEOUT_LOOSE. */
    public static final double SAFETY_TIMEOUT_LOOSE = 0.5;
    
    /**
     * Return the MotorSafetyHelper object for this MotorSafety Object
     * 
     * @return the motor safety helper
     */
    MotorSafetyHelper getSafetyHelper();
    
    /**
     * Feed the safety helper
     */
    public default void feed(){
        getSafetyHelper().feed();
    }
    
    /**
     * Checks if is alive.
     *
     * @return true, if is alive
     */
    public default boolean isAlive(){
        return getSafetyHelper().isAlive();
    }
    
    /**
     * Sets the safety expiration.
     *
     * @param timeout the new safety expiration
     */
    public default void setSafetyExpiration(double timeout){
        getSafetyHelper().setExpiration(timeout);
    }
    
    /**
     * Gets the safety expiration.
     *
     * @return the safety expiration
     */
    public default double getSafetyExpiration(){
        return getSafetyHelper().getExpiration();
    }
    
    /**
     * Stop motor.
     */
    void stopMotor();
    
    /**
     * Enable safety.
     *
     * @param enabled the enabled
     */
    public default void setSafetyEnabled(boolean enabled){
        getSafetyHelper().setSafetyEnabled(enabled);
    }
    
    /**
     * Checks if is safety enabled.
     *
     * @return true, if is safety enabled
     */
    public default boolean isSafetyEnabled(){
        return getSafetyHelper().isSafetyEnabled();
    }
    
    /**
     * Gets the description.
     *
     * @return the description
     */
    String getDescription();
}
