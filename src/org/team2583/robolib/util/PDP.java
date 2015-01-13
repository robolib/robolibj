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

package org.team2583.robolib.util;

import static org.team2583.robolib.util.CommonFunctions.getLE4IntBuffer;

import org.team2583.robolib.robot.RoboLibBot;

import org.team2583.robolib.hal.PDPJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * A better class for accessing the Power Distribution Panel.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PDP implements LiveWindowSendable{
    
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
    
    /** The m_table. */
    private ITable m_table;
    
    /** The Constant m_instance. */
    public static final PDP m_instance = new PDP();
    
    /**
     * Gets the single instance of PDP.
     *
     * @return single instance of PDP
     */
    public static PDP getInstance(){
        return m_instance;
    }
    
    /**
     * Instantiates a new pdp.
     */
    private PDP(){
        initTable(RoboLibBot.getRobotTable().getSubTable("PDP"));
    }
    
    /** The m_chan names. */
    private static String m_chanNames[] = {
        "Chan0", "Chan1", "Chan2", "Chan3",
        "Chan4", "Chan5", "Chan6", "Chan7",
        "Chan8", "Chan9", "Chan10", "Chan11",
        "Chan12", "Chan13", "Chan14", "Chan15"};
    
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
    public static void setChannelName(PowerChannel channel, String name){
//        checkPDPChannel(channel);
        m_chanNames[channel.ordinal()] = name;
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
//        checkPDPChannel(channel);
        return getCurrent(channel.ordinal());
    }
    
    public static double getCurrent(int channel){
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
     * {@inheritDoc}
     */
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    public String getSmartDashboardType() {
        return "PowerDistributionPanel";
    }

    /**
     * {@inheritDoc}
     */
    public void updateTable() {
        if (m_table != null) {
            for(int i = 0; i < m_chanNames.length; i++)
                m_table.putNumber(m_chanNames[i], getCurrent(i));
            
            m_table.putNumber("Voltage", getVoltage());
            m_table.putNumber("TotalCurrent", getTotalCurrent());
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public void startLiveWindowMode() {
        
    }

    /**
     * {@inheritDoc}
     */
    public void stopLiveWindowMode() {
        
    }
    

}
