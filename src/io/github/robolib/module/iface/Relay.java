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

package io.github.robolib.module.iface;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.jni.DIOJNI;
import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.RelayJNI;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.util.log.Logger;


/**
 * Class for VEX Robotics Spike style relay outputs. Relays are intended to be
 * connected to Spikes or similar relays. The relay channels controls a pair of
 * pins that are either both off, one on, the other on, or both on. This
 * translates into two Spike outputs at 0v, one at 12v and one at 0v, one at 0v
 * and the other at 12v, or two Spike outputs at 12V. This allows off, full
 * forward, or full reverse control of motors without variable speed. It also
 * allows the two channels (forward and reverse) to be used independently for
 * something that does not care about voltage polarity (like a solenoid).
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class Relay extends Interface {
    
    /**
     * Enum representation of Relay channels on the RIO
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum RelayChannel {
        Channel0,
        Channel1,
        Channel2,
        Channel3;
    }
    
    /**
     * Set the state of the relay
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum RelayValue {
        OFF,
        FORWARD,
        REVERSE,
        ON;
    }
    
    /**
     * Set the direction the relay can go in
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum RelayDirection {
        FORWARD(1),
        REVERSE(2),
        BOTH(3);
        public byte value;

        private RelayDirection(int val) {
            value = (byte)val;
        }
    }
    
    private static final byte RELAY_ON = (byte) 1;
    private static final byte RELAY_OFF = (byte) 0;
    
    
    protected final String m_description;
    
    /** The The RoboRIO port identifier. */
    private final ByteBuffer m_port;

    /** The Relay Channel this Relay is operating on. */
    private RelayChannel m_channel;
    
    /** The Direction this Relay is allowed to operate in */
    private RelayDirection m_direction;
    
    /** Keep track of already used channels. */
    private static final boolean USED_CHANNELS[] = new boolean[MAX_RELAY_CHANNELS];
    
    /**
     * Relay constructor given a channel, allowing both directions.
     *
     * @param channel The {@link RelayChannel} for this relay.
     */
    public Relay(RelayChannel channel) {
        this(channel, RelayDirection.BOTH, "Relay Ch" + channel.ordinal());
    }
    
    /**
     * Relay constructor given a channel.
     *
     * @param channel The {@link RelayChannel} for this relay.
     * @param dir The direction that the Relay object will control.
     */
    public Relay(RelayChannel channel, RelayDirection dir){
        this(channel, dir, "Relay Ch" + channel.ordinal());
    }

    /**
     * Relay constructor given a channel.
     *
     * @param channel The {@link RelayChannel} for this relay.
     * @param dir The direction that the Relay object will control.
     * @param desc Description of this relay for debugging, dash-board, 
     * and power monitoring purposes
     */
    public Relay(RelayChannel channel, RelayDirection dir, String desc){
        super(InterfaceType.RELAY);
        m_description = desc;
        m_direction = dir;
        
        if(USED_CHANNELS[channel.ordinal()] == false){
            USED_CHANNELS[channel.ordinal()] = true;
        }else{
            throw new ResourceAllocationException("Cannot create '" + desc + "', Relay channel '" + channel.name() + "' already in use.");
        }
        
        IntBuffer status = getLE4IntBuffer();
        m_port = DIOJNI.initializeDigitalPort(DIOJNI.getPort((byte)channel.ordinal()), status);
        HALUtil.checkStatus(status);
        set(RelayValue.OFF);
        
        UsageReporting.report(UsageReporting.ResourceType_Relay, channel.ordinal());
        
    }
    
    /**
     * Free the Relay channel.
     */
    public void free(){
        if(USED_CHANNELS[m_channel.ordinal()] == true){
            USED_CHANNELS[m_channel.ordinal()] = false;
        }else{
            Logger.get(Relay.class).error("Relay Channel '" + getChannelName() + "' was not allocated. How did you get here?");
        }
        
        IntBuffer status = getLE4IntBuffer();
        set(RelayValue.OFF);
        DIOJNI.freeDIO(m_port, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * 
     * @param value
     */
    public void set(boolean value){
        set(value?RelayValue.ON:RelayValue.OFF);
    }
    
    /**
     * Set the relay state.
     *
     * Valid values depend on which directions of the relay are controlled by
     * the object.
     *
     * When set to {@link RelayDirection#BOTH}, the relay can be set to any of the four
     * states: 0v-0v, 12v-0v, 0v-12v, 12v-12v
     *
     * When set to FORWARD or REVERSE, you can specify the constant
     * for the direction or you can simply specify OFF and ON. Using
     * only OFF and ON is recommended.
     *
     * @param value The state to set the relay.
     */
    public void set(RelayValue value){
        IntBuffer status = getLE4IntBuffer();
        switch(value){
        case OFF:
            RelayJNI.setRelayForward(m_port, RELAY_OFF, status);
            RelayJNI.setRelayReverse(m_port, RELAY_OFF, status);
            break;
        case FORWARD:
            RelayJNI.setRelayReverse(m_port, RELAY_OFF, status);
            if(m_direction == RelayDirection.REVERSE)
                Logger.get(Relay.class).warn("Relay '" + m_description + "' configured for REVERSE. cannot go FORWARD.");
            else
                RelayJNI.setRelayForward(m_port, RELAY_ON, status);
            break;
        case REVERSE:
            RelayJNI.setRelayForward(m_port, RELAY_OFF, status);
            if(m_direction == RelayDirection.FORWARD)
                Logger.get(Relay.class).warn("Relay '" + m_description + "' configured for FORWARD. cannot go REVERSE.");
            else
                RelayJNI.setRelayReverse(m_port, RELAY_ON, status);
            break;
        case ON:
            if((m_direction.value & 1) != 0)
                RelayJNI.setRelayForward(m_port, RELAY_ON, status);
            if((m_direction.value & 2) != 0)
                RelayJNI.setRelayReverse(m_port, RELAY_ON, status);
            break;
        }
        HALUtil.checkStatus(status);
    }
    
    /**
     * Get the Relay State
     *
     * Gets the current state of the relay.
     *
     * When set to FORWARD or REVERSE, value is returned as ON/OFF
     * not FORWARD/REVERSE (per the recommendation in Set)
     *
     * @return The current state of the relay as a {@link RelayValue}
     */
    public RelayValue get(){
        IntBuffer status = getLE4IntBuffer();
        int forward = RelayJNI.getRelayForward(m_port, status);
        int reverse = RelayJNI.getRelayReverse(m_port, status) << 1;
        return RelayValue.values()[(forward | reverse)];
    }
    
    /**
     * Set the Relay Direction
     *
     * Changes which values the relay can be set to depending on which direction
     * is used
     *
     * Valid inputs are FORWARD, REVERSE, and BOTH
     *
     * @param dir The direction for the relay to operate in
     */
    public void setDirection(RelayDirection dir){
        set(RelayValue.OFF);
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
