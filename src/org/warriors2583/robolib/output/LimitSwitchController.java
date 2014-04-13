/*
 * Copyright (c) 2014 Westwood Robotics code.westwoodrobotics@gmail.com.
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

package org.warriors2583.robolib.output;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.parsing.IDeviceController;
import org.warriors2583.robolib.input.limitswitch.ILimitSwitchSystem;

/**
 * A SpeedController limited by Limit Switches
 * @author Austin Reuland
 */
public class LimitSwitchController implements SpeedController, IDeviceController, PIDOutput {
    
    private final SpeedController m_motor;
    private final ILimitSwitchSystem m_switchSystem;
    
    public LimitSwitchController(SpeedController motor, ILimitSwitchSystem switchSystem){
        this.m_motor = motor;
        this.m_switchSystem = switchSystem;
    }

    public double get() {
        return m_motor.get();
    }
    
    private double speed = 0;

    public void set(double speed, byte syncGroup) {
        speed = speed > 0 && !m_switchSystem.canUp() ? 0.00 : speed;
        speed = speed < 0 && !m_switchSystem.canDown() ? 0.00 : speed;
        m_motor.set(speed, syncGroup);
        this.speed = speed;
    }

    public void set(double speed) {
        speed = speed > 0 && !m_switchSystem.canUp() ? 0.00 : speed;
        speed = speed < 0 && !m_switchSystem.canDown() ? 0.00 : speed;
        m_motor.set(speed);
        this.speed = speed;
    }
    
    public boolean atLimit(){
        if(speed >= 0 && !m_switchSystem.canUp()) return true;
        return speed < 0 && !m_switchSystem.canDown();
    }

    public void disable() {
        m_motor.disable();
    }

    public void pidWrite(double output) {
        m_motor.pidWrite(output);
    }
}