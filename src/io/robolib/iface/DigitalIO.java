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

package io.robolib.iface;

import static io.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.robolib.communication.UsageReporting;
import io.robolib.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import io.robolib.exception.ResourceAllocationException;
import io.robolib.hal.DIOJNI;
import io.robolib.hal.HALUtil;
import io.robolib.util.log.Logger;


/**
 * The Class DigitalIO.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class DigitalIO extends Interface {

    /**
     * The Enum Channel.
     */
    public static enum DigitalChannel{

        /**  DIO Channel 0 On-board. */
        Channel0,

        /**  DIO Channel 1 On-board. */
        Channel1,

        /**  DIO Channel 2 On-board. */
        Channel2,

        /**  DIO Channel 3 On-board. */
        Channel3,

        /**  DIO Channel 4 On-board. */
        Channel4,

        /**  DIO Channel 5 On-board. */
        Channel5,

        /**  DIO Channel 6 On-board. */
        Channel6,

        /**  DIO Channel 7 On-board. */
        Channel7,

        /**  DIO Channel 8 On-board. */
        Channel8,

        /**  DIO Channel 9 On-board. */
        Channel9,

        /**  DIO Channel 10, Channel 0 on MXP. */
        Channel10(11),

        /**  DIO Channel 11, Channel 1 on MXP. */
        Channel11(13),

        /**  DIO Channel 12, Channel 2 on MXP. */
        Channel12(15),

        /**  DIO Channel 13, Channel 3 on MXP. */
        Channel13(17),

        /**  DIO Channel 14, Channel 4 on MXP. */
        Channel14(19),

        /**  DIO Channel 15, Channel 5 on MXP. */
        Channel15(21),

        /**  DIO Channel 16, Channel 6 on MXP. */
        Channel16(23),

        /**  DIO Channel 17, Channel 7 on MXP. */
        Channel17(25),

        /**  DIO Channel 18, Channel 8 on MXP. */
        Channel18(27),

        /**  DIO Channel 19, Channel 9 on MXP. */
        Channel19(29),

        /**  DIO Channel 20, Channel 10 on MXP. */
        Channel20(31),

        /**  DIO Channel 21, Channel 11 on MXP. */
        Channel21(18),

        /**  DIO Channel 22, Channel 12 on MXP. */
        Channel22(22),

        /**  DIO Channel 23, Channel 13 on MXP. */
        Channel23(26),

        /**  DIO Channel 24, Channel 14 on MXP. */
        Channel24(32),

        /**  DIO Channel 25, Channel 15 on MXP. */
        Channel25(34);

        /** The Pin on the MXP port that this channel is on. */
        public final int m_mxpPin;

        /**
         * Instantiates a new channel.
         */
        private DigitalChannel(){
            m_mxpPin = 0;
        }

        /**
         * Instantiates a new channel.
         *
         * @param mxpPin the mxp pin
         */
        private DigitalChannel(int mxpPin){
            m_mxpPin = mxpPin;
        }
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
    private static boolean m_usedChannels[] = new boolean[kMaxDigitalChannels];

    /** The The RoboRIO port identifier. */
    private ByteBuffer m_port;

    /** The DigitalIO Channel this DigitalIO is operating on. */
    private DigitalChannel m_channel;

    /**
     * Instantiates a new DigitalIO.
     *
     * @param channel the channel for this DigitalIO
     */
    protected DigitalIO(DigitalChannel channel, Direction dir) {
        super(InterfaceType.DIGITALIO);

        allocateChannel(channel);

        m_channel = channel;
        
        if(dir == Direction.IN)        
            UsageReporting.report(tResourceType.kResourceType_DigitalOutput, channel.ordinal());
        else
            UsageReporting.report(tResourceType.kResourceType_DigitalOutput, channel.ordinal());
    }

    /**
     * Free the DigitalIO channel.
     *
     * Free the resource associated with the Digital IO channel and set the value to 0.
     */
    public void free() {

        freeChannel(getChannel());

        IntBuffer status = getLE4IntBuffer();
        DIOJNI.freeDIO(m_port, status);
        HALUtil.checkStatus(status);

        m_channel = null;
    }

    /**
     * Allocate a DigitalIO channel.
     *
     * @param channel the DigitalIO channel to allocate
     */
    private void allocateChannel(DigitalChannel channel){
        if(channel.ordinal() > 9){
            allocateMXPPin(channel.m_mxpPin);
        }

        if(m_usedChannels[channel.ordinal()] == false){
            m_usedChannels[channel.ordinal()] = true;
        }else{
            throw new ResourceAllocationException("Digital IO channel '" + channel.name() + "' already in use.");
        }
    }

    /**
     * Free a DigitalIO channel.
     *
     * @param channel the DigitalIO channel to free
     */
    private void freeChannel(DigitalChannel channel){
        if(channel.ordinal() > 9){
            freeMXPPin(channel.m_mxpPin);
        }

        if(m_usedChannels[channel.ordinal()] == true){
            m_usedChannels[channel.ordinal()] = false;
        }else{
            Logger.get(DigitalIO.class).error("Digital IO Channel '" + channel.name() + "' was not allocated. How did you get here?");
        }
    }

    /**
     * The channel this DigitalIO is operating on.
     *
     * @return {@link DigitalChannel} representation of the DigitalIO channel
     */
    public DigitalChannel getChannel(){
        return m_channel;
    }

    /**
     * The channel this DigitalIO is operating on, in integer form.
     *
     * @return integer representation of the DigitalIO channel
     */
    public int getChannelNumber(){
        return m_channel.ordinal();
    }

    /**
     * The channel this DigitalIO is operating on, in string form.
     *
     * @return string representation of the DigitalIO channel
     */
    public String getChannelName(){
        return m_channel.name();
    }

}
