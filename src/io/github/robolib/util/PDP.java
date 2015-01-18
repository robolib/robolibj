/*
' * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package io.github.robolib.util;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.util.ArrayList;
import java.util.List;

import io.github.robolib.exception.ResourceAllocationException;
import io.github.robolib.framework.RoboLibBot;
import io.github.robolib.hal.PDPJNI;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * A better class for accessing the Power Distribution Panel.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class PDP {
    
    public static enum PowerChannel{
        Channel0,
        Channel1,
        Channel2,
        Channel3,
        Channel4,
        Channel5,
        Channel6,
        Channel7,
        Channel8,
        Channel9,
        Channel10,
        Channel11,
        Channel12,
        Channel13,
        Channel14,
        Channel15;
    }
    
    /** Number of PDP channels. **/
    public static final int kNumPowerChannels = 16;
    
    /** The Constant m_instance. */
    private static PDP m_instance;

    /** Keep track of already used channels. */
    private static boolean m_usedChannels[] = new boolean[kNumPowerChannels];
    
    /** */
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
     * Instantiates a PDP
     */
    private PDP(){
        m_table = RoboLibBot.getRobotTable().getSubTable("Power").getSubTable("PDP");
        updateTable();
    }
    
    /** The m_chan names. */
    private static String m_chanNames[] = new String[kNumPowerChannels];
    
    /**
     * Gets the channel name.
     *
     * @param channel the channel
     * @return the channel name
     */
    public static String getChannelName(PowerChannel channel){
//        checkPDPChannel(channel);
        return m_chanNames[channel.ordinal()];
    }
    
    /**
     * Sets the channel name.
     *
     * @param channel the channel
     * @param name the name
     */
    public static void claimChannel(PowerChannel channel, String name){

        if(m_usedChannels[channel.ordinal()] == false){
            m_usedChannels[channel.ordinal()] = true;
        }else{
            throw new ResourceAllocationException("PDP channel '" + channel.name() + "' already claimed by '" + m_chanNames[channel.ordinal()] + "'.");
        }
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
     * Gets the current
     * 
     * @param channel
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
    
    /**
     * Update the table for this object with the latest values.
     */
    public void updateTable() {
        for(int i : m_updateChannels){
            m_table.putString(m_chanNames[i], StringUtils.getNumber2DWithUnits(getChannelCurrent(i), "A"));
        }
        
        m_table.putString("Voltage", StringUtils.getNumber2DWithUnits(getVoltage(), "V"));
        m_table.putString("TotalCurrent", StringUtils.getNumber2DWithUnits(getTotalCurrent(), "A"));
        m_table.putString("Temperature", StringUtils.getNumber2DWithUnits(getTemperature(), "C"));
        m_table.putString("Total Energy Usage", StringUtils.getNumber2DWithUnits(getTotalEnergy(), "J"));
        m_table.putString("Total Power Usage", StringUtils.getNumber2DWithUnits(getTotalPower(), "A"));
        
    }
}
