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

package io.github.robolib.input.limitswitch;

/**
 * A Limit Switch System using only one Switch.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class SingleLimitSwitchSystem implements ILimitSwitchSystem{
    
   
    /** The m_limit switch. */
    private LimitSwitch m_limitSwitch;
    
    /** The m_type. */
    private ESingleSystemType m_type;
    
    /**
     * Construct a new Single Limit Switch System for use with a LimitSwitchController.
     *
     * @param channel Limit Switch Channel
     * @param switchType Limit Switch Type (SwitchType)
     * @param systemType Single Switch System Type (SingleSystemType)
     */ 
    public SingleLimitSwitchSystem(int channel, ESwitchType switchType, ESingleSystemType systemType){
        this(new LimitSwitch(channel, switchType), systemType);
    }
    
    /**
     * Construct a new Single Limit Switch System for use with a LimitSwitchController.
     *
     * @param limitSwitch Limit Switch
     * @param type Single Switch System Type (SingleSystemType)
     */
    public SingleLimitSwitchSystem(LimitSwitch limitSwitch, ESingleSystemType type){
        this.m_limitSwitch = limitSwitch;
        this.m_type = type;
    }
    
    
    /**
     * Returns if the top limit switch has been triggered or not.
     *
     * @return LimitSwitch value
     */
    public boolean canUp() {
        if(m_type == ESingleSystemType.BOTTOM_LIMIT) return true;
        return !m_limitSwitch.state();
    }
    
    /**
     * Returns if the bottom limit switch has been triggered or not.
     *
     * @return LimitSwitch value
     */
    public boolean canDown() {
        if(m_type == ESingleSystemType.TOP_LIMIT) return true;
        return !m_limitSwitch.state();
    }

}
