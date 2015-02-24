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

package io.github.robolib.module;

import static io.github.robolib.util.Common.allocateInt;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.identifier.UpdatingSendable;
import io.github.robolib.jni.CompressorJNI;
import io.github.robolib.jni.HALUtil;
import io.github.robolib.nettable.ITable;
import io.github.robolib.util.StringUtils;

/**
 * A better class for accessing the Pneumatics Control Module.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class Compressor implements Module, UpdatingSendable {
    
    /** The m_table. */
    private ITable m_table;
    
    /** The m_pcm_compressor. */
    private static final ByteBuffer COMPRESSOR =
            CompressorJNI.initializeCompressor((byte) 0);
    
    /** The Constant m_instance. */
    private static Compressor m_instance;
    
    public static void initialize(){
        if(m_instance != null)
            throw new IllegalStateException("Compressor has already been initialized.");
        m_instance = new Compressor();
    }
    
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
    private Compressor(){}
    
    public void free(){
        
    }
    
    /**
     * Gets the compressor current.
     *
     * @return the compressor current
     */
    public static double getCompressorCurrent(){
        IntBuffer status = allocateInt();
        float current = CompressorJNI.getCompressorCurrent(COMPRESSOR, status);
        HALUtil.checkStatus(status);   
        return current;
    }
    
    /**
     * Enable compressor.
     *
     * @param on the on
     */
    public static void enableCompressor(boolean on){
        IntBuffer status = allocateInt();
        CompressorJNI.setClosedLoopControl(COMPRESSOR, on, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Gets the compressor enabled.
     *
     * @return the compressor enabled
     */
    public static boolean getCompressorEnabled(){
        IntBuffer status = allocateInt();
        boolean on = CompressorJNI.getClosedLoopControl(COMPRESSOR, status);
        HALUtil.checkStatus(status);
        return on;
    }
    
    /**
     * Gets the pressure switch.
     *
     * @return the pressure switch
     */
    public static boolean getPressureSwitch(){
        IntBuffer status = allocateInt();
        boolean on = CompressorJNI.getPressureSwitch(COMPRESSOR, status);
        HALUtil.checkStatus(status);
        return on;
    }
    
    /**
     * Gets the current fault.
     *
     * @return the current fault
     */
    public static boolean getCurrentFault(){
        IntBuffer status = allocateInt();
        boolean retval = CompressorJNI.getCompressorCurrentTooHighFault(COMPRESSOR, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the current sticky fault.
     *
     * @return the current sticky fault
     */
    public static boolean getCurrentStickyFault(){
        IntBuffer status = allocateInt();
        boolean retval = CompressorJNI.getCompressorCurrentTooHighStickyFault(COMPRESSOR, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the short fault.
     *
     * @return the short fault
     */
    public static boolean getShortFault(){
        IntBuffer status = allocateInt();
        boolean retval = CompressorJNI.getCompressorShortedFault(COMPRESSOR, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the short sticky fault.
     *
     * @return the short sticky fault
     */
    public static boolean getShortStickyFault(){
        IntBuffer status = allocateInt();
        boolean retval = CompressorJNI.getCompressorShortedStickyFault(COMPRESSOR, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the no connection fault.
     *
     * @return the no connection fault
     */
    public static boolean getNoConnectionFault(){
        IntBuffer status = allocateInt();
        boolean retval = CompressorJNI.getCompressorNotConnectedFault(COMPRESSOR, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Gets the no connection sticky fault.
     *
     * @return the no connection sticky fault
     */
    public static boolean getNoConnectionStickyFault(){
        IntBuffer status = allocateInt();
        boolean retval = CompressorJNI.getCompressorNotConnectedStickyFault(COMPRESSOR, status);
        HALUtil.checkStatus(status);
        return retval;
    }
    
    /**
     * Clear compressor sticky faults.
     */
    public static void clearCompressorStickyFaults(){
        IntBuffer status = allocateInt();
        CompressorJNI.clearAllPCMStickyFaults(COMPRESSOR, status);
        HALUtil.checkStatus(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTable() {
        m_table.putBoolean("Enabled", getCompressorEnabled());
        m_table.putBoolean("Pressure Switch", getPressureSwitch());
        m_table.putString("Current", StringUtils.getNumber2DWithUnits(getCompressorCurrent(), "A"));
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
     * {@inheritDoc}
     */
    @Override
    public String getSmartDashboardType() {
        return "Compressor";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableModule() {
        enableCompressor(true);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableModule() {
        enableCompressor(false);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getModuleEnabled() {
        return getCompressorEnabled();
    }
}
