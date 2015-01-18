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
import io.github.robolib.exception.ResourceAllocationException;
import io.github.robolib.hal.DIOJNI;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.iface.PWM.PWMChannel;
import io.github.robolib.util.log.Logger;

import edu.wpi.first.wpilibj.hal.RelayJNI;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
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
     * @param channel 
     */
    public Relay(RelayChannel channel) {
        this(channel, Direction.BOTH, "Relay Ch" + channel.ordinal());
    }
    
    public Relay(RelayChannel channel, Direction dir){
        this(channel, dir, "Relay Ch" + channel.ordinal());
    }
    
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
     * Free the PWM channel.
     *
     * @param channel the PWM channel to free
     */
    public void free(){
        if(m_usedChannels[m_channel.ordinal()] == true){
            m_usedChannels[m_channel.ordinal()] = false;
        }else{
            Logger.get(Relay.class).error("Relay Channel '" + getChannelName() + "' was not allocated. How did you get here?");
        }
    }
    
    public void set(boolean value){
        set(value?Value.ON:Value.OFF);
    }
    
    public void set(Value value){
        IntBuffer status = getLE4IntBuffer();
        switch(value){
        case OFF:
//            if((m_direction.ordinal() & 1) != 0)
                RelayJNI.setRelayForward(m_port, kRelayOff, status);
//            if((m_direction.ordinal() & 2) != 0)
                RelayJNI.setRelayReverse(m_port, kRelayOff, status);
            break;
        case FORWARD:
            if(m_direction == Direction.REVERSE)
                Logger.get(Relay.class).warn("Relay '" + m_description + "' configured for REVERSE. cannot go FORWARD.");
            else
                RelayJNI.setRelayForward(m_port, kRelayOn, status);
            
//            if(m_direction == Direction.BOTH)
                RelayJNI.setRelayReverse(m_port, kRelayOff, status);
            break;
        case REVERSE:
            if(m_direction == Direction.FORWARD)
                Logger.get(Relay.class).warn("Relay '" + m_description + "' configured for FORWARD. cannot go REVERSE.");
            else
                RelayJNI.setRelayReverse(m_port, kRelayOn, status);
            
//            if(m_direction == Direction.BOTH)
                RelayJNI.setRelayForward(m_port, kRelayOff, status);
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
    
    public Value get(){
        IntBuffer status = getLE4IntBuffer();
        int forward = RelayJNI.getRelayForward(m_port, status);
        int reverse = RelayJNI.getRelayReverse(m_port, status) << 1;
        return Value.values()[(forward | reverse)];
    }
    
    public void setDirection(Direction dir){
        
    }
    
    /**
     * The channel this PWM is operating on.
     *
     * @return {@link PWMChannel} representation of the PWM channel
     */
    public RelayChannel getChannel(){
        return m_channel;
    }

    /**
     * The channel this PWM is operating on, in integer form.
     *
     * @return integer representation of the PWM channel
     */
    public int getChannelNumber(){
        return m_channel.ordinal();
    }

    /**
     * The channel this PWM is operating on, in string form.
     *
     * @return string representation of the PWM channel
     */
    public String getChannelName(){
        return m_channel.name();
    }

}
