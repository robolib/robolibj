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
import io.github.robolib.hal.I2CJNI;
import io.github.robolib.util.MathUtils;


/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public class I2C extends Interface {

    public static enum Port {
        kOnboard,
        kMXP;
    };
    
    public I2C(int address){
        this(Port.kOnboard, address);
    }
    
    protected byte m_port;
    protected byte m_address;
    private static boolean m_portInitialized[] = new boolean[2];
    
    
    /**
     * @param port
     * @param address
     */
    public I2C(Port port, int address) {
        super(InterfaceType.I2C);
        if(port.equals(Port.kMXP)){
            allocateMXPPin(32);
            allocateMXPPin(34);
        }
        
        m_port = (byte)port.ordinal();
        m_address = (byte)address;

        if(!m_portInitialized[port.ordinal()]){
            IntBuffer status = getLE4IntBuffer();
            I2CJNI.i2CInitialize(m_port, status);
            m_portInitialized[port.ordinal()] = true;
        }
        
        UsageReporting.report(UsageReporting.kResourceType_I2C, address);

    }
    
    /**
     * Generic transaction.
     *
     * This is a lower-level interface to the I2C hardware giving you more
     * control over each transaction.
     *
     * @param dataToSend
     *            Buffer of data to send as part of the transaction.
     * @param sendSize
     *            Number of bytes to send as part of the transaction.
     * @param dataReceived
     *            Buffer to read data into.
     * @param receiveSize
     *            Number of bytes to read from the device.
     * @return Transfer Aborted... false for success, true for aborted.
     */
    public synchronized boolean transaction(byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize){
        boolean aborted = true;
        ByteBuffer dataSendBuffer = ByteBuffer.allocateDirect(sendSize);
        dataSendBuffer.put(dataToSend);
        ByteBuffer dataReceiveBuffer = ByteBuffer.allocateDirect(receiveSize);
        
        aborted = I2CJNI.i2CTransaction(m_port, m_address, dataSendBuffer, (byte)sendSize, dataReceiveBuffer, (byte)receiveSize) != 0;
        
        if(receiveSize > 0 && dataReceiveBuffer != null)
            dataReceiveBuffer.get(dataReceived);
        
        return aborted;
        
    }
    
    /**
     * Attempt to address a device on the I2C bus.
     *
     * This allows you to figure out if there is a device on the I2C bus that
     * responds to the address specified in the constructor.
     *
     * @return Transfer Aborted... false for success, true for aborted.
     */
    public boolean checkAddress(){
        return transaction(null, 0, null, 0); 
    }
    
    /**
     * Execute a write transaction with the device.
     *
     * Write a single byte to a register on a device and wait until the
     * transaction is complete.
     *
     * @param registerAddress
     *            The address of the register on the device to be written.
     * @param data
     *            The byte to write to the register on the device.
     * @return whether the write succeeded
     */
    public synchronized boolean write(int registerAddress, int data){
        byte[] buffer = new byte[2];
        buffer[0] = (byte) registerAddress;
        buffer[1] = (byte) data;

        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(2);
        dataToSendBuffer.put(buffer);

        return I2CJNI.i2CWrite(m_port, m_address, dataToSendBuffer, (byte)2) < 0;
    }
    
    /**
     * Execute a write transaction with the device.
     *
     * Write multiple bytes to a register on a device and wait until the
     * transaction is complete.
     *
     * @param data
     *            The data to write to the device.
     * @return whether the write sccceeded
     */
    public synchronized boolean writeBulk(byte[] data){
        ByteBuffer b = ByteBuffer.allocateDirect(data.length);
        b.put(data);
        return I2CJNI.i2CWrite(m_port, m_address, b, (byte)data.length) < 0;
    }
    
    /**
     * Execute a read transaction with the device.
     *
     * Read bytes from a device. Most I2C devices will auto-increment the
     * register pointer internally allowing you to read consecutive
     * registers on a device in a single transaction.
     *
     * @param registerAddr
     *            The register to read first in the transaction.
     * @param count
     *            The number of bytes to read in the transaction.
     * @param buffer
     *            A pointer to the array of bytes to store the data read from
     *            the device.
     * @return Transfer Aborted... false for success, true for aborted.
     */
    public boolean read(int registerAddr, int count, byte[] buffer){
        /*if(!MathUtils.inBounds(count, 1, 7))
            throw new IllegalArgumentException("Count must be between 1 and 7");*/
        
        byte[] registerArray = new byte[1];
        registerArray[0] = (byte)registerAddr;
        
        return transaction(registerArray, registerArray.length, buffer, count);
    }
    
    /**
     * Execute a read only transaction with the device.
     *
     * Read 1 to 7 bytes from a device. This method does not write any data to prompt
     * the device.
     *
     * @param buffer
     *            A pointer to the array of bytes to store the data read from
     *            the device.
     * @param count
     *            The number of bytes to read in the transaction.
     * @return Transfer Aborted... false for success, true for aborted.
     */
    public boolean readOnly(byte[] buffer, int count){
        if(!MathUtils.inBounds(count, 1, 7))
            throw new IllegalArgumentException("Count must be between 1 and 7");
        
        ByteBuffer b = ByteBuffer.allocateDirect(count);
        
        int value = I2CJNI.i2CRead(m_port, m_address, b, (byte)count);
        b.get(buffer);
        return value < 0;
    }
    
    /**
     * Send a broadcast write to all devices on the I2C bus.
     *
     * This is not currently implemented!
     *
     * @param registerAddr
     *            The register to write on all devices on the bus.
     * @param data
     *            The value to write to the devices.
     */
    public void broadcast(int registerAddr, int data){
        
    }
    
    /**
     * Verify that a device's registers contain expected values.
     *
     * Most devices will have a set of registers that contain a known value that
     * can be used to identify them. This allows an I2C device driver to easily
     * verify that the device contains the expected value.
     *
     * @pre The device must support and be configured to use register
     *      auto-increment.
     *
     * @param registerAddr
     *            The base register to start reading from the device.
     * @param count
     *            The size of the field to be verified.
     * @param expected
     *            A buffer containing the values expected from the device.
     * @return true if the sensor was verified to be connected
     */
    public boolean verifySensor(int registerAddr, int count, byte[] expected){
        byte[] devData = new byte[4];
        
        for(int i = 0, currentAddress = registerAddr; i < count; i += 4, currentAddress += 4){
            int toRead = count - i < 4 ? count - i : 4;
            
            if(read(currentAddress, toRead, devData))
                return false;
            
            for(byte j = 0; j < toRead; j++){
                if(devData[j] != expected[i + j]){
                    return false;
                }
            }
        }
        return true;
    }
}
