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

package io.github.robolib.iface;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.exception.ResourceAllocationException;
import io.github.robolib.hal.DIOJNI;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.hal.RelayJNI;
import io.github.robolib.util.log.Logger;


/**
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class Relay extends Interface {
    
    public static enum RelayChannel {
        Channel0,
        Channel1,
        Channel2,
        Channel3;
    }
    
    public static enum Value {
        OFF,
        FORWARD,
        REVERSE,
        ON;
    }
    
    public static enum Direction {
        NONE,
        FORWARD,
        REVERSE,
        BOTH;
    }
    
    private static final byte kRelayOn = (byte) 1;
    private static final byte kRelayOff = (byte) 0;
    
    
    protected final String m_description;
    
    /** The The RoboRIO port identifier. */
    private ByteBuffer m_port;

    /** The Relay Channel this Relay is operating on. */
    private RelayChannel m_channel;
    
    /** The Direction this Relay is allowed to operate in */
    private Direction m_direction;
    
    /** Keep track of already used channels. */
    private static boolean m_usedChannels[] = new boolean[kMaxRelayChannels];
    
    /**
     * 
     * @param channel
     */
    public Relay(RelayChannel channel) {
        this(channel, Direction.BOTH, "Relay Ch" + channel.ordinal());
    }
    
    /**
     * 
     * @param channel
     * @param dir
     */
    public Relay(RelayChannel channel, Direction dir){
        this(channel, dir, "Relay Ch" + channel.ordinal());
    }

    /**
     * 
     * @param channel
     * @param dir
     * @param desc
     */
    public Relay(RelayChannel channel, Direction dir, String desc){
        super(InterfaceType.RELAY);
        m_description = desc;
        m_direction = dir;
        
        if(m_usedChannels[channel.ordinal()] == false){
            m_usedChannels[channel.ordinal()] = true;
        }else{
            throw new ResourceAllocationException("Cannot create '" + desc + "', Relay channel '" + channel.name() + "' already in use.");
        }
        
        IntBuffer status = getLE4IntBuffer();
        m_port = DIOJNI.initializeDigitalPort(DIOJNI.getPort((byte)channel.ordinal()), status);
        HALUtil.checkStatus(status);
        set(Value.OFF);
        
        UsageReporting.report(UsageReporting.kResourceType_Relay, channel.ordinal());
        
    }
    
    /**
     * Free the Relay channel.
     */
    public void free(){
        if(m_usedChannels[m_channel.ordinal()] == true){
            m_usedChannels[m_channel.ordinal()] = false;
        }else{
            Logger.get(Relay.class).error("Relay Channel '" + getChannelName() + "' was not allocated. How did you get here?");
        }
        
        IntBuffer status = getLE4IntBuffer();
        set(Value.OFF);
        DIOJNI.freeDIO(m_port, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * 
     * @param value
     */
    public void set(boolean value){
        set(value?Value.ON:Value.OFF);
    }
    
    /**
     * 
     * @param value
     */
    public void set(Value value){
        IntBuffer status = getLE4IntBuffer();
        switch(value){
        case OFF:
            RelayJNI.setRelayForward(m_port, kRelayOff, status);
            RelayJNI.setRelayReverse(m_port, kRelayOff, status);
            break;
        case FORWARD:
            RelayJNI.setRelayReverse(m_port, kRelayOff, status);
            if(m_direction == Direction.REVERSE)
                Logger.get(Relay.class).warn("Relay '" + m_description + "' configured for REVERSE. cannot go FORWARD.");
            else
                RelayJNI.setRelayForward(m_port, kRelayOn, status);
            break;
        case REVERSE:
            RelayJNI.setRelayForward(m_port, kRelayOff, status);
            if(m_direction == Direction.FORWARD)
                Logger.get(Relay.class).warn("Relay '" + m_description + "' configured for FORWARD. cannot go REVERSE.");
            else
                RelayJNI.setRelayReverse(m_port, kRelayOn, status);
            break;
        case ON:
            if((m_direction.ordinal() & 1) != 0)
                RelayJNI.setRelayForward(m_port, kRelayOn, status);
            if((m_direction.ordinal() & 2) != 0)
                RelayJNI.setRelayReverse(m_port, kRelayOn, status);
            break;
        }
        HALUtil.checkStatus(status);
    }
    
    /**
     * 
     * @return
     */
    public Value get(){
        IntBuffer status = getLE4IntBuffer();
        int forward = RelayJNI.getRelayForward(m_port, status);
        int reverse = RelayJNI.getRelayReverse(m_port, status) << 1;
        return Value.values()[(forward | reverse)];
    }
    
    /**
     * 
     * @param dir
     */
    public void setDirection(Direction dir){
        set(Value.OFF);
        m_direction = dir;
    }
    
    /**
     * The channel this Relay is operating on.
     *
     * @return {@link RelayChannel} representation of the Relay channel
     */
    public RelayChannel getChannel(){
        return m_channel;
    }

    /**
     * The channel this Relay is operating on, in integer form.
     *
     * @return integer representation of the Relay channel
     */
    public int getChannelNumber(){
        return m_channel.ordinal();
    }

    /**
     * The channel this Relay is operating on, in string form.
     *
     * @return string representation of the Relay channel
     */
    public String getChannelName(){
        return m_channel.name();
    }

}
