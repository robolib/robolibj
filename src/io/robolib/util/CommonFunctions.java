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

package io.robolib.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import io.robolib.exception.ChannelIndexException;

/**
 * Common functions for use in the robot code.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class CommonFunctions {
    
    
    /**
     * Get an integer representation of the direction towards an angle.
     *
     * @param to angle to go to
     * @param from angle to come from
     * @return integer representation (-1/0/1) of left/none/right
     */
    public static int directionTo(double to, double from){
        if (to == from){
            return 0;
        }else if ((to >= 0 & from >= 0) | (to < 0 & from < 0)){
            return (to - from) < 0 ? -1 : 1;
        }else if (to < 0 & from >= 0){
            return ((from < 90 ? -1 : 1) * (to > -90 ? -1 : 1));
        }else{
            return ((from < -90 ? -1 : 1) * (to > 90 ? -1 : 1));
        }
    }
    
    public static IntBuffer getLE4IntBuffer(){
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        return status.asIntBuffer();
    }
    
    public static <T extends Enum<T>> T getChannelFromInt(Class<T> channelType, int channel){
        try{
            return channelType.getEnumConstants()[channel];
        }catch(IndexOutOfBoundsException e){
            ChannelIndexException t = new ChannelIndexException("No such " + channelType.getSimpleName()+ " '" + channel + "'");
            t.setStackTrace(e.getStackTrace());
            throw t;
        }
    }

}