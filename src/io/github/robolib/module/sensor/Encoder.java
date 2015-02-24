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

package io.github.robolib.module.sensor;

import static io.github.robolib.util.Common.allocateInt;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.identifier.PIDSource;
import io.github.robolib.identifier.RateSource;
import io.github.robolib.jni.EncoderJNI;
import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.module.iface.DigitalIO;
import io.github.robolib.module.iface.DigitalIO.DigitalChannel;
import io.github.robolib.module.iface.DigitalInput;
import io.github.robolib.util.MathUtils;
import io.github.robolib.util.log.Logger;

/**
 * Class to read quad encoders. Quadrature encoders are devices that count shaft
 * rotation and can sense direction. The output of the QuadEncoder class is an
 * integer that can count either up or down, and can go negative for reverse
 * direction counting. When creating QuadEncoders, a direction is supplied that
 * changes the sense of the output to make code more readable if the encoder is
 * mounted such that forward movement generates negative values. Quadrature
 * encoders have two digital outputs, an A Channel and a B Channel that are out
 * of phase with each other to allow the FPGA to do direction sensing.
 *
 * All encoders will immediately start counting - reset() them if you need them
 * to be zeroed before use.
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Encoder extends CounterBase implements SensorModule,
        PIDSource, RateSource {
    
    /**
     * The different types of indexing available
     * 
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum IndexingType{
        /** Reset when the signal is high */
        RESET_WHILE_HIGH,
        /** Reset when the signal is low */
        RESET_WHILE_LOW,
        /** Reset on fising edge */
        RESET_EDGE_RISING,
        /** Reset on falling edge */
        RESET_EDGE_FALLING;
    }
    
    /** The a source */
    protected DigitalIO m_aSource;
    /** The b source */
    protected DigitalIO m_bSource;
    /** The index source */
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
    
    
    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     *
     * The encoder will start counting immediately.
     *
     * @param aChannel The a channel {@link DigitalChannel}.
     * @param bChannel The b channel {@link DigitalChannel}.
     */
    public Encoder(DigitalChannel aChannel, DigitalChannel bChannel){
        this(new DigitalInput(aChannel), new DigitalInput(bChannel), null, false, EncodingType.k4X);
        m_allocatedA = true;
        m_allocatedB = true;
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a and b channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     *
     * The encoder will start counting immediately.
     *
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     */
    public Encoder(DigitalIO aSource, DigitalIO bSource){
        this(aSource, bSource, null, false, EncodingType.k4X);
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     *
     * The encoder will start counting immediately.
     *
     * @param aChannel The a channel {@link DigitalChannel}.
     * @param bChannel The b channel {@link DigitalChannel}.
     * @param reverseDirection represents the orientation of the encoder
     * and inverts the output values if necessary so forward represents
     * positive values.
     */
    public Encoder(DigitalChannel aChannel, DigitalChannel bChannel,
            boolean reverseDirection){
        this(new DigitalInput(aChannel), new DigitalInput(bChannel),
                null, reverseDirection, EncodingType.k4X);
        m_allocatedA = true;
        m_allocatedB = true;
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a and b channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     *
     * The encoder will start counting immediately.
     *
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param reverseDirection represents the orientation of the encoder
     * and inverts the output values if necessary so forward represents
     * positive values.
     */
    public Encoder(DigitalIO aSource, DigitalIO bSource,
            boolean reverseDirection){
        this(aSource, bSource, null, reverseDirection, EncodingType.k4X);
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     * Using an index pulse forces 4x encoding
     *
     * The encoder will start counting immediately.
     *
     * @param aChannel The a channel {@link DigitalChannel}.
     * @param bChannel The b channel {@link DigitalChannel}.
     * @param iChannel The index channel {@link DigitalChannel}.
     */
    public Encoder(DigitalChannel aChannel, DigitalChannel bChannel, DigitalChannel iChannel){
        this(new DigitalInput(aChannel), new DigitalInput(bChannel), new DigitalInput(iChannel),
                false, EncodingType.k4X);
        m_allocatedA = true;
        m_allocatedB = true;
        m_allocatedI = true;
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a, b and index channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     *
     * The encoder will start counting immediately.
     *
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param iSource the source that should be used for the index channel.
     */
    public Encoder(DigitalIO aSource, DigitalIO bSource, DigitalIO iSource){
        this(aSource, bSource, iSource, false, EncodingType.k4X);
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     *
     * The encoder will start counting immediately.
     *
     * @param aChannel The a channel {@link DigitalChannel}.
     * @param bChannel The b channel {@link DigitalChannel}.
     * @param reverseDirection represents the orientation of the encoder
     * and inverts the output values if necessary so forward represents
     * positive values.
     * @param eType either k1X, k2X, or k4X to indicate 1X, 2X or 4X
     * decoding. If 4X is selected, then an encoder FPGA object is used
     * and the returned counts will be 4x the encoder spec'd value since
     * all rising and falling edges are counted. If 1X or 2X are selected
     * then a counter object will be used and the returned value will either
     * exactly match the spec'd count or be double (2x) the spec'd count.
     */
    public Encoder(DigitalChannel aChannel, DigitalChannel bChannel,
            boolean reverseDirection, EncodingType eType){
        this(new DigitalInput(aChannel), new DigitalInput(bChannel), null,
                reverseDirection, eType);
        m_allocatedA = true;
        m_allocatedB = true;
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a and b channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     *
     * The encoder will start counting immediately.
     *
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param reverseDirection represents the orientation of the encoder
     * and inverts the output values if necessary so forward represents
     * positive values.
     * @param eType either k1X, k2X, or k4X to indicate 1X, 2X or 4X
     * decoding. If 4X is selected, then an encoder FPGA object is used
     * and the returned counts will be 4x the encoder spec'd value since
     * all rising and falling edges are counted. If 1X or 2X are selected
     * then a counter object will be used and the returned value will either
     * exactly match the spec'd count or be double (2x) the spec'd count.
     */
    public Encoder(DigitalIO aSource, DigitalIO bSource,
            boolean reverseDirection, EncodingType eType){
        this(aSource, bSource, null, reverseDirection, eType);
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a and b channels.
     * Using an index pulse forces 4x encoding
     *
     * The encoder will start counting immediately.
     *
     * @param aChannel The a channel {@link DigitalChannel}.
     * @param bChannel The b channel {@link DigitalChannel}.
     * @param iChannel The index channel {@link DigitalChannel}.
     * @param reverseDirection represents the orientation of the encoder
     * and inverts the output values if necessary so forward represents
     * positive values.
     */
    public Encoder(DigitalChannel aChannel, DigitalChannel bChannel, DigitalChannel iChannel,
            boolean reverseDirection){
        this(new DigitalInput(aChannel), new DigitalInput(bChannel), new DigitalInput(iChannel),
                reverseDirection, EncodingType.k4X);
        m_allocatedA = true;
        m_allocatedB = true;
        m_allocatedI = true;
    }
    
    /**
     * Encoder constructor. Construct a Encoder given a, b and index channels as
     * digital inputs. This is used in the case where the digital inputs are
     * shared. The Encoder class will not allocate the digital inputs and assume
     * that they already are counted.
     *
     * The encoder will start counting immediately.
     *
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param iSource the source that should be used for the index channel.
     * @param reverseDirection represents the orientation of the encoder
     * and inverts the output values if necessary so forward represents
     * positive values.
     */
    public Encoder(DigitalIO aSource, DigitalIO bSource, DigitalIO iSource,
            boolean reverseDirection){
        this(aSource, bSource, iSource, reverseDirection, EncodingType.k4X);
    }
    
    /**
     * Encoder constructor.
     *
     * The encoder will start counting immediately.
     *
     * @param aSource The source that should be used for the a channel.
     * @param bSource the source that should be used for the b channel.
     * @param iSource the source that should be used for the index channel.
     * @param reverseDirection represents the orientation of the encoder
     * and inverts the output values if necessary so forward represents
     * positive values.
     * @param eType either k1X, k2X, or k4X to indicate 1X, 2X or 4X
     * decoding. If 4X is selected, then an encoder FPGA object is used
     * and the returned counts will be 4x the encoder spec'd value since
     * all rising and falling edges are counted. If 1X or 2X are selected
     * then a counter object will be used and the returned value will either
     * exactly match the spec'd count or be double (2x) the spec'd count.
     */
    private Encoder(DigitalIO aSource, DigitalIO bSource, DigitalIO iSource,
            boolean reverseDirection, EncodingType eType){
        m_encodingType = eType;
        switch(eType){
        case k1X:
        case k2X:
            m_encodingScale = eType.ordinal() + 1;
            m_counter = new Counter(eType, m_aSource, m_bSource, reverseDirection);
            break;
        case k4X:
            m_encodingScale = 4;
            IntBuffer status = allocateInt();
            IntBuffer index = allocateInt();
            m_encoder = EncoderJNI.initializeEncoder(
                    m_aSource.getModuleNumber(), m_aSource.getChannelNumber(),
                    (byte)(m_aSource.isAnalogTrigger()?1:0),
                    m_bSource.getModuleNumber(), m_bSource.getChannelNumber(),
                    (byte)(m_bSource.isAnalogTrigger()?1:0),
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
        
        UsageReporting.report(UsageReporting.ResourceType_Encoder, m_index, eType.ordinal());
    }
    
    /**
     * @return the encoding scale factor 1x, 2x, or 4x, per the requested
     * encodingType. Used to divide raw edge counts down to spec'd counts.
     */
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
            IntBuffer status = allocateInt();
            EncoderJNI.freeEncoder(m_encoder, status);
            HALUtil.checkStatus(status);
        }
    }
    
    /**
     * Gets the raw value from the encoder. The raw value is the actual count
     * unscaled by the 1x, 2x, or 4x scale factor.
     *
     * @return Current raw count from the encoder
     */
    public int getRaw(){
        if(m_counter != null){
            return m_counter.get();
        }else{
            IntBuffer status = allocateInt();
            int value = EncoderJNI.getEncoder(m_encoder, status);
            HALUtil.checkStatus(status);
            return value;
        }   
    }
    
    /**
     * Gets the current count. Returns the current count on the Encoder. This
     * method compensates for the decoding type.
     *
     * @return Current count from the Encoder adjusted for the 1x, 2x, or 4x
     *         scale factor.
     */
    @Override
    public int get(){
        return getCount();
    }
    
    /**
     * Gets the current count. Returns the current count on the Encoder. This
     * method compensates for the decoding type.
     *
     * @return Current count from the Encoder adjusted for the 1x, 2x, or 4x
     *         scale factor.
     */
    @Override
    public int getCount(){
        return (int)(getRaw() * decodingScaleFactor());
    }
    
    /**
     * Reset the Encoder distance to zero. Resets the current count to zero on
     * the encoder.
     */
    @Override
    public void reset(){
        if(m_counter != null){
            m_counter.reset();
        }else{
            IntBuffer status = allocateInt();
            EncoderJNI.resetEncoder(m_encoder, status);
            HALUtil.checkStatus(status);
        }
    }
    
    /**
     * Returns the period of the most recent pulse. Returns the period of the
     * most recent Encoder pulse in seconds. This method compensates for the
     * decoding type.
     *
     * @return Period in seconds of the most recent pulse.
     */
    @Override
    public double getPeriod(){
        double measuredPeriod;
        if(m_counter != null){
            measuredPeriod = m_counter.getPeriod() / decodingScaleFactor();
        }else{
            IntBuffer status = allocateInt();
            measuredPeriod = EncoderJNI.getEncoderPeriod(m_encoder, status);
            HALUtil.checkStatus(status);
        }
        return measuredPeriod;
    }
    
    /**
     * Sets the maximum period for stopped detection. Sets the value that
     * represents the maximum period of the Encoder before it will assume that
     * the attached device is stopped. This timeout allows users to determine if
     * the wheels or other shaft has stopped rotating. This method compensates
     * for the decoding type.
     *
     *
     * @param maxPeriod The maximum time between rising and falling edges before the
     * FPGA will report the device stopped. This is expressed in seconds.
     */
    @Override
    public void setMaxPeriod(double maxPeriod){
        if(m_counter != null){
            m_counter.setMaxPeriod(maxPeriod * decodingScaleFactor());
        }else{
            IntBuffer status = allocateInt();
            EncoderJNI.setEncoderMaxPeriod(m_encoder, maxPeriod, status);
            HALUtil.checkStatus(status);
        }
    }
    
    /**
     * Determine if the encoder is stopped. Using the MaxPeriod value, a boolean
     * is returned that is true if the encoder is considered stopped and false
     * if it is still moving. A stopped encoder is one where the most recent
     * pulse width exceeds the MaxPeriod.
     *
     * @return True if the encoder is considered stopped.
     */
    @Override
    public boolean getStopped(){
        if(m_counter != null){
            return m_counter.getStopped();
        }else{
            IntBuffer status = allocateInt();
            boolean value = EncoderJNI.getEncoderStopped(m_encoder, status) != 0;
            HALUtil.checkStatus(status);
            return value;
        }
    }
    
    /**
     * The last direction the encoder value changed.
     *
     * @return The last direction the encoder value changed.
     */
    @Override
    public boolean getDirection(){
        if(m_counter != null){
            return m_counter.getDirection();
        }else{
            IntBuffer status = allocateInt();
            boolean value = EncoderJNI.getEncoderDirection(m_encoder, status) != 0;
            HALUtil.checkStatus(status);
            return value;
        }
    }
    
    /**
     * The scale needed to convert a raw counter value into a number of encoder
     * pulses.
     */
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
    
    /**
     * Get the distance the robot has driven since the last reset.
     *
     * @return The distance driven since the last reset as scaled by the value
     * from setDistancePerPulse().
     */
    public double getDistance(){
        return getRaw() * decodingScaleFactor() * m_distancePerPulse;
    }
    
    /**
     * Get the current rate of the encoder. Units are distance per second as
     * scaled by the value from setDistancePerPulse().
     *
     * @return The current rate of the encoder.
     */
    public double getRate(){
        return m_distancePerPulse / getPeriod();
    }
    
    /**
     * Set the minimum rate of the device before the hardware reports it
     * stopped.
     *
     * @param minRate The minimum rate. The units are in distance per second
     * as scaled by the value from setDistancePerPulse().
     */
    public void setMinRate(double minRate){
        setMaxPeriod(m_distancePerPulse / minRate);
    }
    
    /**
     * Set the distance per pulse for this encoder. This sets the multiplier
     * used to determine the distance driven based on the count value from the
     * encoder. Do not include the decoding type in this scale. The library
     * already compensates for the decoding type. Set this value based on the
     * encoder's rated Pulses per Revolution and factor in gearing reductions
     * following the encoder shaft. This distance can be in any units you like,
     * linear or angular.
     *
     * @param distance The scale factor that will be used to convert
     * pulses to useful units.
     */
    public void setDistancePerPulse(double distance){
        m_distancePerPulse = distance;
    }
    
    /**
     * Set the direction sensing for this encoder. This sets the direction
     * sensing on the encoder so that it could count in the correct software
     * direction regardless of the mounting.
     *
     * @param reverse true if the encoder direction should be reversed
     */
    public void setReverseDirection(boolean reverse){
        if(m_counter != null){
            m_counter.setReverseDirection(reverse);
        }else{
            Logger.get(Encoder.class).warn("4X Encoders... dont use this");
        }
    }
    
    /**
     * Set the Samples to Average which specifies the number of samples of the
     * timer to average when calculating the period. Perform averaging to
     * account for mechanical imperfections or as oversampling to increase
     * resolution.
     * 
     * @param samples The number of samples to average from 1 to 127.
     */
    public void setSamplesPerAverage(int samples){
        switch(m_encodingType){
        case k1X:
        case k2X:
            m_counter.setSamplesPerAverage(samples);
            break;
        case k4X:
            IntBuffer status = allocateInt();
            EncoderJNI.setEncoderSamplesToAverage(m_encoder, MathUtils.clamp(samples, 1, 127), status);
            HALUtil.checkStatus(status);
        }
    }
    
    /**
     * Get the Samples to Average which specifies the number of samples of the
     * timer to average when calculating the period. Perform averaging to
     * account for mechanical imperfections or as oversampling to increase
     * resolution.
     *
     * @return SamplesToAverage The number of samples being averaged (from 1 to
     * 127)
     */
    public int getSamplesPerAverage(){
        switch(m_encodingType){
        case k1X:
        case k2X:
            return m_counter.getSamplesPerAverage();
        case k4X:
            IntBuffer status = allocateInt();
            int value = EncoderJNI.getEncoderSamplesToAverage(m_encoder, status);
            HALUtil.checkStatus(status);
            return value;
        }
        return 1;
    }
    
    /**
     * Set which parameter of the encoder you are using as a process control
     * variable. The encoder class supports the rate and distance parameters.
     *
     * @param sType An enum to select the parameter.
     */
    public void setPIDSourceType(PIDSourceType sType){
        if(sType == PIDSourceType.ANGLE)
            throw new IllegalArgumentException("Cant use Angle here!");
        
        m_pidSType = sType;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
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
    
    /**
     * Set the index source for the encoder.
     * When this source rises, the encoder count automatically resets.
     *
     * @param channel A {@link DigitalChannel} to set as the encoder index
     * @param iType The state that will cause the encoder to reset
     */
    public void setIndexSource(DigitalChannel channel, IndexingType iType){
        setIndexSource(new DigitalInput(channel), iType);
        m_allocatedI = true;
    }
    
    /**
     * Set the index source for the encoder.
     * When this source rises, the encoder count automatically resets.
     *
     * @param source A digital source to set as the encoder index
     * @param iType The state that will cause the encoder to reset
     */
    public void setIndexSource(DigitalIO source, IndexingType iType){
        IntBuffer status = allocateInt();
        
        boolean activeHigh = false, edgeSensitive = false;
        if(iType.ordinal() % 2 == 0)
            activeHigh = true;
        else
            edgeSensitive = true;
        
        EncoderJNI.setEncoderIndexSource(
                m_encoder,
                source.getChannelNumber(), source.isAnalogTrigger(),
                activeHigh, edgeSensitive, status);
        
        HALUtil.checkStatus(status);
    }
    
    /**
     * Set the index source for the encoder.
     * When this source is activated, the encoder count automatically resets.
     *
     * @param channel A {@link DigitalChannel} to set as the encoder index
     */
    public void setIndexSource(DigitalChannel channel){
        setIndexSource(new DigitalInput(channel), IndexingType.RESET_EDGE_RISING);
        m_allocatedI = true;
    }
    
    /**
     * Set the index source for the encoder.
     * When this source is activated, the encoder count automatically resets.
     *
     * @param source A digital source to set as the encoder index
     */
    public void setIndexSource(DigitalIO source){
        setIndexSource(source, IndexingType.RESET_EDGE_RISING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableModule() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableModule() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getModuleEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}
