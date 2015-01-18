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

package io.github.robolib.pneumatic;

import java.nio.ByteBuffer;

import io.github.robolib.util.log.Logger;

/**
 * The Class DoubleSolenoid.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class DoubleSolenoid extends SolenoidBase {
    
    private SolenoidChannel m_forwardChannel;
    private SolenoidChannel m_reverseChannel;
    
    private ByteBuffer m_forwardPort;
    private ByteBuffer m_reversePort;
    
//    public 
    
    public DoubleSolenoid(SolenoidChannel forwardChannel, SolenoidChannel reverseChannel){
        m_forwardChannel = forwardChannel;
        m_reverseChannel = reverseChannel;
        
        m_forwardPort = initChannel(forwardChannel);
        m_reversePort = initChannel(reverseChannel);
    }
    
    public SolenoidChannel getForwardChannel(){
        return m_forwardChannel;
    }
    
    public SolenoidChannel getReverseChannel(){
        return m_reverseChannel;
    }

    /**
     * {@inheritDoc}
     */
    public void set(Value value) {
        switch(value){
        case OFF:
            set(m_forwardPort, kSolenoidOff);
            set(m_reversePort, kSolenoidOff);
            break;
        case ON:
            Logger.get(SolenoidBase.class, "Solenoid").warn("Double Solenoid cant be 'ON'");
            break;
        case FORWARD:
            set(m_forwardPort, kSolenoidOn);
            set(m_reversePort, kSolenoidOff);
            break;
        case REVERSE:
            set(m_forwardPort, kSolenoidOff);
            set(m_forwardPort, kSolenoidOn);
            break;
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public Value get() {
        boolean forward = get(m_forwardPort);
        boolean reverse = get(m_reversePort);
        
        if(forward){
            return Value.FORWARD;
        }else if(reverse){
            return Value.REVERSE;
        }else{
            return Value.OFF;
        }
    }
    
//    public boolean isForwardSolenoidBlacklisted(){
//        int 
//    }
    
    
    

}
