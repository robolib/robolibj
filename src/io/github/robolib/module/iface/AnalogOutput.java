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

package io.github.robolib.module.iface;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.IntBuffer;

import io.github.robolib.identifier.LiveWindowSendable;
import io.github.robolib.identifier.NumberSink;
import io.github.robolib.jni.AnalogJNI;
import io.github.robolib.jni.HALUtil;
import io.github.robolib.nettable.ITable;

/**
 * Analog output class.
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class AnalogOutput extends AnalogIO implements LiveWindowSendable, NumberSink {
    
    private ITable m_table;
    
    /**
     * Construct an analog output on a specified AnalogChannel
     * channel.
     *
     * @param channel The channel to represent.
     */
    public AnalogOutput(AnalogChannel channel){
        super(channel, Direction.OUT);
    }
    
    /**
     * Set the voltage of this Analog Output
     * @param voltage the voltage to set
     */
    public void set(double voltage){
        setVoltage(voltage);
    }
    
    public void setVoltage(double voltage){
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAnalogOutput(m_port, voltage, status);
        HALUtil.checkStatus(status);
    }
    
    public double get(){
        return getVoltage();
    }
    
    public double getVoltage(){
        IntBuffer status = getLE4IntBuffer();
        double value = AnalogJNI.getAnalogOutput(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSmartDashboardType(){
        return "Analog Output";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void updateTable(){
        if(m_table != null){
            m_table.putNumber("Value", getVoltage());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * Analog Channels don't have to do anything special when entering the
     * LiveWindow.
     * {@inheritDoc}
     */
    @Override
    public void startLiveWindowMode() {
    }

    /**
     * Analog Channels don't have to do anything special when exiting the
     * LiveWindow.
     * {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {
    }
    
    
    
    

}
