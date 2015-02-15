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

import io.github.robolib.SafetyManager;
import io.github.robolib.module.LimitSystem;
import io.github.robolib.module.MotorSafety;
import io.github.robolib.module.MotorSafetyHelper;

/**
 * A SpeedController limited by Boolean Sources.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class LimitedController implements SpeedController, MotorSafety {
    
    /** The m_motor. */
    private final SpeedController m_motor;
    
    /** The m_switch system. */
    private final LimitSystem m_system;
    
    private final MotorSafetyHelper m_safetyHelper;
    
    /**
     * Instantiates a new limit switch controller.
     *
     * @param motor the motor
     * @param system the switch system
     */
    public LimitedController(SpeedController motor, LimitSystem system){
        m_motor = motor;
        m_system = system;
        m_safetyHelper = SafetyManager.addMotor(this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double getSpeed() {
        return m_motor.getSpeed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpeed(double speed) {
        if(speed > 0 && m_system.canForward())
            m_motor.setSpeed(speed);
        else if(speed < 0 && m_system.canReverse())
            m_motor.setSpeed(speed);
        else
            m_motor.setSpeed(0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setInverted(boolean inverted){
        m_motor.setInverted(inverted);
    }
    
    /**
     * At a limit.
     *
     * @return true, if at a limit
     */
    public boolean atLimit(){
        return atFrontLimit() || atBackLimit();
    }
    
    /**
     * At far limit.
     * 
     * @return true, if at far limit.
     */
    public boolean atFrontLimit(){
        return !m_system.canForward();
    }
    
    /**
     * At near limit
     * 
     * @return true, if at near limit.
     */
    public boolean atBackLimit(){
        return !m_system.canReverse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MotorSafetyHelper getSafetyHelper() {
        return m_safetyHelper;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void stopMotor(){       
        m_motor.stopMotor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "LimitSwitchController";
    }
}