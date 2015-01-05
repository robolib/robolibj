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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.team2583.robolib.robot.RoboLibBot;

import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * A better class for accessing the Power Distribution Panel
 * 
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public class PDP extends SensorBase implements LiveWindowSendable{
    
    private ITable m_table;
    private static IntBuffer status;
    
    public static final PDP m_instance = new PDP();
    
    public static PDP getInstance(){
        return m_instance;
    }
    
    private PDP(){
        initTable(RoboLibBot.getRobotTable().getSubTable("PDP"));
        ByteBuffer b = ByteBuffer.allocateDirect(4);
        b.order(ByteOrder.LITTLE_ENDIAN);
        status = b.asIntBuffer();
    }
    
    private static String m_chanNames[] = {
        "Chan0", "Chan1", "Chan2", "Chan3",
        "Chan4", "Chan5", "Chan6", "Chan7",
        "Chan8", "Chan9", "Chan10", "Chan11",
        "Chan12", "Chan13", "Chan14", "Chan15"};
    
    public static String getChannelName(int channel){
        checkPDPChannel(channel);
        return m_chanNames[channel];
    }
    
    public static void setChannelName(int channel, String name){
        checkPDPChannel(channel);
        m_chanNames[channel] = name;
    }

    
    public static double getVoltage(){
        return PDPJNI.getPDPVoltage(status);
    }
    
    public static double getTemperature(){
        return PDPJNI.getPDPTemperature(status);
    }
    
    public static double getCurrent(int channel){
        checkPDPChannel(channel);
        return PDPJNI.getPDPChannelCurrent((byte)channel, status);
    }
    
    public static double getTotalCurrent(){
        return PDPJNI.getPDPTotalCurrent(status);
    }
    
    
    public static double getTotalPower(){
        return PDPJNI.getPDPTotalPower(status);
    }
    
    public static double getTotalEnergy(){
        return PDPJNI.getPDPTotalEnergy(status);
    }
    
    public static void resetTotalEnergy(){
        PDPJNI.resetPDPTotalEnergy(status);   
    }
    
    public static void resetFaults(){
        PDPJNI.clearPDPStickyFaults(status);
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
