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

package io.robolib.pneumatic;

import java.nio.ByteBuffer;

import io.robolib.util.log.Logger;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The Class Solenoid.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class Solenoid extends SolenoidBase implements LiveWindowSendable {
    
    private ByteBuffer m_port;
    private SolenoidChannel m_channel;
    
    public Solenoid(SolenoidChannel channel){
        m_channel = channel;
        m_port = initChannel(channel);
        
    }
    
    public SolenoidChannel getChannel(){
        return m_channel;
    }
    
    /**
     * {@inheritDoc}
     */
    public void set(Value value){
        switch(value){
        case OFF:
            set(m_port, kSolenoidOff);
            break;
        case ON:
            set(m_port, kSolenoidOn);
            break;
        case FORWARD:
            Logger.get(SolenoidBase.class, "Solenoid").warn("Single solenoid cannot go Forward!");
            break;
        case REVERSE:
            Logger.get(SolenoidBase.class, "Solenoid").warn("Single solenoid cannot go in Reverse!");
            break;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Value get(){
        return get(m_port) ? Value.ON : Value.OFF;
    }

    /**
     * {@inheritDoc}
     */
    public void initTable(ITable subtable) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    public ITable getTable() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getSmartDashboardType() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void updateTable() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    public void startLiveWindowMode() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    public void stopLiveWindowMode() {
        // TODO Auto-generated method stub
        
    }

}
