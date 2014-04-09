package org.warriors2583.lib.controller;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.parsing.IDeviceController;
import org.warriors2583.lib.limitswitch.ILimitSwitchSystem;

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