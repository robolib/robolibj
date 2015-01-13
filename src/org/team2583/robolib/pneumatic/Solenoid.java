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

package org.team2583.robolib.pneumatic;

import java.nio.ByteBuffer;

import org.team2583.robolib.util.log.Logger;

/**
 * The Class Solenoid.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Solenoid extends SolenoidBase {
    
    
    private ByteBuffer m_port;
    private Channel m_channel;
    
    public Solenoid(Channel channel){
        m_channel = channel;
        m_port = initChannel(channel);
        
    }
    
    public Channel getChannel(){
        return m_channel;
    }
    
    /**
     * {@inheritDoc}
     */
    public void set(Value value){
        switch(value){
        case OFF:
            set(m_port, kSolenoidOff);
            break;
        case ON:
            set(m_port, kSolenoidOn);
            break;
        case FORWARD:
            Logger.get(SolenoidBase.class, "Solenoid").warn("Single solenoid cannot go Forward!");
            break;
        case REVERSE:
            Logger.get(SolenoidBase.class, "Solenoid").warn("Single solenoid cannot go in Reverse!");
            break;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Value get(){
        return get(m_port) ? Value.ON : Value.OFF;
    }

}
