/*
 * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
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

import io.github.robolib.identifier.LiveWindowSendable;
import io.github.robolib.nettable.ITable;
import io.github.robolib.nettable.ITableListener;
import io.github.robolib.util.log.Logger;

/**
 * The Class DoubleSolenoid.
 *
 * @author noriah <vix@noriah.dev>
 */
public final class DoubleSolenoid extends SolenoidBase
        implements ActuatorModule, LiveWindowSendable {

    private final SolenoidChannel m_forwardChannel;
    private final SolenoidChannel m_reverseChannel;

    private final ByteBuffer m_forwardPort;
    private final ByteBuffer m_reversePort;

    private ITable m_table;
    private ITableListener m_table_listener;

    public DoubleSolenoid(SolenoidChannel forwardChannel, SolenoidChannel reverseChannel){
        m_forwardChannel = forwardChannel;
        m_reverseChannel = reverseChannel;

        m_forwardPort = initChannel(forwardChannel);
        m_reversePort = initChannel(reverseChannel);
    }

    public SolenoidChannel getForwardChannel(){
        return m_forwardChannel;
    }

    public SolenoidChannel getReverseChannel(){
        return m_reverseChannel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(Value value) {
        switch(value){
        case OFF:
            set(m_forwardPort, SOLENOID_OFF);
            set(m_reversePort, SOLENOID_OFF);
            break;
        case ON:
            Logger.get(SolenoidBase.class, "Solenoid").warn("Double Solenoid cant be 'ON'");
            break;
        case FORWARD:
            set(m_forwardPort, SOLENOID_ON);
            set(m_reversePort, SOLENOID_OFF);
            break;
        case REVERSE:
            set(m_forwardPort, SOLENOID_OFF);
            set(m_forwardPort, SOLENOID_ON);
            break;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value get() {
        boolean forward = get(m_forwardPort);
        boolean reverse = get(m_reversePort);

        if(forward){
            return Value.FORWARD;
        }else if(reverse){
            return Value.REVERSE;
        }else{
            return Value.OFF;
        }
    }

    /**
     * Check if the forward solenoid is blacklisted.
     *      If a solenoid is shorted, it is added to the blacklist and
     *      disabled until power cycle, or until faults are cleared.
     *      @see SolenoidBase#clearAllPCMStickyFaults(int)
     *
     * @return If solenoid is disabled due to short.
     */
    public boolean isFwdSolenoidBlackListed() {
        int blackList = getPCMSolenoidBlacklist(m_forwardChannel.ordinal() / 8) & (1 << m_forwardChannel.ordinal());
        return blackList != 0;
    }
    /**
     * Check if the reverse solenoid is blacklisted.
     *      If a solenoid is shorted, it is added to the blacklist and
     *      disabled until power cycle, or until faults are cleared.
     *      @see SolenoidBase#clearAllPCMStickyFaults(int)
     *
     * @return If solenoid is disabled due to short.
     */
    public boolean isRevSolenoidBlackListed() {
        int blackList = getPCMSolenoidBlacklist(m_reverseChannel.ordinal() / 8) & (1 << m_reverseChannel.ordinal());
        return blackList != 0;
    }

    /*
     * Live Window code, only does anything if live window is activated.
     */
    @Override
    public String getSmartDashboardType() {
        return "Double Solenoid";
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
            m_table.putString("Value", (get() == Value.FORWARD ? "Forward" : (get() == Value.REVERSE ? "Reverse" : "Off")));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startLiveWindowMode() {
        set(Value.OFF);
        m_table_listener = new ITableListener() {
            @Override
            public void valueChanged(ITable itable, String key, Object value, boolean bln) {
                if (value.toString().equalsIgnoreCase("Reverse"))
                    set(Value.REVERSE);
                else if (value.toString().equalsIgnoreCase("Forward"))
                    set(Value.FORWARD);
                else
                    set(Value.OFF);
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {
        set(Value.OFF);
        m_table.removeTableListener(m_table_listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableModule() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableModule() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeSafe() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getModuleEnabled() {
        // TODO Auto-generated method stub
        return true;
    }




}
