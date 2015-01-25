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

import java.nio.IntBuffer;

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.hal.SPIJNI;

/**
 * SPI Interface class.
 * @author noriah Reuland <vix@noriah.dev>
 */
public class SPI extends Interface {

    public static enum Port {
        kOnboardCS0,
        kOnboardCS1,
        kOnboardCS2,
        kOnboardCS3,
        kMXP;
    };
    
//    private static int m_devices = 0;
    
    private byte m_port;
//    private int m_bitOrder;
//    private int m_clockPolarity;
//    private int m_dataOnTrailing;
    
    /**
     * @param port 
     */
    public SPI(Port port) {
        super(InterfaceType.SPI);
        if(port.equals(Port.kMXP)){
            allocateMXPPin(19);
            allocateMXPPin(21);
            allocateMXPPin(23);
            allocateMXPPin(25);
        }
        
        IntBuffer status = getLE4IntBuffer();
        
        m_port = (byte)port.ordinal();
//        m_devices++;
        
        SPIJNI.spiInitialize(m_port, status);
        HALUtil.checkStatus(status);        
        
        UsageReporting.report(UsageReporting.kResourceType_SPI, port.ordinal());
    }
    
    public void free(){
        SPIJNI.spiClose(m_port);
    }

}
