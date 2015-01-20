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

package io.github.robolib.sensor;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.hal.CounterJNI;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.iface.DigitalIO;
import io.github.robolib.iface.DigitalIO.DigitalChannel;
import io.github.robolib.iface.DigitalInput;
import io.github.robolib.pid.PIDSource;
import io.github.robolib.util.MathUtils;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Counter extends CounterBase implements PIDSource {
    
    public static enum CounterMode{
        kTwoPulse,
        kSemiperiod,
        kPulseLength,
        kExternalDirection;
    }
    
    public static enum SourceType{
        UP,
        DOWN;
    }
    
    private DigitalIO m_upSource;
    private DigitalIO m_downSource;
    private boolean m_allocatedUpSource;
    private boolean m_allocatedDownSource;
    private ByteBuffer m_counter;
    private int m_index;
    private PIDSourceType m_pidSType;
    private double m_distancePerPulse;    

    public Counter(){
        
        IntBuffer status = getLE4IntBuffer();
        IntBuffer index = getLE4IntBuffer();
        m_counter = CounterJNI.initializeCounter(0, index, status);
        HALUtil.checkStatus(status);
        m_index = index.get(0);

        m_allocatedUpSource = false;
        m_allocatedDownSource = false;
        m_upSource = null;
        m_downSource = null;

        setMaxPeriod(0.5);
        
        UsageReporting.report(UsageReporting.kResourceType_Counter, m_index, 0);
        
    }
    
    public Counter(DigitalChannel upChannel){
        this();
        setSource(SourceType.UP, upChannel);
    }
    
    public Counter(DigitalIO upSource){
        this();
        setSource(SourceType.UP, upSource);
    }
    
    public Counter(EncodingType eType, DigitalChannel upChannel, DigitalChannel downChannel, boolean inverted){
        this(eType, new DigitalInput(upChannel), new DigitalInput(downChannel), inverted);
        m_allocatedUpSource = true;
        m_allocatedDownSource = true;
    }
    
    public Counter(EncodingType eType, DigitalIO upSource, DigitalIO downSource, boolean inverted){
        this();
        setExternalDirectionMode();
        
        setSource(SourceType.UP, upSource);
        setSource(SourceType.DOWN, downSource);
        
        if(eType == EncodingType.k4X)
            throw new IllegalArgumentException("Counters only support 1X and 2X quadreature decoding!");
        
        IntBuffer status = getLE4IntBuffer();
        boolean upEdgeA = eType == EncodingType.k1X;
        setSourceEdge(SourceType.UP, true, upEdgeA);
        CounterJNI.setCounterAverageSize(m_counter, eType.ordinal() + 1, status);
        HALUtil.checkStatus(status);
        setSourceEdge(SourceType.DOWN, inverted, true);
        
    }
    
    public void free(){
        setUpdateWhenEmpty(true);
        
        clearSource(SourceType.UP);
        clearSource(SourceType.DOWN);
        
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.freeCounter(m_counter, status);
        HALUtil.checkStatus(status);
        
        m_upSource = null;
        m_downSource = null;
        m_counter = null;
    }
    
    public void setSource(SourceType sType, DigitalChannel channel){
        setSource(sType, new DigitalInput(channel));
        switch(sType){
        case UP:
            m_allocatedUpSource = true;
        case DOWN:
            m_allocatedDownSource = true;
        }
    }
    
    public void setSource(SourceType sType, DigitalIO source){
        IntBuffer status = getLE4IntBuffer();
        switch(sType){
        case UP:
            if(m_upSource != null && m_allocatedUpSource){
                m_upSource.free();
                m_allocatedUpSource = false;
            }
            m_upSource = source;
            CounterJNI.setCounterUpSource(m_counter, source.getChannelNumber(), (byte)0, status);
            break;
        case DOWN:
            if(m_downSource != null && m_allocatedDownSource){
                m_downSource.free();
                m_allocatedDownSource = false;
            }
            m_downSource = source;
            CounterJNI.setCounterDownSource(m_counter, source.getChannelNumber(), (byte)0, status);
            break;
        }
        HALUtil.checkStatus(status);
    }
    
    public void setSourceEdge(SourceType sType, boolean risingEdge, boolean fallingEdge){
        IntBuffer status = getLE4IntBuffer();
        switch(sType){
        case UP:
            CounterJNI.setCounterUpSourceEdge(m_counter, (byte)(risingEdge?1:0), (byte)(fallingEdge?1:0), status);
        case DOWN:
            CounterJNI.setCounterDownSourceEdge(m_counter, (byte)(risingEdge?1:0), (byte)(fallingEdge?1:0), status);
        }
        HALUtil.checkStatus(status);
    }
    
    public void clearSource(SourceType sType){
        IntBuffer status = getLE4IntBuffer();
        switch(sType){
        case UP:
            if(m_upSource != null && m_allocatedUpSource){
                m_upSource.free();
                m_allocatedUpSource = false;
            }
            m_upSource = null;
            CounterJNI.clearCounterUpSource(m_counter, status);
            break;
                    
        case DOWN:
            if(m_downSource != null && m_allocatedDownSource){
                m_downSource.free();
                m_allocatedDownSource = false;
            }
            CounterJNI.clearCounterDownSource(m_counter, status);
            break;
        }
        HALUtil.checkStatus(status);
    }
    
    private void setMode(CounterMode mode, Object... args){
        IntBuffer status = getLE4IntBuffer();
        switch(mode){
        case kTwoPulse:
            CounterJNI.setCounterUpDownMode(m_counter, status);
        case kSemiperiod:
            CounterJNI.setCounterSemiPeriodMode(m_counter,(byte)args[0], status);
        case kPulseLength:
            CounterJNI.setCounterPulseLengthMode(m_counter, (double)args[0], status);
        case kExternalDirection:
            CounterJNI.setCounterExternalDirectionMode(m_counter, status);
        }
        HALUtil.checkStatus(status);
    }
    
    public void setUpDownCounterMode(){
        setMode(CounterMode.kTwoPulse);
    }
    
    public void setSemiPeriodMode(boolean highSemiPeriod){
        setMode(CounterMode.kSemiperiod, (byte)(highSemiPeriod?1:0));
    }
    
    public void setPulseLengthMode(double threshold){
        setMode(CounterMode.kPulseLength, threshold);
    }
    
    public void setExternalDirectionMode(){
        setMode(CounterMode.kExternalDirection);
    }
    
    public int get(){
        IntBuffer status = getLE4IntBuffer();
        int value = CounterJNI.getCounter(m_counter, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    public double getDistance(){
        return get() * m_distancePerPulse;
    }
    
    public void reset(){
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.resetCounter(m_counter, status);
        HALUtil.checkStatus(status);
    }
    
    public void setMaxPeriod(double maxPeriod){
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.setCounterMaxPeriod(m_counter, maxPeriod, status);
        HALUtil.checkStatus(status);
    }
    
    public void setUpdateWhenEmpty(boolean enabled){
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.setCounterUpdateWhenEmpty(m_counter, (byte)(enabled?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    public boolean getStopped(){
        IntBuffer status = getLE4IntBuffer();
        boolean value = CounterJNI.getCounterStopped(m_counter, status) != 0;
        HALUtil.checkStatus(status);
        return value;
    }
    
    public boolean getDirection(){
        IntBuffer status = getLE4IntBuffer();
        boolean value = CounterJNI.getCounterDirection(m_counter, status) != 0;
        HALUtil.checkStatus(status);
        return value;
    }
    
    public void setReverseDirection(boolean reverse){
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.setCounterReverseDirection(m_counter, (byte)(reverse?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    public double getPeriod(){
        IntBuffer status = getLE4IntBuffer();
        double value = CounterJNI.getCounterPeriod(m_counter, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    public double getRate(){
        return m_distancePerPulse / getPeriod();
    }
    
    public void setSamplesPerAverage(int samples){
        samples = MathUtils.clamp(samples, 1, 127);
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.setCounterSamplesToAverage(m_counter, samples, status);
        HALUtil.checkStatus(status);
    }
    
    public int getSamplesPerAverage(){
        IntBuffer status = getLE4IntBuffer();
        int value = CounterJNI.getCounterSamplesToAverage(m_counter, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    public void setDistancePerPulse(double distance){
        m_distancePerPulse = distance;
    }
    
    public void setPIDSourceType(PIDSourceType sType){
        if(sType == PIDSourceType.ANGLE)
            throw new IllegalArgumentException("PIDSourceType.ANGLE is not a valid source type for a counter.");
        
        m_pidSType = sType;
    }
    
    public double pidGet(){
        switch(m_pidSType){
        case DISTANCE:
            return getDistance();
        case RATE:
            return getRate();
        default:
            return 0.0;
        }
    }
    
}
