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

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.iface.AnalogInput;
import io.github.robolib.livewindow.LiveWindowSendable;
import io.github.robolib.pid.PIDSource;
import io.github.robolib.util.Timer;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Gyro extends AnalogInput implements PIDSource, LiveWindowSendable {
    
    public static final int OVERSAMPLE_BITS = 10;
    
    public static final int AVERAGE_BITS = 0;
    
    public static final double SAMPLES_PER_SEC = 50.0;
    
    public static final double SAMPLE_RATE = 51200.0;
    
    public static final double CALIBRATE_SAMPLE_TIME = 5.0;
    
    public static final double DEF_VOLTAGE_PER_DEGREE = 0.007;
    
    private double m_VperDperS;
    private double m_offset;
    private double m_co;
    private int m_center;
    private AccumulatorResult m_results;
    private PIDSourceType m_sType;
    
    public Gyro(AnalogChannel channel){
        super(channel);
        m_results = new AccumulatorResult();
        m_VperDperS = DEF_VOLTAGE_PER_DEGREE;
        
        setAverageBits(AVERAGE_BITS);
        setOversampleBits(OVERSAMPLE_BITS);
        
        setGlobalSampleRate(SAMPLE_RATE);
        
        Timer.delay(0.1);
        
        initAccumulator();
        resetAccumulator();
        
        Timer.delay(CALIBRATE_SAMPLE_TIME);
        
        getAccumulatorOutput(m_results);
        
        m_center = (int)((double) m_results.value / (double) m_results.count + 0.5);
        m_offset = ((double) m_results.value / (double) m_results.count) - m_center;
        
        m_co = m_center + m_offset;
        
        setAccumulatorCenter(m_center);
        resetAccumulator();
        
        setDeadband(0.0);
        
        setPIDSourceType(PIDSourceType.ANGLE);
        
        UsageReporting.report(UsageReporting.ResourceType_Gyro, channel.ordinal());
    }
    
    /**
     * Reset the gyro. Resets the gyro to a heading of zero. This can be used if
     * there is significant drift in the gyro and it needs to be recalibrated
     * after it has been running.case DISTANCE:
     */
    public void reset(){
        resetAccumulator();
    }
    
    /**
     * Return the actual angle in degrees that the robot is currently facing.
     *
     * The angle is based on the current accumulator value corrected by the
     * oversampling rate, the gyro type and the A/D calibration values. The
     * angle is continuous, that is it will continue from 360 to 361 degrees. This allows
     * algorithms that wouldn't want to see a discontinuity in the gyro output
     * as it sweeps past from 360 to 0 on the second time around.
     *
     * @return the current heading of the robot in degrees. This heading is
     *         based on integration of the returned rate from the gyro.
     */
    public double getAngle(){
        getAccumulatorOutput(m_results);
        return (m_results.value - (long)(m_results.count * m_offset)) *
                1e-9 * getLSBWeight() * (1 << getAverageBits()) / (getGlobalSampleRate() * m_VperDperS);
    }
    
    public double get(){
        return getAngle();
    }
    
    /**
     * Return the rate of rotation of the gyro
     *
     * The rate is based on the most recent reading of the gyro analog value
     *
     * @return the current rate in degrees per second
     */
    public double getRate(){
        return (getAverageValue() - m_co) * 1e-9 * getLSBWeight() / ((1 << getOversampleBits()) * m_VperDperS);
    }
    
    /**
     * Set the gyro sensitivity. This takes the number of
     * volts/degree/second sensitivity of the gyro and uses it in subsequent
     * calculations to allow the code to work with multiple gyros. This value
     * is typically found in the gyro datasheet.
     *
     * @param voltsPerDegreesPerSecond The sensitivity in Volts/degree/second.
     */
    public void setSensitivity(double voltsPerDegreesPerSecond){
        m_VperDperS = voltsPerDegreesPerSecond;
    }
    
    /**
     * Set the size of the neutral zone.  Any voltage from the gyro less than
     * this amount from the center is considered stationary.  Setting a
     * deadband will decrease the amount of drift when the gyro isn't rotating,
     * but will make it less accurate.
     *
     * @param volts The size of the deadband in volts
     */
    public void setDeadband(double volts){
        int deadband = (int)(volts * 1e9 / getLSBWeight() * (1 << getOversampleBits()));
        setAccumulatorDeadband(deadband);
    }
    
    /**
     * Set which parameter of the gyro you are using as a process control
     * variable. The Gyro class supports the rate and angle parameters
     *
     * @param sType An enum to select the parameter.
     */
    public void setPIDSourceType(PIDSourceType sType){
        if(sType == PIDSourceType.DISTANCE)
            throw new IllegalArgumentException("Cant use Distance here!");
        
        m_sType = sType;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double pidGet(){
        switch(m_sType){
        case RATE:
            return getRate();
        case ANGLE:
            return getAngle();
        default:
            return 0.0;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getSmartDashboardType() {
        return "Gyro";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", getAngle());
        }
    }
    
}
