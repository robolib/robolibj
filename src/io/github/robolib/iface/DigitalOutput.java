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

package io.github.robolib.iface;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.hal.DIOJNI;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.hal.PWMJNI;
import io.github.robolib.identifier.BooleanSink;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class DigitalOutput extends DigitalIO implements BooleanSink {
    
    ByteBuffer m_pwmGenerator;
    
    public DigitalOutput(DigitalChannel channel){
        super(channel, Direction.OUT);
    }
    
    /**
     * Set the value of this DigitalOutput
     * @param value the value true = High, false = Low
     */
    public void set(boolean value){
        IntBuffer status = getLE4IntBuffer();
        DIOJNI.setDIO(m_port, (short)(value?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Generate a single pulse. Write a pulse to the specified digital output
     * channel. There can only be a single pulse going at any time.
     *
     * @param pulseLength The length of the pulse.
     */
    public void pulse(double pulseLength){
        IntBuffer status = getLE4IntBuffer();
        DIOJNI.pulse(m_port, pulseLength, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Determine if the pulse is still going. Determine if a previously started
     * pulse is still going.
     *
     * @return true if pulsing
     */
    public boolean isPulsing(){
        IntBuffer status = getLE4IntBuffer();
        boolean value = DIOJNI.isPulsing(m_port, status) != 0;
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Change the PWM frequency of the PWM output on a Digital Output line.
     *
     * The valid range is from 0.6 Hz to 19 kHz. The frequency resolution is
     * logarithmic.
     *
     * There is only one PWM frequency for all channnels.
     *
     * @param rate The frequency to output all digital output PWM signals.
     */
    public static void setPWMRate(double rate){
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWMRate(rate, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Enable a PWM Output on this line.
     *
     * Allocate one of the 6 DO PWM generator resources.
     *
     * Supply the initial duty-cycle to output so as to avoid a glitch when
     * first starting.
     *
     * The resolution of the duty cycle is 8-bit for low frequencies (1kHz or
     * less) but is reduced the higher the frequency of the PWM signal is.
     *
     * @param initialDutyCycle The duty-cycle to start generating. [0..1]
     */
    public void enablePWM(double initialDutyCycle){
        if(m_pwmGenerator != null)
            return;
        
        IntBuffer status = getLE4IntBuffer();
        m_pwmGenerator = PWMJNI.allocatePWM(status);
        HALUtil.checkStatus(status);
        PWMJNI.setPWMDutyCycle(m_pwmGenerator, initialDutyCycle, status);
        HALUtil.checkStatus(status);
        PWMJNI.setPWMOutputChannel(m_pwmGenerator, m_channel.ordinal(), status);
    }
    
    /**
     * Change this line from a PWM output back to a static Digital Output line.
     *
     * Free up one of the 6 DO PWM generator resources that were in use.
     */
    public void disablePWM(){
        if(m_pwmGenerator == null)
            return;
        
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWMOutputChannel(m_pwmGenerator, 26, status);
        HALUtil.checkStatus(status);
        PWMJNI.freePWM(m_pwmGenerator, status);
        m_pwmGenerator = null;
    }
    
    /**
     * Change the duty-cycle that is being generated on the line.
     *
     * The resolution of the duty cycle is 8-bit for low frequencies (1kHz or
     * less) but is reduced the higher the frequency of the PWM signal is.
     *
     * @param dutyCycle The duty-cycle to change to. [0..1]
     */
    public void updateDutyCycle(double dutyCycle){
        if(m_pwmGenerator == null)
            return;
        
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWMDutyCycle(m_pwmGenerator, dutyCycle, status);
        HALUtil.checkStatus(status);
    }

}
