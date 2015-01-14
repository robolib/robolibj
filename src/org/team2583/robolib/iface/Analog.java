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

package org.team2583.robolib.iface;

import org.team2583.robolib.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import org.team2583.robolib.communication.UsageReporting;
import org.team2583.robolib.exception.ResourceAllocationException;

/**
 * The Class Analog.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Analog extends Interface {

    /**
     * The Enum Channel.
     */
    public static enum AnalogChannel{
        
        /**  Analog Channel 0. */
        Channel0,
        
        /**  Analog Channel 1. */
        Channel1,
        
        /**  Analog Channel 2. */
        Channel2,
        
        /**  Analog Channel 3. */
        Channel3,
        
        /**  Analog Channel 4, Channel 1 on MXP. */
        Channel4,
        
        /**  Analog Channel 5, Channel 2 on MXP. */
        Channel5,
        
        /**  Analog Channel 6, Channel 3 on MXP. */
        Channel6,
        
        /**  Analog Channel 7, Channel 4 on MXP. */
        Channel7;
    }
    
    /**
     * The Enum Direction.
     */
    public static enum Direction{
        
        /** The in. */
        IN,
        
        /** The out. */
        OUT;
    }
    
    /** Keep track of already used channels. */
    private static boolean m_usedInChannels[] = new boolean[kMaxAnalogInputChannels];
    
    /** The m_used out channels. */
    private static boolean m_usedOutChannels[] = new boolean[kMaxAnalogOutputChannels];
     
    /**
     * Instantiates a new analog.
     *
     * @param channel the channel
     * @param dir the dir
     */
    protected Analog(AnalogChannel channel, Direction dir) {
        super(InterfaceType.ANALOG);
        
        switch(dir){
        case IN:
            checkAnalogInputChannel(channel.ordinal());
            if(m_usedInChannels[channel.ordinal()] == true){
                throw new ResourceAllocationException("Analog Input channel '" + channel.name() + "' already in use.");
            }else{
                m_usedInChannels[channel.ordinal()] = true;
            }
        break;
        case OUT:
            checkAnalogOutputChannel(channel.ordinal());
            if(m_usedOutChannels[channel.ordinal()] == true){
                throw new ResourceAllocationException("Analog Output channel '" + channel.name() + "' already in use.");
            }else{
                m_usedOutChannels[channel.ordinal()] = true;
            }
        break;
        }
        
        UsageReporting.report(tResourceType.kResourceType_AnalogChannel, channel.ordinal());
    }
}
