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
import java.util.HashMap;
import java.util.Map;

import org.team2583.robolib.robot.RoboLibBot;

import edu.wpi.first.wpilibj.Resource;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.hal.CompressorJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.SolenoidJNI;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * A better class for accessing the Pneumatics Control Module.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PCM extends SensorBase implements LiveWindowSendable{
    
    /** The m_table. */
    private ITable m_table;
    
    /** The m_pcm_compressor. */
    private static ByteBuffer m_pcm_compressor;
    
    /** The m_map_module_ports. */
    private static Map<Integer, ByteBuffer[]> m_map_module_ports = new HashMap<Integer, ByteBuffer[]>();
    
    /** The m_status. */
    private static IntBuffer m_status;
    
    /** The m_allocated. */
    protected Resource m_allocated = new Resource(63* SensorBase.kSolenoidChannels);
    
    /** The Constant m_instance. */
    private static final PCM m_instance = new PCM();
    
    /**
     * Gets the single instance of PCM.
     *
     * @return single instance of PCM
     */
    public static PCM getInstance(){
        return m_instance;
    }
    
    /**
     * Instantiates a new pcm.
     */
    private PCM(){
        initTable(RoboLibBot.getRobotTable().getSubTable("PCM"));
        ByteBuffer b = ByteBuffer.allocateDirect(4);
        b.order(ByteOrder.LITTLE_ENDIAN);
        m_status = b.asIntBuffer();
        m_pcm_compressor = CompressorJNI.initializeCompressor((byte)getDefaultSolenoidModule());
        initModule(getDefaultSolenoidModule());
    }
    
    /**
     * Inits the module.
     *
     * @param module the module
     */
    private void initModule(int module){
        ByteBuffer ports[] = new ByteBuffer[SensorBase.kSolenoidChannels];
        for(int i = 0; i < SensorBase.kSolenoidChannels; i++){
            ByteBuffer port = SolenoidJNI.getPortWithModule((byte)module, (byte) i);
            IntBuffer status = IntBuffer.allocate(1);
            ports[i] = SolenoidJNI.initializeSolenoidPort(port, status);
            HALUtil.checkStatus(status);
        }
        m_map_module_ports.put(module, ports);
    }
    
    /**
     * Set the value of a solenoid.
     *
     * @param module The PCM module the solenoid is on.
     * @param value The value you want to set on the module.
     * @param mask The channels you want to be affected.
     */
    public synchronized void set(int module, int value, int mask){
        
    }
    
    /**
     * Gets the compressor current.
     *
     * @return the compressor current
     */
    public static double getCompressorCurrent(){
        float current = CompressorJNI.getCompressorCurrent(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);   
        return (double)current;
    }
    
    /**
     * Enable compressor.
     *
     * @param on the on
     */
    public static void enableCompressor(boolean on){
        CompressorJNI.setClosedLoopControl(m_pcm_compressor, on, m_status);
        HALUtil.checkStatus(m_status);
    }
    
    /**
     * Gets the compressor enabled.
     *
     * @return the compressor enabled
     */
    public static boolean getCompressorEnabled(){
        boolean on = CompressorJNI.getClosedLoopControl(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return on;
    }
    
    /**
     * Gets the pressure switch.
     *
     * @return the pressure switch
     */
    public static boolean getPressureSwitch(){
        boolean on = CompressorJNI.getPressureSwitch(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return on;
    }
    
    /**
     * Gets the current fault.
     *
     * @return the current fault
     */
    public static boolean getCurrentFault(){
        boolean retval = CompressorJNI.getCompressorCurrentTooHighFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    /**
     * Gets the current sticky fault.
     *
     * @return the current sticky fault
     */
    public static boolean getCurrentStickyFault(){
        boolean retval = CompressorJNI.getCompressorCurrentTooHighStickyFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    /**
     * Gets the short fault.
     *
     * @return the short fault
     */
    public static boolean getShortFault(){
        boolean retval = CompressorJNI.getCompressorShortedFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    /**
     * Gets the short sticky fault.
     *
     * @return the short sticky fault
     */
    public static boolean getShortStickyFault(){
        boolean retval = CompressorJNI.getCompressorShortedStickyFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    /**
     * Gets the no connection fault.
     *
     * @return the no connection fault
     */
    public static boolean getNoConnectionFault(){
        boolean retval = CompressorJNI.getCompressorNotConnectedFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    /**
     * Gets the no connection sticky fault.
     *
     * @return the no connection sticky fault
     */
    public static boolean getNoConnectionStickyFault(){
        boolean retval = CompressorJNI.getCompressorNotConnectedStickyFault(m_pcm_compressor, m_status);
        HALUtil.checkStatus(m_status);
        return retval;
    }
    
    /**
     * Clear compressor sticky faults.
     */
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
