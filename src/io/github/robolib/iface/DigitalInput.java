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

import java.nio.IntBuffer;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import io.github.robolib.hal.DIOJNI;
import io.github.robolib.hal.HALUtil;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class DigitalInput extends DigitalIO {
    
    public DigitalInput(DigitalChannel channel){
        super(channel, Direction.IN);
    }
    
    public boolean get(){
        IntBuffer status = getLE4IntBuffer();
        boolean value = DIOJNI.getDIO(m_port, status) != 0;
        HALUtil.checkStatus(status);
        return value;
    }
}
