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

package org.team2583.robolib.pneumatics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.team2583.robolib.robot.RoboLibBot;

import edu.wpi.first.wpilibj.Resource;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.hal.CompressorJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.SolenoidJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * A better class for accessing the Pneumatics Control Module
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public class PCM extends SensorBase implements LiveWindowSendable{
    
    private ITable m_table;
    private static ByteBuffer m_pcm_compressor;
    private static ByteBuffer[] m_ports;
    private static IntBuffer m_status;
    protected Resource m_allocated = new Resource(63* SensorBase.kSolenoidChannels);
    
    private static final PCM m_instance = new PCM();
    
    public static PCM getInstance(){
        return m_instance;
    }
    
    private PCM(){
        initTable(RoboLibBot.getRobotTable().getSubTable("PCM"));
        ByteBuffer b = ByteBuffer.allocateDirect(4);
        m_ports = new ByteBuffer[SensorBase.kSolenoidChannels];
        for(int i = 0; i < SensorBase.kSolenoidChannels; i++){
            ByteBuffer port = SolenoidJNI.getPortWithModule((byte) 0, (byte) i);
            IntBuffer status = IntBuffer.allocate(1);
            m_ports[i] = SolenoidJNI.initializeSolenoidPort(port, status);
            HALUtil.checkStatus(status);
        }
        b.order(ByteOrder.LITTLE_ENDIAN);
        m_status = b.asIntBuffer();
        m_pcm_compressor = CompressorJNI.initializeCompressor((byte)0);
    }
    
    public static double getCompressorCurrent(){
        float current = CompressorJNI.getCompressorCurrent(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);   
        return (double)current;
    }
    
    public static void enableCompressor(boolean on){
        CompressorJNI.setClosedLoopControl(m_pcm_compressor, on, m_status);
        HALUtil.checkStatus(m_status);
    }
    
    public static boolean getCompressorEnabled(){
        boolean on = CompressorJNI.getClosedLoopControl(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return on;
    }
    
    public static boolean getPressureSwitch(){
        boolean on = CompressorJNI.getPressureSwitch(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return on;
    }
    
    public static boolean getCurrentFault(){
        boolean retval = CompressorJNI.getCompressorCurrentTooHighFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    public static boolean getCurrentStickyFault(){
        boolean retval = CompressorJNI.getCompressorCurrentTooHighStickyFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    public static boolean getShortFault(){
        boolean retval = CompressorJNI.getCompressorShortedFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    public static boolean getShortStickyFault(){
        boolean retval = CompressorJNI.getCompressorShortedStickyFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    public static boolean getNoConnectionFault(){
        boolean retval = CompressorJNI.getCompressorNotConnectedFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    public static boolean getNoConnectionStickyFault(){
        boolean retval = CompressorJNI.getCompressorNotConnectedStickyFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    public static void clearCompressorStickyFaults(){
        CompressorJNI.clearAllPCMStickyFaults(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
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
        return "Pneumatics";
    }

    /**
     * {@inheritDoc}
     */
    public void updateTable() {
        if (m_table != null) {
            m_table.putBoolean("Enabled", getCompressorEnabled());
            m_table.putBoolean("Pressure Switch", getPressureSwitch());
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
