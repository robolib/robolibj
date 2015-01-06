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

import org.team2583.robolib.exception.ResourceAllocationException;

/**
 * The Class PWM.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PWM extends Interface {

    
    /**
     * The Enum Channel.
     */
    public static enum Channel{
        
        /** PWM Channel 0 On-board */
        Channel0,
        /** PWM Channel 1 On-board */
        Channel1,
        /** PWM Channel 2 On-board */
        Channel2,
        /** PWM Channel 3 On-board */
        Channel3,
        /** PWM Channel 4 On-board */
        Channel4,
        /** PWM Channel 5 On-board */
        Channel5,
        /** PWM Channel 6 On-board */
        Channel6,
        /** PWM Channel 7 On-board */
        Channel7,
        /** PWM Channel 8 On-board */
        Channel8,
        /** PWM Channel 9 On-board */
        Channel9,
        /** PWM Channel 10, Channel 0 on MXP */
        Channel10(11),
        /** PWM Channel 11, Channel 1 on MXP */
        Channel11(13),
        /** PWM Channel 12, Channel 2 on MXP */
        Channel12(15),
        /** PWM Channel 13, Channel 3 on MXP */
        Channel13(17),
        /** PWM Channel 14, Channel 4 on MXP */
        Channel14(27),
        /** PWM Channel 15, Channel 5 on MXP */
        Channel15(29),
        /** PWM Channel 16, Channel 6 on MXP */
        Channel16(31),
        /** PWM Channel 17, Channel 7 on MXP */
        Channel17(18),
        /** PWM Channel 18, Channel 8 on MXP */
        Channel18(22),
        /** PWM Channel 19, Channel 9 on MXP */
        Channel19(26);
        
        public final int m_mxpPin;
        
        private Channel(){ m_mxpPin = 0; }
        private Channel(int mxpPin){ m_mxpPin = mxpPin; }
    }
    
    /** Keep track of already used channels. */
    private static boolean m_usedChannels[] = new boolean[kMaxPWMChannels];
    
    private Channel m_channel;
    
    /**
     * Instantiates a new pwm.
     *
     * @param iType the i type
     * @param channel the channel
     */
    protected PWM(Channel channel) {
        super(InterfaceType.PWM, channel.ordinal());
        
        if(channel.ordinal() > 9){
            checkMXPPin(InterfaceType.PWM, channel.m_mxpPin);
        }
        
        if(m_usedChannels[channel.ordinal()] == true){
            throw new ResourceAllocationException("PWM channel '" + channel.name() + "' already in use.");
        }else{
            m_usedChannels[channel.ordinal()] = true;
        }
        
        m_channel = channel;

    }
    
    protected void initPWM(){
        
    }

}
