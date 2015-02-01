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

package io.github.robolib.input.limitswitch;

import io.github.robolib.iface.DigitalIO.DigitalChannel;
import io.github.robolib.input.limitswitch.LimitSwitch.SwitchType;

/**
 * A Limit Switch System using two Switches.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class DualLimitSwitchSystem implements LimitSwitchSystem{
    
    /** The m_bottom switch. */
    private LimitSwitch m_topSwitch, m_bottomSwitch;

    /**
     * Instantiates a new dual limit switch system.
     *
     * @param forwardLimitChannel the top channel
     * @param forwardLimitType the top type
     * @param reverseLimitChannel the bottom channel
     * @param reverseLimitType the bottom type
     */
    public DualLimitSwitchSystem(DigitalChannel forwardLimitChannel, SwitchType forwardLimitType,
            DigitalChannel reverseLimitChannel, SwitchType reverseLimitType){
        this(new LimitSwitch(forwardLimitChannel, forwardLimitType),
                new LimitSwitch(reverseLimitChannel, reverseLimitType));
    }
    
    /**
     * Instantiates a new dual limit switch system.
     *
     * @param top the top
     * @param bottom the bottom
     */
    public DualLimitSwitchSystem(LimitSwitch top, LimitSwitch bottom){
        m_topSwitch = top;
        m_bottomSwitch = bottom;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canForward() {
        return !m_topSwitch.state();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canReverse() {
        return !m_bottomSwitch.state();
    }

}