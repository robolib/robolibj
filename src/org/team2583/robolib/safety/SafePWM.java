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

package org.team2583.robolib.safety;

import org.team2583.robolib.iface.PWM;

/**
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 *
 */
public class SafePWM extends PWM implements MotorSafety {
    
    MotorSafetyHelper m_safetyHelper;
    
    public SafePWM(PWMChannel channel){
        super(channel);
        m_safetyHelper = MotorSafetyManager.addMotor(this);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAlive() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void setSafetyExpiration(double timeout) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    public double getSafetyExpiration() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public void stopMotor() {
        setRaw(kPWMDisabled);
    }

    /**
     * {@inheritDoc}
     */
    public void enableSafety(boolean enabled) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSafetyEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
