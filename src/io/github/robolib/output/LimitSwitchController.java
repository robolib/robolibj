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

import io.github.robolib.input.limitswitch.LimitSwitchSystem;
import io.github.robolib.pid.PIDOutput;

/**
 * A SpeedController limited by Limit Switches.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitSwitchController implements SpeedController, PIDOutput {
    
    /** The m_motor. */
    private final SpeedController m_motor;
    
    /** The m_switch system. */
    private final LimitSwitchSystem m_switchSystem;
    
    private final MotorSafetyHelper m_safetyHelper;
    
    /**
     * Instantiates a new limit switch controller.
     *
     * @param motor the motor
     * @param switchSystem the switch system
     */
    public LimitSwitchController(SpeedController motor, LimitSwitchSystem switchSystem){
        m_motor = motor;
        m_switchSystem = switchSystem;
        m_safetyHelper = new MotorSafetyHelper(this);
    }

    /**
     * {@inheritDoc}
     */
    public double get() {
        return m_motor.get();
    }

    /**
     * {@inheritDoc}
     */
    public void set(double speed, byte syncGroup) {
        speed = speed > 0 && !m_switchSystem.canForward() ? 0.00 : speed;
        speed = speed < 0 && !m_switchSystem.canReverse() ? 0.00 : speed;
        m_motor.set(speed, syncGroup);
    }

    /**
     * {@inheritDoc}
     */
    public void set(double speed) {
        if(speed > 0)
        speed = speed > 0 && !m_switchSystem.canForward() ? 0.00 : speed;
        speed = speed < 0 && !m_switchSystem.canReverse() ? 0.00 : speed;
        m_motor.set(speed);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSpeed(double speed) {
        set(speed);
    }
    
    public void setInverted(boolean inverted){
        m_motor.setInverted(inverted);
    }
    
    /**
     * At a limit.
     *
     * @return true, if at a limit
     */
    public boolean atLimit(){
        return atForwardLimit() || atReverseLimit();
    }
    
    /**
     * At far limit.
     * 
     * @return true, if at far limit.
     */
    public boolean atForwardLimit(){
        return !m_switchSystem.canForward();
    }
    
    /**
     * At near limit
     * 
     * @return true, if at near limit.
     */
    public boolean atReverseLimit(){
        return !m_switchSystem.canReverse();
    }

    /**
     * {@inheritDoc}
     */
    public void pidWrite(double output) {
        m_motor.pidWrite(output);
    }

    /**
     * {@inheritDoc}
     */
    public MotorSafetyHelper getSafetyHelper() {
        return m_safetyHelper;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "LimitSwitchController";
    }
}