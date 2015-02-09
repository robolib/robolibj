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
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.util.log.Logger;


/**
 * The Class DigitalIO.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class DigitalIO extends InterruptBase {
 
    /**
     * The Enum Channel.
     * 
     * @author noriah Reuland <vix@noriah.dev>
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
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum Direction{

        /** The in. */
        IN,

        /** The out. */
        OUT;
    }

    /** Keep track of already used channels. */
    private static final boolean USED_CHANNELS[] = new boolean[MAX_DIGITAL_CHANNELS];

    /** The The RoboRIO port identifier. */
    protected ByteBuffer m_port;

    /** The DigitalIO Channel this DigitalIO is operating on. */
    protected DigitalChannel m_channel;
    
    /** The direction of this digital io. */
    protected Direction m_direction;

    /**
     * Instantiates a new DigitalIO.
     *
     * @param channel the channel for this DigitalIO
     */
    protected DigitalIO(DigitalChannel channel, Direction dir) {
        super(InterfaceType.DIGITALIO);

        allocateChannel(channel);

        m_channel = channel;
        
        byte isIn = (byte) (dir.equals(Direction.IN) ? 1 : 0);
        
        IntBuffer status = getLE4IntBuffer();
        
        m_port = DIOJNI.initializeDigitalPort(DIOJNI.getPort((byte)channel.ordinal()), status);
        HALUtil.checkStatus(status);
        DIOJNI.allocateDIO(m_port, isIn, status);
        HALUtil.checkStatus(status);
        
        UsageReporting.report((byte) (UsageReporting.ResourceType_DigitalOutput - isIn), channel.ordinal());
    }
    
    /**
     * Constructor for use with analog trigger output. NOTHING ELSE!!!
     */
    protected DigitalIO(){
        super(InterfaceType.ANALOG);
    }

    /**
     * Free the DigitalIO channel.
     *
     * Free the resource associated with the Digital IO channel and set the value to 0.
     */
    public final void free() {

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
    private final void allocateChannel(DigitalChannel channel){
        if(channel.ordinal() > 9){
            allocateMXPPin(channel.m_mxpPin);
        }

        if(USED_CHANNELS[channel.ordinal()] == false){
            USED_CHANNELS[channel.ordinal()] = true;
        }else{
            throw new ResourceAllocationException("Digital IO channel '" + channel.name() + "' already in use.");
        }
    }

    /**
     * Free a DigitalIO channel.
     *
     * @param channel the DigitalIO channel to free
     */
    private final void freeChannel(DigitalChannel channel){
        if(channel.ordinal() > 9){
            freeMXPPin(channel.m_mxpPin);
        }

        if(USED_CHANNELS[channel.ordinal()] == true){
            USED_CHANNELS[channel.ordinal()] = false;
        }else{
            Logger.get(DigitalIO.class).error("Digital IO Channel '" + channel.name() + "' was not allocated. How did you get here?");
        }
    }

    /**
     * The channel this DigitalIO is operating on.
     *
     * @return {@link DigitalChannel} representation of the DigitalIO channel
     */
    public final DigitalChannel getChannel(){
        return m_channel;
    }

    /**
     * The channel this DigitalIO is operating on, in integer form.
     *
     * @return integer representation of the DigitalIO channel
     */
    @Override
    public int getChannelNumber(){
        return m_channel.ordinal();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public byte getModuleNumber(){
        return 0;
    }

    /**
     * The channel this DigitalIO is operating on, in string form.
     *
     * @return string representation of the DigitalIO channel
     */
    public String getChannelName(){
        return m_channel.name();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAnalogTrigger(){
        return false;
    }

}
