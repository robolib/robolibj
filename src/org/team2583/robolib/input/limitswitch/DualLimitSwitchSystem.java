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

package org.team2583.robolib.input.limitswitch;

/**
 * A Limit Switch System using two Switches
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class DualLimitSwitchSystem implements ILimitSwitchSystem{
    
    private LimitSwitch m_topSwitch, m_bottomSwitch;

    public DualLimitSwitchSystem(int topChannel, ESwitchType topType,
            int bottomChannel, ESwitchType bottomType){
        this(new LimitSwitch(topChannel, topType),
                new LimitSwitch(bottomChannel, bottomType));
    }
    
    public DualLimitSwitchSystem(LimitSwitch top, LimitSwitch bottom){
        m_topSwitch = top;
        m_bottomSwitch = bottom;
    }
    
    public boolean canUp() {
        return !m_topSwitch.state();
    }

    public boolean canDown() {
        return !m_bottomSwitch.state();
    }

}