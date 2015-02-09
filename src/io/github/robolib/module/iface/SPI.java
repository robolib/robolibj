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

import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.SPIJNI;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.lang.ResourceAllocationException;

/**
 * SPI bus interface class.
 * @author noriah Reuland <vix@noriah.dev>
 */
public class SPI extends Interface {

    /**
     * Valid SPI ports on the RIO
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum Port {
        ONBOARD_CS0,
        ONBOARD_CS1,
        ONBOARD_CS2,
        ONBOARD_CS3,
        MXP;
    };
    
    private int m_bitOrder;
    private final byte m_port;
    private int m_clockPolarity;
    private int m_dataOnTrailing;
    
    private static final boolean[] ALLOCATED_PORTS = new boolean[5];
    
    /**
     * Constructor
     *
     * @param port the physical SPI port
     */
    public SPI(Port port) {
        super(InterfaceType.SPI);
        
        if(ALLOCATED_PORTS[port.ordinal()])
            throw new ResourceAllocationException("Cannot allocate spi port '" + port.name() + "', already in use.");

        if(port == Port.MXP){
            allocateMXPPin(19);
            allocateMXPPin(21);
            allocateMXPPin(23);
            allocateMXPPin(25);
        }
        
        ALLOCATED_PORTS[port.ordinal()] = true;
        
        IntBuffer status = getLE4IntBuffer();
        
        m_port = (byte)port.ordinal();
//        m_devices++;
        
        SPIJNI.spiInitialize(m_port, status);
        HALUtil.checkStatus(status);        
        
        UsageReporting.report(UsageReporting.ResourceType_SPI, port.ordinal());
    }
    
    /**
     * Free the resources used by this object
     */
    public final void free(){
        SPIJNI.spiClose(m_port);
    }
    
    /**
     * Configure the rate of the generated clock signal.
     * The default value is 500,000 Hz.
     * The maximum value is 4,000,000 Hz.
     *
     * @param hz The clock rate in Hertz.
     */
    public final void setClockRate(int hz){
        SPIJNI.spiSetSpeed(m_port, hz);
    }
    
    /**
     * Configure the order that bits are sent and received on the wire
     * to be most significant bit first.
     */
    public final void setMSBFirst(){
        m_bitOrder = 1;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    /**
     * Configure the order that bits are sent and received on the wire
     * to be least significant bit first.
     */
    public final void setLSBFirst(){
        m_bitOrder = 0;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    /**
     * Configure the clock output line to be active low.
     * This is sometimes called clock polarity high or clock idle high.
     */
    public final void setClockActiveLow(){
        m_clockPolarity = 1;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    /**
     * Configure the clock output line to be active high.
     * This is sometimes called clock polarity low or clock idle low.
     */
    public final void setClockActiveHight(){
        m_clockPolarity = 0;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    /**
     * Configure that the data is stable on the falling edge and the data
     * changes on the rising edge.
     */
    public final void setSampleDataOnFalling(){
        m_dataOnTrailing = 1;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    /**
     * Configure that the data is stable on the rising edge and the data
     * changes on the falling edge.
     */
    public final void setSampleDataOnRising(){
        m_dataOnTrailing = 0;
        SPIJNI.spiSetOpts(m_port, m_bitOrder, m_dataOnTrailing, m_clockPolarity);
    }
    
    /**
     * Configure the chip select line to be active high.
     */
    public final void setChipSelectActiveHigh(){
        IntBuffer status = getLE4IntBuffer();
        SPIJNI.spiSetChipSelectActiveHigh(m_port, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Configure the chip select line to be active low.
     */
    public final void setChipSelectActiveLow(){
        IntBuffer status = getLE4IntBuffer();
        SPIJNI.spiSetChipSelectActiveLow(m_port, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Write data to the slave device.  Blocks until there is space in the
     * output FIFO.
     *
     * If not running in output only mode, also saves the data received
     * on the MISO input during the transfer into the receive FIFO.
     * 
     * @param data the data to write
     * @param size the number of bytes to send
     */
    public final int write(byte[] data, int size){
        int retVal = 0;
        ByteBuffer dB = ByteBuffer.allocateDirect(size);
        dB.put(data);
        retVal = SPIJNI.spiWrite(m_port, dB, (byte) size);
        return retVal;
    }
    
    /**
     * Read a word from the receive FIFO.
     *
     * Waits for the current transfer to complete if the receive FIFO is empty.
     *
     * If the receive FIFO is empty, there is no active transfer, and initiate
     * is false, errors.
     *
     * @param initiate If true, this function pushes "0" into the
     *                 transmit buffer and initiates a transfer.
     *                 If false, this function assumes that data is
     *                 already in the receive FIFO from a previous write.
     * @param data the buffer to read into
     * @param size the number of bytes to read
     */
    public final int read(Boolean initiate, byte[] data, int size){
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
    
    /**
     * Perform a simultaneous read/write transaction with the device
     *
     * @param dataSend The data to be written out to the device
     * @param dataGet Buffer to receive data from the device
     * @param size The length of the transaction, in bytes
     */
    public final int transaction(byte[] dataSend, byte[] dataGet, int size){
        int retVal = 0;
        ByteBuffer dSB = ByteBuffer.allocateDirect(size);
        dSB.put(dataSend);
        ByteBuffer dRB = ByteBuffer.allocateDirect(size);
        retVal = SPIJNI.spiTransaction(m_port, dSB, dRB, (byte) size);
        dRB.get(dataGet);
        return retVal;
    }
    

}
