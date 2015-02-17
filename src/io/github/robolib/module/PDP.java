/*
' * Copyright (c) 2015 noriah <vix@noriah.dev>.
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

package io.github.robolib.module;

import static io.github.robolib.util.CommonFunctions.allocateInt;

import java.util.HashMap;
import java.util.Map;

import io.github.robolib.identifier.UpdatingSendable;
import io.github.robolib.jni.PDPJNI;
import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.nettable.ITable;

/**
 * A class for accessing the Power Distribution Panel.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class PDP implements UpdatingSendable {
    
    /**
     * The Enum PowerChannel.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum PowerChannel {
        
        /** Channel 0 on the Power Distribution Panel. */
        POWER0,
        
        /** Channel 1 on the Power Distribution Panel. */
        POWER1,
        
        /** Channel 2 on the Power Distribution Panel. */
        POWER2,
        
        /** Channel 3 on the Power Distribution Panel. */
        POWER3,
        
        /** Channel 4 on the Power Distribution Panel. */
        POWER4,
        
        /** Channel 5 on the Power Distribution Panel. */
        POWER5,
        
        /** Channel 6 on the Power Distribution Panel. */
        POWER6,
        
        /** Channel 7 on the Power Distribution Panel. */
        POWER7,
        
        /** Channel 8 on the Power Distribution Panel. */
        POWER8,
        
        /** Channel 9 on the Power Distribution Panel. */
        POWER9,
        
        /** Channel 10 on the Power Distribution Panel. */
        POWER10,
        
        /** Channel 11 on the Power Distribution Panel. */
        POWER11,
        
        /** Channel 12 on the Power Distribution Panel. */
        POWER12,
        
        /** Channel 13 on the Power Distribution Panel. */
        POWER13,
        
        /** Channel 14 on the Power Distribution Panel. */
        POWER14,
        
        /** Channel 15 on the Power Distribution Panel. */
        POWER15;
    }
    
    /** Number of PDP channels. **/
    public static final int MAX_POWER_CHANNELS = 16;
    
    /** The Constant m_instance. */
    private static PDP m_instance;
    
    /** The m_update channels. */
    private static final Map<Integer, String> CHANNEL_MAP =
            new HashMap<Integer, String>(MAX_POWER_CHANNELS);
    
    /** The m_table. */
    private ITable m_table;
    
    public static void initialize(){
        if(m_instance != null)
            throw new IllegalStateException("PDP already initialized.");
        
        m_instance = new PDP();
    }
    
    /**
     * Gets the single instance of PDP.
     *
     * @return single instance of PDP
     */
    public static PDP getInstance(){
        return m_instance;
    }
    
    /**
     * Instantiates a PDP.
     */
    private PDP(){}
    
    /**
     * Gets the channel name.
     *
     * @param channel the channel
     * @return the channel name
     */
    public static String getChannelName(PowerChannel channel){
        return CHANNEL_MAP.get(channel.ordinal());
    }
    
    /**
     * Sets the channel name.
     *
     * @param channel the channel
     * @param name the name
     */
    public static void claimChannel(PowerChannel channel, String name){

        if(CHANNEL_MAP.containsKey(channel.ordinal()))
            throw new ResourceAllocationException("PDP channel '"
                    + channel.name() + "' already claimed by '"
                    + CHANNEL_MAP.get(channel.ordinal()) + "'.");
        
        CHANNEL_MAP.put(channel.ordinal(),  name);
    }
    
    /**
     * Gets the voltage.
     *
     * @return the voltage
     */
    public static double getVoltage(){
        return PDPJNI.getPDPVoltage(allocateInt());
    }
    
    /**
     * Gets the temperature.
     *
     * @return the temperature
     */
    public static double getTemperature(){
        return PDPJNI.getPDPTemperature(allocateInt());
    }
    
    /**
     * Gets the current.
     *
     * @param channel the channel
     * @return the current
     */
    public static double getCurrent(PowerChannel channel){
        return PDPJNI.getPDPChannelCurrent((byte)channel.ordinal(), allocateInt());
    }

    /**
     * Gets the current.
     *
     * @param channel the channel
     * @return the current
     */
    private double getChannelCurrent(int channel){
        return PDPJNI.getPDPChannelCurrent((byte)channel, allocateInt());
    }
    
    /**
     * Gets the total current.
     *
     * @return the total current
     */
    public static double getTotalCurrent(){
        return PDPJNI.getPDPTotalCurrent(allocateInt());
    }
    
    /**
     * Gets the total power.
     *
     * @return the total power
     */
    public static double getTotalPower(){
        return PDPJNI.getPDPTotalPower(allocateInt());
    }
    
    /**
     * Gets the total energy.
     *
     * @return the total energy
     */
    public static double getTotalEnergy(){
        return PDPJNI.getPDPTotalEnergy(allocateInt());
    }
    
    /**
     * Reset total energy.
     */
    public static void resetTotalEnergy(){
        PDPJNI.resetPDPTotalEnergy(allocateInt());   
    }
    
    /**
     * Reset faults.
     */
    public static void resetFaults(){
        PDPJNI.clearPDPStickyFaults(allocateInt());
    }
    
    /*
     * Live Window code, only does anything if live window is activated.
     */
    @Override
    public String getSmartDashboardType() {
        return "Power Distribution Panel";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }
    
    /**
     * Update the table for this object with the latest values.
     */
    @Override
    public void updateTable() {
        CHANNEL_MAP.forEach((Integer i, String s) ->
            m_table.putNumber(s, getChannelCurrent(i)));
        
        m_table.putNumber("Voltage", getVoltage());
        m_table.putNumber("TotalCurrent", getTotalCurrent());
        m_table.putNumber("Temperature", getTemperature());
        m_table.putNumber("Total Energy Usage", getTotalEnergy());
        m_table.putNumber("Total Power Usage", getTotalPower());
             
    }
}
