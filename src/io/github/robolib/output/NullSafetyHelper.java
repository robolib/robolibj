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
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class NullSafetyHelper extends MotorSafetyHelper {
    
    public NullSafetyHelper(MotorSafety s){
        super(s);
    }
    
    /**
     * Feed.
     */
    @Override
    public void feed(){}
    
    /**
     * Enable safety.
     *
     * @param enabled the enabled
     */
    @Override
    public void setSafetyEnabled(boolean enabled){}
    
    /**
     * Checks if is safety enabled.
     *
     * @return true, if is safety enabled
     */
    @Override
    public boolean isSafetyEnabled(){
        return false;
    }
    
    /**
     * Sets the expiration.
     *
     * @param exp the new expiration
     */
    @Override
    public void setExpiration(double exp){}
    
    /**
     * Gets the expiration.
     *
     * @return the expiration
     */
    @Override
    public double getExpiration(){
        return 0.0;
    }
    
    /**
     * Checks if is alive.
     *
     * @return true, if is alive
     */
    @Override
    public boolean isAlive(){
        return true;
    }
    
    /**
     * Check.
     */
    @Override
    public void check(){}
}
