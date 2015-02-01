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

import io.github.robolib.framework.SafetyManager;
import io.github.robolib.util.LimitSystem;

/**
 * A SpeedController limited by {@link BooleanSource}(s).
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitedController implements SpeedController {
    
    /** The m_motor. */
    private final SpeedController m_motor;
    
    /** The m_switch system. */
    private final LimitSystem m_system;
    
    private final MotorSafetyHelper m_safetyHelper;
    
    /**
     * Instantiates a new limit switch controller.
     *
     * @param motor the motor
     * @param switchSystem the switch system
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
    public double get() {
        return m_motor.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(double speed) {
        speed = speed > 0 && !m_system.canForward() ? 0.00 : speed;
        speed = speed < 0 && !m_system.canReverse() ? 0.00 : speed;
        m_motor.set(speed);
    }
    
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
    public String getDescription() {
        return "LimitSwitchController";
    }
}