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
    private int m_bitOrder;
    private byte m_port;
    private int m_clockPolarity;
    private int m_dataOnTrailing;
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
    
    /**
     * 
     * @param hz The clock rate in Hertz
     */
    public final void setClockRate(int hz){
        SPIJNI.spiSetSpeed(m_port, hz);
    }
    
    public final void setMSBFirst(){
        m_bitOrder = 1;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    public final void setLSBFirst(){
        m_bitOrder = 0;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    public final void setClockActiveLow(){
        m_clockPolarity = 1;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    public final void setClockActiveHight(){
        m_clockPolarity = 0;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    public final void setSampleDataOnFalling(){
        m_dataOnTrailing = 1;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    public final void setSampleDataOnRising(){
        m_dataOnTrailing = 0;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    public final void setChipSelectActiveHigh(){
        IntBuffer status = getLE4IntBuffer();
        SPIJNI.spiSetChipSelectActiveHigh(m_port, status);
        HALUtil.checkStatus(status);
    }
    
    public final void setChipSelectActiveLow(){
        IntBuffer status = getLE4IntBuffer();
        SPIJNI.spiSetChipSelectActiveLow(m_port, status);
        HALUtil.checkStatus(status);
    }
    
    public int write(byte[] data, int size){
        int retVal = 0;
        ByteBuffer dB = ByteBuffer.allocateDirect(size);
        dB.put(data);
        retVal = SPIJNI.spiWrite(m_port, dB, (byte) size);
        return retVal;
    }
    
    public int read(Boolean initiate, byte[] data, int size){
        int retVal = 0;
        ByteBuffer dRB = ByteBuffer.allocateDirect(size);
        
        
        if(initiate){
            ByteBuffer dSB = ByteBuffer.allocateDirect(size);
            retVal = SPIJNI.spiTransaction(m_port, dSB, dRB, (byte) size);
        }else{
            retVal = SPIJNI.spiRead(m_port, dRB, (byte) size);
        }
        dRB.get(data);
        return retVal;
    }
    
    public int transaction(byte[] dataSend, byte[] dataGet, int size){
        int retVal = 0;
        ByteBuffer dSB = ByteBuffer.allocateDirect(size);
        dSB.put(dataSend);
        ByteBuffer dRB = ByteBuffer.allocateDirect(size);
        retVal = SPIJNI.spiTransaction(m_port, dSB, dRB, (byte) size);
        dRB.get(dataGet);
        return retVal;
    }
    

}
