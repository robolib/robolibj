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

package io.github.robolib.util;

/**
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 *
 */
public class MathUtils {
    
    public static double clamp(double value, double min, double max){
        return Math.max(Math.min(value, max), min);
    }
    
    public static float clamp(float value, float min, float max){
        return Math.max(Math.min(value, max), min);
    }
    
    public static int clamp(int value, int min, int max){
        return Math.max(Math.min(value, max), min);
    }
    
    public static boolean inBounds(double value, double min, double max){
        return (min <= value) && (value <= max);
    }
    
    public static boolean inBounds(float value, float min, float max){
        return (min <= value) && (value <= max);
    }
    
    public static boolean inBounds(int value, int min, int max){
        return (min <= value) && (value <= max);
    }
    
    public static double squareSign(double value){
        if(value < 0){
            return -(value * value);
        }
        return value * value;
    }
        

}