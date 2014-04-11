/*
 * Copyright (c) 2014 noriah vix@noriah.dev.
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

package org.warriors2583.robolib.lang;

/**
 *
 * @author noriah Reuland
 */
public class BooleanArray extends Number implements Comparable {
    public static final int MIN_VALUE = 0x00000000;
    public static final int MAX_VALUE = 0x7fffffff;

    public static final Class TYPE = Integer.TYPE;

    private final int value;
    
    public BooleanArray(boolean arg){
    	value = (arg ? 1 : 0);
    }

    public BooleanArray(boolean[] args){
        int n = 0, l = args.length;
        for (int i = 0; i < l; ++i) {
            n = (n << 1) + (args[i] ? 1 : 0);
        }
        value = n;
    }

    public byte byteValue(){
    	return (byte)value;
    }

    public short shortValue(){
    	return (short)value;
    }

    public int intValue(){
    	return value;
    }

    public long longValue(){
    	return (long)value;
    }

    public float floatValue(){
    	return (float)value;
    }

    public double doubleValue(){
    	return (double)value;
    }

    public String toString() {
    	return Integer.toString(value);
    }

    public int hashCode(){
    	return value;
    }

    public boolean equals(Object obj){
    	if(obj instanceof Integer){
    		return value == ((Integer)obj).intValue();
    	}

    	return false;
    }

    public int compareTo(Object anotherInteger){
    	return Integer.compare(this.value, ((Integer)anotherInteger).intValue());
    }

    public static final int SIZE = 32;

}