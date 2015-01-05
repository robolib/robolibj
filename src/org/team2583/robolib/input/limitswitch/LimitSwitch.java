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

package org.team2583.robolib.input.limitswitch;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Limit Switch Class
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitSwitch implements IStandardSwitch, LiveWindowSendable {
    
    private DigitalInput m_limitSwitch;
    private ESwitchType m_type;
    
    public LimitSwitch(int channel, ESwitchType type){
        m_limitSwitch = new DigitalInput(channel);
        this.m_type = type;
    }
    
    public boolean state() {
        return m_type.getValue(m_limitSwitch.get());
    }

    public int getChannel() {
        return m_limitSwitch.getChannel();
    }

    public ESwitchType getType() {
        return m_type;
    }

    public void updateTable() {
        m_limitSwitch.updateTable();
    }

    public void startLiveWindowMode() {
        m_limitSwitch.startLiveWindowMode();
    }

    public void stopLiveWindowMode() {
        m_limitSwitch.stopLiveWindowMode();
    }

    public void initTable(ITable arg0) {
        m_limitSwitch.initTable(arg0);
    }

    public ITable getTable() {
        return m_limitSwitch.getTable();
    }

    public String getSmartDashboardType() {
        return m_limitSwitch.getSmartDashboardType();
    }

}
