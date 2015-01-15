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

package io.robolib.pneumatic;

import static io.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.robolib.exception.ResourceAllocationException;
import io.robolib.hal.HALUtil;
import io.robolib.hal.SolenoidJNI;
import io.robolib.util.log.Loggable;
import io.robolib.util.log.Logger;


/**
 * The Class SolenoidBase.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class SolenoidBase implements Loggable {
    
    /**
     * The Enum Channel.
     */
    public static enum SolenoidChannel{
        
        /**   Solenoid channel 0 on first module. */
        Channel0,

        /**   Solenoid channel 1 on first module. */
        Channel1,

        /**   Solenoid channel 2 on first module. */
        Channel2,

        /**   Solenoid channel 3 on first module. */
        Channel3,

        /**   Solenoid channel 4 on first module. */
        Channel4,

        /**   Solenoid channel 5 on first module. */
        Channel5,

        /**   Solenoid channel 6 on first module. */
        Channel6,

        /**   Solenoid channel 7 on first module. */
        Channel7,

        /**   Solenoid channel 0 on second module. */
        Channel8,

        /**   Solenoid channel 1 on second module. */
        Channel9,

        /**   Solenoid channel 2 on second module. */
        Channel10,

        /**   Solenoid channel 3 on second module. */
        Channel11,

        /**   Solenoid channel 4 on second module. */
        Channel12,

        /**   Solenoid channel 5 on second module. */
        Channel13,

        /**   Solenoid channel 6 on second module. */
        Channel14,

        /**   Solenoid channel 7 on second module. */
        Channel15;
    }
    
    /**
     * The Enum Value.
     */
    public static enum Value {
        
        /** The off. */
        OFF,
        
        /** The on. */
        ON,
        
        /** The forward. */
        FORWARD,
        
        /** The reverse. */
        REVERSE;
    }

    /** Keep track of already used channels. */
    private static boolean m_usedChannels[] = new boolean[SolenoidChannel.values().length];
    
    /** The Constant kSolenoidOff. */
    protected static final byte kSolenoidOff = (byte)0x00;
    
    /** The Constant kSolenoidOn. */
    protected static final byte kSolenoidOn = (byte)0xFF;
    
    
    /**
     * Inits the module.
     *
     * @param channel the channel
     * @return the byte buffer
     */
    protected synchronized static ByteBuffer initChannel(SolenoidChannel channel){
        allocateChannel(channel);
        IntBuffer status = getLE4IntBuffer();
        ByteBuffer port = SolenoidJNI.getPortWithModule((byte) (channel.ordinal() / 8), (byte) channel.ordinal());
        port = SolenoidJNI.initializeSolenoidPort(port, status);
        HALUtil.checkStatus(status);
        return port;
    }
    
    /**
     * Free channel.
     *
     * @param channel the channel
     */
    protected synchronized static void freeChannel(SolenoidChannel channel){
        unallocateChannel(channel);
    }
    
    /**
     * Allocate a Solenoid channel.
     *
     * @param channel the Solenoid channel to allocate
     */
    private synchronized static void allocateChannel(SolenoidChannel channel){

        if(m_usedChannels[channel.ordinal()] == false){
            m_usedChannels[channel.ordinal()] = true;
        }else{
            throw new ResourceAllocationException("Solenoid channel '" + channel.name() + "' already in use.");
        }
    }

    /**
     * Free a Solenoid channel.
     *
     * @param channel the Solenoid channel to free
     */
    private synchronized static void unallocateChannel(SolenoidChannel channel){

        if(m_usedChannels[channel.ordinal()] == true){
            m_usedChannels[channel.ordinal()] = false;
        }else{
            Logger.get(SolenoidBase.class, "Solenoid").error("Solenoid Channel '" + channel.name() + "' was not allocated. How did you get here?");
        }
    }
    
    /**
     * Sets the.
     *
     * @param port the port
     * @param value the value
     */
    protected static final void set(ByteBuffer port, byte value){
        IntBuffer status = getLE4IntBuffer();
        SolenoidJNI.setSolenoid(port, value, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * 
     * @param port
     * @return
     */
    protected static final boolean get(ByteBuffer port){
        IntBuffer status = getLE4IntBuffer();
        byte out = SolenoidJNI.getSolenoid(port, status);
        HALUtil.checkStatus(status);
        return out == kSolenoidOn;
    }
    
    /**
     * Sets the.
     *
     * @param value the value
     */
    public abstract void set(Value value);
    
    /**
     * 
     * @return
     */
    public abstract Value get();
    
//    public byte getPCMSolenoidBlacklist(){
//        IntBuffer status = getLE4IntBuffer();
//        byte retVal = SolenoidJNI.getPCMSolenoidBlackList(pcm_pointer, status)
//    }
    
}
