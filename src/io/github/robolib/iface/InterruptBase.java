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

package io.github.robolib.iface;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.hal.HALUtil;
import io.github.robolib.hal.InterruptJNI;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class InterruptBase extends Interface {

    static {
        IntBuffer status = getLE4IntBuffer();
        InterruptJNI.initializeInterruptJVM(status);
        HALUtil.checkStatus(status);
    }
    
    
    protected ByteBuffer m_interrupt = null;
    
    protected boolean m_isSyncInterrupt = false;
    
    protected int m_interruptIndex;
    
    /**
     * @param iType
     */
    protected InterruptBase(InterfaceType iType) {
        super(iType);
    }
    

    
    abstract boolean isAnalogTrigger();
    
    abstract int getChannelNumber();   
    

}
