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

package org.team2583.robolib.output;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import org.team2583.robolib.input.limitswitch.ILimitSwitchSystem;

/**
 * A SpeedController limited by Limit Switches.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class LimitSwitchController implements SpeedController, PIDOutput {
    
    /** The m_motor. */
    private final SpeedController m_motor;
    
    /** The m_switch system. */
    private final ILimitSwitchSystem m_switchSystem;
    
    /**
     * Instantiates a new limit switch controller.
     *
     * @param motor the motor
     * @param switchSystem the switch system
     */
    public LimitSwitchController(SpeedController motor, ILimitSwitchSystem switchSystem){
        this.m_motor = motor;
        this.m_switchSystem = switchSystem;
    }

    /**
     * {@inheritDoc}
     */
    public double get() {
        return m_motor.get();
    }
    
    /** The speed. */
    private double speed = 0;

    /**
     * {@inheritDoc}
     */
    public void set(double speed, byte syncGroup) {
        speed = speed > 0 && !m_switchSystem.canUp() ? 0.00 : speed;
        speed = speed < 0 && !m_switchSystem.canDown() ? 0.00 : speed;
        m_motor.set(speed, syncGroup);
        this.speed = speed;
    }

    /**
     * {@inheritDoc}
     */
    public void set(double speed) {
        speed = speed > 0 && !m_switchSystem.canUp() ? 0.00 : speed;
        speed = speed < 0 && !m_switchSystem.canDown() ? 0.00 : speed;
        m_motor.set(speed);
        this.speed = speed;
    }
    
    /**
     * At limit.
     *
     * @return true, if successful
     */
    public boolean atLimit(){
        if(speed >= 0 && !m_switchSystem.canUp()) return true;
        return speed < 0 && !m_switchSystem.canDown();
    }

    /**
     * {@inheritDoc}
     */
    public void disable() {
        m_motor.disable();
    }

    /**
     * {@inheritDoc}
     */
    public void pidWrite(double output) {
        m_motor.pidWrite(output);
    }
}