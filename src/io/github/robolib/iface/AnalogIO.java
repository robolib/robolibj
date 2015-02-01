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

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.hal.AnalogJNI;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.lang.ResourceAllocationException;

/**
 * The Class AnalogIO.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class AnalogIO extends Interface {

    /**
     * The Enum Channel.
     */
    public static enum AnalogChannel {
        
        /**  AnalogIO Channel 0. */
        Channel0,
        
        /**  AnalogIO Channel 1. */
        Channel1,
        
        /**  AnalogIO Channel 2. */
        Channel2,
        
        /**  AnalogIO Channel 3. */
        Channel3,
        
        /**  AnalogIO Channel 4, Channel 1 on MXP. */
        Channel4,
        
        /**  AnalogIO Channel 5, Channel 2 on MXP. */
        Channel5,
        
        /**  AnalogIO Channel 6, Channel 3 on MXP. */
        Channel6,
        
        /**  AnalogIO Channel 7, Channel 4 on MXP. */
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
    
    
    protected ByteBuffer m_port;
    protected AnalogChannel m_channel;
     
    /**
     * Instantiates a new analog.
     *
     * @param channel the channel
     * @param dir the dir
     */
    protected AnalogIO(AnalogChannel channel, Direction dir) {
        super(InterfaceType.ANALOG);
        
        ByteBuffer port = AnalogJNI.getPort((byte) channel.ordinal());
        IntBuffer status = getLE4IntBuffer();

        switch(dir){
        case IN:
            checkAnalogInputChannel(channel.ordinal());
            if(m_usedInChannels[channel.ordinal()] == true)
                throw new ResourceAllocationException("AnalogIO Input channel '" + channel.name() + "' already in use.");
            
            m_usedInChannels[channel.ordinal()] = true;
            m_port = AnalogJNI.initializeAnalogInputPort(port, status);
        break;
        case OUT:
            checkAnalogOutputChannel(channel.ordinal());
            if(m_usedOutChannels[channel.ordinal()] == true)
                throw new ResourceAllocationException("AnalogIO Output channel '" + channel.name() + "' already in use.");
            
            m_usedOutChannels[channel.ordinal()] = true;
            m_port = AnalogJNI.initializeAnalogOutputPort(port, status);
            
        break;
        }
        
        HALUtil.checkStatus(status);
        
        UsageReporting.report(UsageReporting.kResourceType_AnalogChannel, channel.ordinal());
    }
    
    /**
     * Channel destructor.
     */
    public void free(){
        m_channel = null;
    }
    
    
    /**
     * Get the AnalogChannel.
     *
     * @return The channel AnalogChannel.
     */
    public AnalogChannel getChannel(){
        return m_channel;
    }
    
    /**
     * Get the channel number.
     *
     * @return The channel number.
     */
    public int getChannelNumber(){
        return m_channel.ordinal();
    }
    
    
}
