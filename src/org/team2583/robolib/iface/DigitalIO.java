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
 * 
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public class DigitalIO extends Interface {


    /**
     * The Enum Channel.
     */
    public static enum Channel{
        
        /** DIO Channel 0 On-board */
        Channel0,
        /** DIO Channel 1 On-board */
        Channel1,
        /** DIO Channel 2 On-board */
        Channel2,
        /** DIO Channel 3 On-board */
        Channel3,
        /** DIO Channel 4 On-board */
        Channel4,
        /** DIO Channel 5 On-board */
        Channel5,
        /** DIO Channel 6 On-board */
        Channel6,
        /** DIO Channel 7 On-board */
        Channel7,
        /** DIO Channel 8 On-board */
        Channel8,
        /** DIO Channel 9 On-board */
        Channel9,
        /** DIO Channel 10, Channel 0 on MXP */
        Channel10(11),
        /** DIO Channel 11, Channel 1 on MXP */
        Channel11(13),
        /** DIO Channel 12, Channel 2 on MXP */
        Channel12(15),
        /** DIO Channel 13, Channel 3 on MXP */
        Channel13(17),
        /** DIO Channel 14, Channel 4 on MXP */
        Channel14(19),
        /** DIO Channel 15, Channel 5 on MXP */
        Channel15(21),
        /** DIO Channel 16, Channel 6 on MXP */
        Channel16(23),
        /** DIO Channel 17, Channel 7 on MXP */
        Channel17(25),
        /** DIO Channel 18, Channel 8 on MXP */
        Channel18(27),
        /** DIO Channel 19, Channel 9 on MXP */
        Channel19(29),
        /** DIO Channel 20, Channel 10 on MXP */
        Channel20(31),
        /** DIO Channel 21, Channel 11 on MXP */
        Channel21(18),
        /** DIO Channel 22, Channel 12 on MXP */
        Channel22(22),
        /** DIO Channel 23, Channel 13 on MXP */
        Channel23(26),
        /** DIO Channel 24, Channel 14 on MXP */
        Channel24(32),
        /** DIO Channel 25, Channel 15 on MXP */
        Channel25(34);
        
        public final int m_mxpPin;
        
        private Channel(){ m_mxpPin = 0; }
        private Channel(int mxpPin){ m_mxpPin = mxpPin; }
    }
    
    /** Keep track of already used channels. */
    private static boolean m_usedChannels[] = new boolean[kMaxDigitalChannels];
    
    private Channel m_channel;
    
    /**
     * 
     * @param channel
     */
    protected DigitalIO(Channel channel) {
        super(InterfaceType.DIGITALIO, channel.ordinal());
        
        if(channel.ordinal() > 9)
            allocateMXPPin(InterfaceType.DIGITALIO, channel.m_mxpPin);
        
        if(m_usedChannels[channel.ordinal()] == true){
            throw new ResourceAllocationException("DIO channel '" + channel.name() + "' already in use.");
        }else{
            m_usedChannels[channel.ordinal()] = true;
        }
        
        m_channel = channel;
    }

}
