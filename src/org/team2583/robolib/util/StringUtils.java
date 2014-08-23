/*
 * Copyright (c) 2014 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package org.team2583.robolib.util;

/**
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public final class StringUtils {
    
    //If we try to extend or instantiate this class, we should throw a new IllegalAccessException
    private StringUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
    
    public static String buildString(String[] strs){
        return buildStringBuffer(strs).toString();
    }
    
    public static StringBuffer buildStringBuffer(String[] strs){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < strs.length; i++)
            sb.append(strs[i]);
        return sb;
    }
    
    public static StringBuffer buildStringBuffer(StringBuffer[] sbs){
        for(int i = 1; i < sbs.length; i++){
            sbs[0].append(sbs[i]);
        }
        return sbs[0];
    }

}