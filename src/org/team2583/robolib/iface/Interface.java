/*
 * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package org.team2583.robolib.iface;

import java.util.HashMap;
import java.util.Map;

import org.team2583.robolib.exception.ResourceAllocationException;

import edu.wpi.first.wpilibj.communication.UsageReporting;

/**
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 *
 */
public abstract class Interface {

    public static final int kMaxDigitalChannels = 26;
    
    public static final int kMaxAnalogInputChannels = 8;

    public static final int kMaxAnalogOutputChannels = 2;

    public static final int kMaxPWMChannels = 20;

    public static final int kMaxRelayChannels = 4;
    
    protected static void checkDigitalChannel(final int channel){
        if(channel < 0 || channel > kMaxDigitalChannels)
            throw new IndexOutOfBoundsException("Bad Digital Channel");
    }
    
    protected static void checkAnalogInputChannel(final int channel){
        if(channel < 0 || channel > kMaxAnalogInputChannels)
            throw new IndexOutOfBoundsException("Bad Analog Input Channel");
    }
    
    protected static void checkAnalogOutputChannel(final int channel){
        if(channel < 0 || channel > kMaxAnalogOutputChannels)
            throw new IndexOutOfBoundsException("Bad Analog Output Channel");
    }
    
    protected static void checkPWMChannel(final int channel){
        if(channel < 0 || channel > kMaxPWMChannels)
            throw new IndexOutOfBoundsException("Bad PWM Channel");
    }
    
    protected static void checkRelayChannel(final int channel){
        if(channel < 0 || channel > kMaxRelayChannels)
            throw new IndexOutOfBoundsException("Bad Relay Channel");
    }

    private InterfaceType m_ifaceType;
    
    private static Map<Integer, InterfaceType> m_mxpMap = new HashMap<Integer, InterfaceType>();
    
    protected Interface(InterfaceType iType, int channel){
        m_ifaceType = iType;
        //m_address = address;
        
        UsageReporting.report(iType.m_resource, channel);
    }
    
    protected void checkMXPPin(InterfaceType type, int pin){
        if(pin > 0){
            if(m_mxpMap.containsKey(pin) || !m_mxpMap.get(pin).equals(type)){
                throw new ResourceAllocationException("Port already allocated as '" + m_mxpMap.get(pin).name() + "'.");
            }else{
                m_mxpMap.put(pin, type);
            }
        }
    }
    
}
