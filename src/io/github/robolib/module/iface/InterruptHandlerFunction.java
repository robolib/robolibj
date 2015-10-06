/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

import io.github.robolib.jni.InterruptJNI.InterruptJNIHandlerFunction;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 * 
 * @param <T> The type of the parameter that should be returned to the the
 *  method {@link #interruptFired(int, Object)}
 */
public interface InterruptHandlerFunction<T> extends InterruptJNIHandlerFunction {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public default void apply(int mask, Object param) {
        interruptFired(mask, (T)param);
    }
    
    /**
     * This method is run every time an interrupt is fired.
     * @param mask ??? What is this?
     * @param param an argument that can be overidden by overriding the {@link #getParameter()}
     * method
     */
    abstract void interruptFired(int mask, T param);
    
    /**
     * Parameter to be passed with the interrupt when called.
     * @return a variable to be passed to the function
     */
    public default T getParameter(){
        return null;
    }
    


}
