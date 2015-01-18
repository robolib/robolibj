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

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class DigitalOutput extends DigitalIO {
    
    ByteBuffer m_pwmGenerator;
    
    public DigitalOutput(DigitalChannel channel){
        super(channel, Direction.OUT);
    }
    
    public void set(boolean value){
        IntBuffer status = getLE4IntBuffer();
        DIOJNI.setDIO(m_port, (short)(value?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    public void pulse(double pulseLength){
        IntBuffer status = getLE4IntBuffer();
        DIOJNI.pulse(m_port, pulseLength, status);
        HALUtil.checkStatus(status);
    }
    
    public boolean isPulsing(){
        IntBuffer status = getLE4IntBuffer();
        boolean value = DIOJNI.isPulsing(m_port, status) != 0;
        HALUtil.checkStatus(status);
        return value;
    }
    
    public static void setPWMRate(double rate){
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWMRate(rate, status);
        HALUtil.checkStatus(status);
    }
    
    public void enablePWM(double initalRate){
        if(m_pwmGenerator != null)
            return;
        
        IntBuffer status = getLE4IntBuffer();
        m_pwmGenerator = PWMJNI.allocatePWM(status);
        HALUtil.checkStatus(status);
        PWMJNI.setPWMDutyCycle(m_pwmGenerator, initalRate, status);
        HALUtil.checkStatus(status);
        PWMJNI.setPWMOutputChannel(m_pwmGenerator, m_channel.ordinal(), status);
    }
    
    public void disablePWM(){
        if(m_pwmGenerator == null)
            return;
        
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWMOutputChannel(m_pwmGenerator, 26, status);
        HALUtil.checkStatus(status);
        PWMJNI.freePWM(m_pwmGenerator, status);
        m_pwmGenerator = null;
    }
    
    public void updateDutyCycle(double dutyCycle){
        if(m_pwmGenerator == null)
            return;
        
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWMDutyCycle(m_pwmGenerator, dutyCycle, status);
        HALUtil.checkStatus(status);
    }

}
