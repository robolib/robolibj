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

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Limit Switch Class.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class LimitSwitch implements IStandardSwitch, LiveWindowSendable {
    
    /** The m_limit switch. */
    private DigitalInput m_limitSwitch;
    
    /** The m_type. */
    private ESwitchType m_type;
    
    /**
     * Instantiates a new limit switch.
     *
     * @param channel the channel
     * @param type the type
     */
    public LimitSwitch(int channel, ESwitchType type){
        m_limitSwitch = new DigitalInput(channel);
        this.m_type = type;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean state() {
        return m_type.getValue(m_limitSwitch.get());
    }

    /**
     * {@inheritDoc}
     */
    public int getChannel() {
        return m_limitSwitch.getChannel();
    }

    /**
     * {@inheritDoc}
     */
    public ESwitchType getType() {
        return m_type;
    }

    /**
     * {@inheritDoc}
     */
    public void updateTable() {
        m_limitSwitch.updateTable();
    }

    /**
     * {@inheritDoc}
     */
    public void startLiveWindowMode() {
        m_limitSwitch.startLiveWindowMode();
    }

    /**
     * {@inheritDoc}
     */
    public void stopLiveWindowMode() {
        m_limitSwitch.stopLiveWindowMode();
    }

    /**
     * {@inheritDoc}
     */
    public void initTable(ITable arg0) {
        m_limitSwitch.initTable(arg0);
    }

    /**
     * {@inheritDoc}
     */
    public ITable getTable() {
        return m_limitSwitch.getTable();
    }

    /**
     * {@inheritDoc}
     */
    public String getSmartDashboardType() {
        return m_limitSwitch.getSmartDashboardType();
    }

}
