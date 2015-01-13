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

package org.team2583.robolib.pneumatic;

import static org.team2583.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.team2583.robolib.hal.CompressorJNI;
import org.team2583.robolib.hal.HALUtil;
import org.team2583.robolib.robot.RoboLibBot;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * A better class for accessing the Pneumatics Control Module.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public final class Compressor {
    
    /** The m_table. */
    private ITable m_table;
    
    /** The m_pcm_compressor. */
    private static ByteBuffer m_compressor;
    
    /** The Constant m_instance. */
    private static final Compressor m_instance = new Compressor();
    
    /**
     * Gets the single instance of PCM.
     *
     * @return single instance of PCM
     */
    public static Compressor getInstance(){
        return m_instance;
    }
    
    /**
     * Instantiates a new pcm.
     */
    private Compressor(){
        initTable(RoboLibBot.getRobotTable().getSubTable("PCM"));
        m_compressor = CompressorJNI.initializeCompressor((byte) 0);
    }
    
    public void free(){
        
    }
    
    /**
     * Gets the compressor current.
     *
     * @return the compressor current
     */
    public static double getCompressorCurrent(){
        IntBuffer status = getLE4IntBuffer();
        float current = CompressorJNI.getCompressorCurrent(m_compressor, status);
        HALUtil.checkStatus(status);   
        return (double)current;
    }
    
    /**
     * Enable compressor.
     *
     * @param on the on
     */
    public static void enableCompressor(boolean on){
        IntBuffer status = getLE4IntBuffer();
        CompressorJNI.setClosedLoopControl(m_compressor, on, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Gets the compressor enabled.
     *
     * @return the compressor enabled
     */
    public static boolean getCompressorEnabled(){
        IntBuffer status = getLE4IntBuffer();
        boolean on = CompressorJNI.getClosedLoopControl(m_compressor, status);
        HALUtil.checkStatus(status);
        return on;
    }
    
    /**
     * Gets the pressure switch.
     *
     * @return the pressure switch
     */
    public static boolean getPressureSwitch(){
        IntBuffer status = getLE4IntBuffer();
        boolean on = CompressorJNI.getPressureSwitch(m_compressor, status);
        HALUtil.checkStatus(status);
        return on;
    }
    
    /**
     * Gets the current fault.
     *
     * @return the current fault
     */
    public static boolean getCurrentFault(){
        IntBuffer status = getLE4IntBuffer();
        boolean retval = CompressorJNI.getCompressorCurrentTooHighFault(m_compressor, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the current sticky fault.
     *
     * @return the current sticky fault
     */
    public static boolean getCurrentStickyFault(){
        IntBuffer status = getLE4IntBuffer();
        boolean retval = CompressorJNI.getCompressorCurrentTooHighStickyFault(m_compressor, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the short fault.
     *
     * @return the short fault
     */
    public static boolean getShortFault(){
        IntBuffer status = getLE4IntBuffer();
        boolean retval = CompressorJNI.getCompressorShortedFault(m_compressor, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the short sticky fault.
     *
     * @return the short sticky fault
     */
    public static boolean getShortStickyFault(){
        IntBuffer status = getLE4IntBuffer();
        boolean retval = CompressorJNI.getCompressorShortedStickyFault(m_compressor, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the no connection fault.
     *
     * @return the no connection fault
     */
    public static boolean getNoConnectionFault(){
        IntBuffer status = getLE4IntBuffer();
        boolean retval = CompressorJNI.getCompressorNotConnectedFault(m_compressor, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the no connection sticky fault.
     *
     * @return the no connection sticky fault
     */
    public static boolean getNoConnectionStickyFault(){
        IntBuffer status = getLE4IntBuffer();
        boolean retval = CompressorJNI.getCompressorNotConnectedStickyFault(m_compressor, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Clear compressor sticky faults.
     */
    public static void clearCompressorStickyFaults(){
        IntBuffer status = getLE4IntBuffer();
        CompressorJNI.clearAllPCMStickyFaults(m_compressor, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * {@inheritDoc}
     * @param subtable 
     */
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
        
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    public String getSmartDashboardType() {
        return "Compressor";
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
