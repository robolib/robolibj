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

    /**
     * Create an instance of a counter where no sources are selected. Then they
     * all must be selected by calling functions to specify the upsource and the
     * downsource independently.
     *
     * The counter will start counting immediately.
     */
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
    
    /**
     * Create an instance of a Counter object. Create an up-Counter instance
     * given a channel.
     *
     * The counter will start counting immediately.
     *
     * @param upChannel the DIO channel to use as the up source.
     */
    public Counter(DigitalChannel upChannel){
        this();
        setSource(SourceType.UP, upChannel);
    }
    
    /**
     * Create an instance of a counter from a Digital Input. This is used if an
     * existing digital input is to be shared by multiple other objects such as
     * encoders or if the Digital Source is not a DIO channel (such as an Analog Trigger)
     *
     * The counter will start counting immediately.
     *
     * @param upSource the digital source to count
     */
    public Counter(DigitalIO upSource){
        this();
        setSource(SourceType.UP, upSource);
    }
    
    public Counter(EncodingType eType, DigitalChannel upChannel, DigitalChannel downChannel, boolean inverted){
        this(eType, new DigitalInput(upChannel), new DigitalInput(downChannel), inverted);
        m_allocatedUpSource = true;
        m_allocatedDownSource = true;
    }
    
    /**
     * Create an instance of a Counter object. Create an instance of a simple
     * up-Counter.
     *
     * The counter will start counting immediately.
     *
     * @param eType which edges to count
     * @param upSource first source to count
     * @param downSource second source for direction
     * @param inverted true to invert the count
     */
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
    
    /**
     * Set the Up or Down source for the counter as a digital input channel.
     *
     *
     * @param sType the Source type Up or Down.
     * @param channel the DIO channel to count
     */
    public void setSource(SourceType sType, DigitalChannel channel){
        setSource(sType, new DigitalInput(channel));
        switch(sType){
        case UP:
            m_allocatedUpSource = true;
        case DOWN:
            m_allocatedDownSource = true;
        }
    }
    
    /**
     * Set the source object that causes the counter to count up or down.
     * Set the up or down counting DigitalSource.
     *
     *
     * @param sType the Source type Up or Down.
     * @param source the digital source to count
     */
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
    
    
    /**
     * Set the edge sensitivity on a counting source. Set the source
     * to either detect rising edges or falling edges.
     *
     * @param sType the Source type Up or Down.
     * @param risingEdge true to count the rising edge
     * @param fallingEdge true to count the falling edge
     */
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
    
    /**
     * Disable the counting source to the counter.
     * 
     * @param sType the Source type Up or Down.
     */
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
    
    /**
     * Set standard up / down counting mode on this counter. Up and down counts
     * are sourced independently from two inputs.
     */
    public void setUpDownCounterMode(){
        setMode(CounterMode.kTwoPulse);
    }
    
    /**
     * Set Semi-period mode on this counter. Counts up on both rising and
     * falling edges.
     *
     * @param highSemiPeriod true to count up on both rising and falling
     */
    public void setSemiPeriodMode(boolean highSemiPeriod){
        setMode(CounterMode.kSemiperiod, (byte)(highSemiPeriod?1:0));
    }
    
    /**
     * Configure the counter to count in up or down based on the length of the
     * input pulse. This mode is most useful for direction sensitive gear tooth
     * sensors.
     *
     * @param threshold The pulse length beyond which the counter counts the
     * opposite direction. Units are seconds.
     */
    public void setPulseLengthMode(double threshold){
        setMode(CounterMode.kPulseLength, threshold);
    }
    
    /**
     * Set external direction mode on this counter. Counts are sourced on the Up
     * counter input. The Down counter input represents the direction to count.
     */
    public void setExternalDirectionMode(){
        setMode(CounterMode.kExternalDirection);
    }
    
    /**
     * Read the current counter value. Read the value at this instant. It may
     * still be running, so it reflects the current value. Next time it is read,
     * it might have a different value.
     */
    public int get(){
        IntBuffer status = getLE4IntBuffer();
        int value = CounterJNI.getCounter(m_counter, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Read the current scaled counter value. Read the value at this instant,
     * scaled by the distance per pulse (defaults to 1).
     *
     * @return The distance since the last reset
     */
    public double getDistance(){
        return get() * m_distancePerPulse;
    }
    
    /**
     * Reset the Counter to zero. Set the counter value to zero. This doesn't
     * effect the running state of the counter, just sets the current value to
     * zero.
     */
    public void reset(){
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.resetCounter(m_counter, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Set the maximum period where the device is still considered "moving".
     * Sets the maximum period where the device is considered moving. This value
     * is used to determine the "stopped" state of the counter using the
     * GetStopped method.
     *
     * @param maxPeriod The maximum period where the counted device is considered
     * moving in seconds.
     */
    public void setMaxPeriod(double maxPeriod){
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.setCounterMaxPeriod(m_counter, maxPeriod, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Select whether you want to continue updating the event timer output when
     * there are no samples captured. The output of the event timer has a buffer
     * of periods that are averaged and posted to a register on the FPGA. When
     * the timer detects that the event source has stopped (based on the
     * MaxPeriod) the buffer of samples to be averaged is emptied. If you enable
     * the update when empty, you will be notified of the stopped source and the
     * event time will report 0 samples. If you disable update when empty, the
     * most recent average will remain on the output until a new sample is
     * acquired. You will never see 0 samples output (except when there have
     * been no events since an FPGA reset) and you will likely not see the
     * stopped bit become true (since it is updated at the end of an average and
     * there are no samples to average).
     *
     * @param enabled true to continue updating
     */
    public void setUpdateWhenEmpty(boolean enabled){
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.setCounterUpdateWhenEmpty(m_counter, (byte)(enabled?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Determine if the clock is stopped. Determine if the clocked input is
     * stopped based on the MaxPeriod value set using the SetMaxPeriod method.
     * If the clock exceeds the MaxPeriod, then the device (and counter) are
     * assumed to be stopped and it returns true.
     *
     * @return Returns true if the most recent counter period exceeds the
     * MaxPeriod value set by SetMaxPeriod.
     */
    public boolean getStopped(){
        IntBuffer status = getLE4IntBuffer();
        boolean value = CounterJNI.getCounterStopped(m_counter, status) != 0;
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * The last direction the counter value changed.
     *
     * @return The last direction the counter value changed.
     */
    public boolean getDirection(){
        IntBuffer status = getLE4IntBuffer();
        boolean value = CounterJNI.getCounterDirection(m_counter, status) != 0;
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Set the Counter to return reversed sensing on the direction. This allows
     * counters to change the direction they are counting in the case of 1X and
     * 2X quadrature encoding only. Any other counter mode isn't supported.
     *
     * @param reverse true if the value counted should be negated.
     */
    public void setReverseDirection(boolean reverse){
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.setCounterReverseDirection(m_counter, (byte)(reverse?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Get the Period of the most recent count. Returns the time interval of the
     * most recent count. This can be used for velocity calculations to
     * determine shaft speed.
     *
     * @return The period of the last two pulses in units of seconds.
     */
    public double getPeriod(){
        IntBuffer status = getLE4IntBuffer();
        double value = CounterJNI.getCounterPeriod(m_counter, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Get the current rate of the Counter. Read the current rate of the counter
     * accounting for the distance per pulse value. The default value for
     * distance per pulse (1) yields units of pulses per second.
     *
     * @return The rate in units/sec
     */
    public double getRate(){
        return m_distancePerPulse / getPeriod();
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
        samples = MathUtils.clamp(samples, 1, 127);
        IntBuffer status = getLE4IntBuffer();
        CounterJNI.setCounterSamplesToAverage(m_counter, samples, status);
        HALUtil.checkStatus(status);
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
        IntBuffer status = getLE4IntBuffer();
        int value = CounterJNI.getCounterSamplesToAverage(m_counter, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Set the distance per pulse for this counter. This sets the multiplier
     * used to determine the distance driven based on the count value from the
     * encoder. Set this value based on the Pulses per Revolution and factor in
     * any gearing reductions. This distance can be in any units you like,
     * linear or angular.
     *
     * @param distance The scale factor that will be used to convert pulses to
     * useful units.
     */
    public void setDistancePerPulse(double distance){
        m_distancePerPulse = distance;
    }
    
    /**
     * Set which parameter of the encoder you are using as a process control
     * variable. The counter class supports the rate and distance parameters.
     *
     * @param sType An enum to select the parameter.
     */
    public void setPIDSourceType(PIDSourceType sType){
        if(sType == PIDSourceType.ANGLE)
            throw new IllegalArgumentException("PIDSourceType.ANGLE is not a valid source type for a counter.");
        
        m_pidSType = sType;
    }
    
    /**
     * {@inheritDoc}
     */
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
