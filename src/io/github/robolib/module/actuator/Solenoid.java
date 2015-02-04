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

package io.github.robolib.module.actuator;

import java.nio.ByteBuffer;

import io.github.robolib.identifier.BooleanSink;
import io.github.robolib.identifier.LiveWindowSendable;
import io.github.robolib.util.log.Logger;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * The Class Solenoid.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Solenoid extends SolenoidBase implements LiveWindowSendable, BooleanSink {
    
    private ByteBuffer m_port;
    private SolenoidChannel m_channel;
    
    private ITable m_table;
    private ITableListener m_table_listener;
    
    public Solenoid(SolenoidChannel channel){
        m_channel = channel;
        m_port = initChannel(channel);
        
    }
    
    public int getChannelNumber(){
        return m_channel.ordinal();
    }
    
    public SolenoidChannel getChannel(){
        return m_channel;
    }
    
    /**
     * Set the solenoid with a boolean value.
     * @param value on or off
     */
    public void set(boolean value){
        set(m_port, value?kSolenoidOn:kSolenoidOff);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
    public Value get(){
        return get(m_port) ? Value.ON : Value.OFF;
    }
    
    public boolean getRaw(){
        return get(m_port);
    }
    
    /**
     * Check if solenoid is blacklisted.
     *      If a solenoid is shorted, it is added to the blacklist and
     *      disabled until power cycle, or until faults are cleared.
     *      @see SolenoidBase#clearAllPCMStickyFaults(int)
     *
     * @return If solenoid is disabled due to short.
     */
    public boolean isBlackListed(){
        int value = getPCMSolenoidBlacklist(m_channel.ordinal() / 8) & (1 << m_channel.ordinal());
        return value != 0;
    }

    /*
     * Live Window code, only does anything if live window is activated.
     */
    @Override
    public String getSmartDashboardType() {
        return "Solenoid";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTable() {
        if (m_table != null) {
            m_table.putBoolean("Value", getRaw());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void startLiveWindowMode() {
        m_table_listener = new ITableListener() {
            @Override
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                set(((Boolean) value).booleanValue());
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {
        set(false);
        m_table.removeTableListener(m_table_listener);
    }

}
