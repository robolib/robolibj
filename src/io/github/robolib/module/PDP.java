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

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.util.ArrayList;
import java.util.List;

import io.github.robolib.identifier.Sendable;
import io.github.robolib.jni.PDPJNI;
import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.nettable.ITable;
import io.github.robolib.util.StringUtils;

/**
 * A class for accessing the Power Distribution Panel.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PDP implements Sendable {
    
    /**
     * The Enum PowerChannel.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum PowerChannel {
        
        /** Channel 0 on the Power Distribution Panel. */
        Channel0,
        
        /** Channel 1 on the Power Distribution Panel. */
        Channel1,
        
        /** Channel 2 on the Power Distribution Panel. */
        Channel2,
        
        /** Channel 3 on the Power Distribution Panel. */
        Channel3,
        
        /** Channel 4 on the Power Distribution Panel. */
        Channel4,
        
        /** Channel 5 on the Power Distribution Panel. */
        Channel5,
        
        /** Channel 6 on the Power Distribution Panel. */
        Channel6,
        
        /** Channel 7 on the Power Distribution Panel. */
        Channel7,
        
        /** Channel 8 on the Power Distribution Panel. */
        Channel8,
        
        /** Channel 9 on the Power Distribution Panel. */
        Channel9,
        
        /** Channel 10 on the Power Distribution Panel. */
        Channel10,
        
        /** Channel 11 on the Power Distribution Panel. */
        Channel11,
        
        /** Channel 12 on the Power Distribution Panel. */
        Channel12,
        
        /** Channel 13 on the Power Distribution Panel. */
        Channel13,
        
        /** Channel 14 on the Power Distribution Panel. */
        Channel14,
        
        /** Channel 15 on the Power Distribution Panel. */
        Channel15;
    }
    
    /** Number of PDP channels. **/
    public static final int kNumPowerChannels = 16;
    
    /** The Constant m_instance. */
    private static PDP m_instance;
    
    /** The m_update channels. */
    private static List<Integer> m_updateChannels = new ArrayList<>();
    
    /** The m_table. */
    private ITable m_table;
    
    /**
     * Gets the single instance of PDP.
     *
     * @return single instance of PDP
     */
    public static PDP getInstance(){
        return m_instance == null ? m_instance = new PDP() : m_instance;
    }
    
    /**
     * Instantiates a PDP.
     */
    private PDP(){}
    
    /** The m_chan names. */
    private static String m_chanNames[] = new String[kNumPowerChannels];
    
    /**
     * Gets the channel name.
     *
     * @param channel the channel
     * @return the channel name
     */
    public static String getChannelName(PowerChannel channel){
        return m_chanNames[channel.ordinal()];
    }
    
    /**
     * Sets the channel name.
     *
     * @param channel the channel
     * @param name the name
     */
    public static void claimChannel(PowerChannel channel, String name){

        if(m_updateChannels.contains(channel.ordinal()))
            throw new ResourceAllocationException("PDP channel '" + channel.name() + "' already claimed by '" + m_chanNames[channel.ordinal()] + "'.");
        
        m_chanNames[channel.ordinal()] = name;
        m_updateChannels.add(channel.ordinal());
    }
    
    /**
     * Gets the voltage.
     *
     * @return the voltage
     */
    public static double getVoltage(){
        return PDPJNI.getPDPVoltage(getLE4IntBuffer());
    }
    
    /**
     * Gets the temperature.
     *
     * @return the temperature
     */
    public static double getTemperature(){
        return PDPJNI.getPDPTemperature(getLE4IntBuffer());
    }
    
    /**
     * Gets the current.
     *
     * @param channel the channel
     * @return the current
     */
    public static double getCurrent(PowerChannel channel){
        return PDPJNI.getPDPChannelCurrent((byte)channel.ordinal(), getLE4IntBuffer());
    }

    /**
     * Gets the current.
     *
     * @param channel the channel
     * @return the current
     */
    private double getChannelCurrent(int channel){
        return PDPJNI.getPDPChannelCurrent((byte)channel, getLE4IntBuffer());
    }
    
    /**
     * Gets the total current.
     *
     * @return the total current
     */
    public static double getTotalCurrent(){
        return PDPJNI.getPDPTotalCurrent(getLE4IntBuffer());
    }
    
    /**
     * Gets the total power.
     *
     * @return the total power
     */
    public static double getTotalPower(){
        return PDPJNI.getPDPTotalPower(getLE4IntBuffer());
    }
    
    /**
     * Gets the total energy.
     *
     * @return the total energy
     */
    public static double getTotalEnergy(){
        return PDPJNI.getPDPTotalEnergy(getLE4IntBuffer());
    }
    
    /**
     * Reset total energy.
     */
    public static void resetTotalEnergy(){
        PDPJNI.resetPDPTotalEnergy(getLE4IntBuffer());   
    }
    
    /**
     * Reset faults.
     */
    public static void resetFaults(){
        PDPJNI.clearPDPStickyFaults(getLE4IntBuffer());
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
        m_updateChannels.forEach(i -> m_table.putString(m_chanNames[i],
                StringUtils.getNumber2DWithUnits(getChannelCurrent(i), "A")));
        
        m_table.putString("Voltage", StringUtils.getNumber2DWithUnits(getVoltage(), "V"));
        m_table.putString("TotalCurrent", StringUtils.getNumber2DWithUnits(getTotalCurrent(), "A"));
        m_table.putString("Temperature", StringUtils.getNumber2DWithUnits(getTemperature(), "C"));
        m_table.putString("Total Energy Usage", StringUtils.getNumber2DWithUnits(getTotalEnergy(), "J"));
        m_table.putString("Total Power Usage", StringUtils.getNumber2DWithUnits(getTotalPower(), "A"));
        
    }
}
