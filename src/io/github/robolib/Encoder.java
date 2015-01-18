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

package io.github.robolib;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.hal.EncoderJNI;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.iface.DigitalIO;
import io.github.robolib.iface.DigitalIO.DigitalChannel;
import io.github.robolib.iface.DigitalInput;
import io.github.robolib.pid.PIDSource;
import io.github.robolib.util.MathUtils;
import io.github.robolib.util.log.Logger;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Encoder extends CounterBase implements PIDSource {
    public static enum IndexingType{
        kResetWhileHigh,
        kResetWhileLow,
        kResetOnRisingEdge,
        kResetOnFallingEdge;
    }
    
    protected DigitalIO m_aSource;
    
    protected DigitalIO m_bSource;
    
    protected DigitalIO m_indexSource = null;
    private ByteBuffer m_encoder;
    private double m_distancePerPulse;
    
    private Counter m_counter;
    private EncodingType m_encodingType;
    private int m_encodingScale;
    private boolean m_allocatedA = false;
    private boolean m_allocatedB = false;
    private boolean m_allocatedI = false;
    private PIDSourceType m_pidSType;
    
    public Encoder(DigitalChannel aChannel, DigitalChannel bChannel){
        this(new DigitalInput(aChannel), new DigitalInput(bChannel), null, false, EncodingType.k4X);
        m_allocatedA = true;
        m_allocatedB = true;
    }
    
    public Encoder(DigitalIO aSource, DigitalIO bSource){
        this(aSource, bSource, null, false, EncodingType.k4X);
    }
    
    public Encoder(DigitalChannel aChannel, DigitalChannel bChannel, boolean reverseDirection){
        this(new DigitalInput(aChannel), new DigitalInput(bChannel),
                null, reverseDirection, EncodingType.k4X);
        m_allocatedA = true;
        m_allocatedB = true;
    }
    
    public Encoder(DigitalIO aSource, DigitalIO bSource, boolean reverseDirection){
        this(aSource, bSource, null, reverseDirection, EncodingType.k4X);
    }
    
    public Encoder(DigitalChannel aChannel, DigitalChannel bChannel, DigitalChannel iChannel,
            boolean reverseDirection, EncodingType eType){
        this(new DigitalInput(aChannel), new DigitalInput(bChannel), new DigitalInput(iChannel),
                reverseDirection, eType);
        m_allocatedA = true;
        m_allocatedB = true;
        m_allocatedI = true;
    }
    
    public Encoder(DigitalIO aSource, DigitalIO bSource, DigitalIO iSource, boolean reverseDirection, EncodingType eType){
        m_encodingType = eType;
        switch(eType){
        case k1X:
        case k2X:
            m_encodingScale = eType.ordinal() + 1;
            m_counter = new Counter(eType, m_aSource, m_bSource, reverseDirection);
            break;
        case k4X:
            m_encodingScale = 4;
            IntBuffer status = getLE4IntBuffer();
            IntBuffer index = getLE4IntBuffer();
            m_encoder = EncoderJNI.initializeEncoder(
                    (byte)0, m_aSource.getChannelNumber(), (byte)0,
                    (byte)0, m_bSource.getChannelNumber(), (byte)0,
                    (byte)(reverseDirection?1:0), index, status);
            HALUtil.checkStatus(status);
            m_index = index.get(0);
            m_counter = null;
            setMaxPeriod(0.5);
            break;
        }
        
        if(iSource != null){
            setIndexSource(iSource);
        }
        
        m_distancePerPulse = 1.0;
        m_pidSType = PIDSourceType.DISTANCE;
        
        UsageReporting.report(UsageReporting.kResourceType_Encoder, m_index, eType.ordinal());
    }
    
    public int getEncodingScale(){
        return m_encodingScale;
    }
    
    public void free(){
        if(m_aSource != null && m_allocatedA){
            m_aSource.free();
            m_allocatedA = false;
        }
        if(m_bSource != null && m_allocatedB){
            m_bSource.free();
            m_allocatedB = false;
        }
        if(m_indexSource != null && m_allocatedI){
            m_indexSource.free();
            m_allocatedI = false;
        }
        
        m_aSource = null;
        m_bSource = null;
        m_indexSource = null;
        if(m_counter != null){
            m_counter.free();
            m_counter = null;
        }else{
            IntBuffer status = getLE4IntBuffer();
            EncoderJNI.freeEncoder(m_encoder, status);
            HALUtil.checkStatus(status);
        }
    }
    
    public int getRaw(){
        if(m_counter != null){
            return m_counter.get();
        }else{
            IntBuffer status = getLE4IntBuffer();
            int value = EncoderJNI.getEncoder(m_encoder, status);
            HALUtil.checkStatus(status);
            return value;
        }   
    }
    
    public int get(){
        return (int)(getRaw() * decodingScaleFactor());
    }
    
    public void reset(){
        if(m_counter != null){
            m_counter.reset();
        }else{
            IntBuffer status = getLE4IntBuffer();
            EncoderJNI.resetEncoder(m_encoder, status);
            HALUtil.checkStatus(status);
        }
    }
    
    public double getPeriod(){
        double measuredPeriod;
        if(m_counter != null){
            measuredPeriod = m_counter.getPeriod() / decodingScaleFactor();
        }else{
            IntBuffer status = getLE4IntBuffer();
            measuredPeriod = EncoderJNI.getEncoderPeriod(m_encoder, status);
            HALUtil.checkStatus(status);
        }
        return measuredPeriod;
    }
    
    public void setMaxPeriod(double maxPeriod){
        if(m_counter != null){
            m_counter.setMaxPeriod(maxPeriod * decodingScaleFactor());
        }else{
            IntBuffer status = getLE4IntBuffer();
            EncoderJNI.setEncoderMaxPeriod(m_encoder, maxPeriod, status);
            HALUtil.checkStatus(status);
        }
    }
    
    public boolean getStopped(){
        if(m_counter != null){
            return m_counter.getStopped();
        }else{
            IntBuffer status = getLE4IntBuffer();
            boolean value = EncoderJNI.getEncoderStopped(m_encoder, status) != 0;
            HALUtil.checkStatus(status);
            return value;
        }
    }
    
    public boolean getDirection(){
        if(m_counter != null){
            return m_counter.getDirection();
        }else{
            IntBuffer status = getLE4IntBuffer();
            boolean value = EncoderJNI.getEncoderDirection(m_encoder, status) != 0;
            HALUtil.checkStatus(status);
            return value;
        }
    }
    
    public double decodingScaleFactor(){
        switch(m_encodingType){
        case k1X:
            return 1.0;
        case k2X:
            return 0.5;
        case k4X:
            return 0.25;
        }
        return 0.0;
    }
    
    double getDistance(){
        return getRaw() * decodingScaleFactor() * m_distancePerPulse;
    }
    
    public double getRate(){
        return m_distancePerPulse / getPeriod();
    }
    
    public void setMinRate(double minRate){
        setMaxPeriod(m_distancePerPulse / minRate);
    }
    
    public void setDistancePerPulse(double distance){
        m_distancePerPulse = distance;
    }
    
    public void setReverseDirection(boolean reverse){
        if(m_counter != null){
            m_counter.setReverseDirection(reverse);
        }else{
            Logger.get(Encoder.class).warn("4X Encoders... dont use this");
        }
    }
    
    public void setSamplesPerAverage(int samples){
        switch(m_encodingType){
        case k1X:
        case k2X:
            m_counter.setSamplesPerAverage(samples);
            break;
        case k4X:
            IntBuffer status = getLE4IntBuffer();
            EncoderJNI.setEncoderSamplesToAverage(m_encoder, MathUtils.clamp(samples, 1, 127), status);
            HALUtil.checkStatus(status);
        }
    }
    
    public int getSamplesPerAverage(){
        switch(m_encodingType){
        case k1X:
        case k2X:
            return m_counter.getSamplesPerAverage();
        case k4X:
            IntBuffer status = getLE4IntBuffer();
            int value = EncoderJNI.getEncoderSamplesToAverage(m_encoder, status);
            HALUtil.checkStatus(status);
            return value;
        }
        return 1;
    }
    
    public void setPIDSourceType(PIDSourceType sType){
        if(sType == PIDSourceType.ANGLE)
            throw new IllegalArgumentException("Cant use Angle here!");
        
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
    
    public void setIndexSource(DigitalChannel channel, IndexingType iType){
        setIndexSource(new DigitalInput(channel), iType);
        m_allocatedI = true;
    }
    
    public void setIndexSource(DigitalIO source, IndexingType iType){
        IntBuffer status = getLE4IntBuffer();
        
        boolean activeHigh = false, edgeSensitive = false;
        if(iType.ordinal() % 2 == 0)
            activeHigh = true;
        else
            edgeSensitive = true;
        
        EncoderJNI.setEncoderIndexSource(m_encoder, source.getChannelNumber(), false, activeHigh, edgeSensitive, status);
        
        HALUtil.checkStatus(status);
    }
    
    public void setIndexSource(DigitalChannel channel){
        setIndexSource(new DigitalInput(channel), IndexingType.kResetOnRisingEdge);
        m_allocatedI = true;
    }
    
    public void setIndexSource(DigitalIO source){
        setIndexSource(source, IndexingType.kResetOnRisingEdge);
    }

}
