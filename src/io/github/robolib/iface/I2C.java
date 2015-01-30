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
import io.github.robolib.hal.I2CJNI;
import io.github.robolib.util.MathUtils;
import io.github.robolib.util.log.Logger;


/**
 * I2C Interface class.
 * @author noriah Reuland <vix@noriah.dev>
 */
public class I2C extends Interface {

    public static enum Port {
        ONBOARD,
        MXP;
    };
    
    public I2C(int address){
        this(Port.ONBOARD, address);
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
        if(port == Port.MXP && !m_portInitialized[1]){
            allocateMXPPin(32);
            allocateMXPPin(34);
        }
        
        m_port = (byte)port.ordinal();
        m_address = (byte)address;

        if(!m_portInitialized[port.ordinal()]){
            IntBuffer status = getLE4IntBuffer();
            I2CJNI.i2CInitialize(m_port, status);
            HALUtil.checkStatus(status);
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
     * @param dataToSend Buffer of data to send as part of the transaction.
     * @param sendSize Number of bytes to send as part of the transaction.
     * @param dataReceived Buffer to read data into.
     * @param receiveSize Number of bytes to read from the device.
     * @return Status of operation (true = success)
     */
    public synchronized boolean transaction(byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize){
        boolean success = false;
        ByteBuffer dataSendBuffer = ByteBuffer.allocateDirect(sendSize);
        dataSendBuffer.put(dataToSend);
        ByteBuffer dataReceiveBuffer = ByteBuffer.allocateDirect(receiveSize);
        
        success = I2CJNI.i2CTransaction(m_port, m_address, dataSendBuffer, (byte)sendSize, dataReceiveBuffer, (byte)receiveSize) > 0;
        
        if(receiveSize > 0 && dataReceived != null)
            dataReceiveBuffer.get(dataReceived);
        
        return success;
        
    }
    
    /**
     * Attempt to address a device on the I2C bus.
     *
     * This allows you to figure out if there is a device on the I2C bus that
     * responds to the address specified in the constructor.
     *
     * @return Status of operation (true = success)
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
     * @param reg The address of the register on the device to be written.
     * @param data The byte to write to the register on the device.
     * @return Status of operation (true = success)
     */
    public synchronized boolean write(int reg, int data){
        ByteBuffer dataToSendBuffer = ByteBuffer.allocateDirect(2);
        dataToSendBuffer.put(new byte[]{(byte) reg, (byte) data});

        return I2CJNI.i2CWrite(m_port, m_address, dataToSendBuffer, (byte)2) > 0;
    }
    
    /**
     * Execute a write transaction with the device.
     *
     * Write multiple bytes to a register on a device and wait until the
     * transaction is complete.
     *
     * @param data The data to write to the device.
     * @return Status of operation (true = success)
     */
    public synchronized boolean writeBulk(byte[] data){
        ByteBuffer b = ByteBuffer.allocateDirect(data.length);
        b.put(data);
        return I2CJNI.i2CWrite(m_port, m_address, b, (byte)data.length) > 0;
    }
    
    /**
     * write a single bit in an 8-bit device register.
     *
     * @param reg Register regAddr to write to
     * @param bit Bit position to write (0-7)
     * @param value New bit value to write
     * @return Status of operation (true = success)
     */
    public synchronized boolean writeBit(int reg, int bit, byte value){
        return writeBit(reg, bit, value != 0);
    }

    /**
     * write a single bit in an 8-bit device register.
     *
     * @param reg Register regAddr to write to
     * @param bit Bit position to write (0-7)
     * @param value New bit value to write
     * @return Status of operation (true = success)
     */
    public synchronized boolean writeBit(int reg, int bit, boolean value){
        byte[] b = new byte[1];
        readByte(reg, b);
        b[0] = (byte) (value ? (b[0] | (1 << bit)) : (b[0] & ~(1 << bit)));
        return writeByte(reg, b[0]);
    }

    /**
     * Write multiple bits in an 8-bit device register.
     *
     * @param reg Register regAddr to write to
     * @param bitStart First bit position to write (0-7)
     * @param length Number of bits to write (not more than 8)
     * @param data Right-aligned value to write
     * @return Status of operation (true = success)
     */
    public synchronized boolean writeBits(int reg, int bitStart, int length, byte data){
        byte[] b = new byte[1];
        if(readByte(reg, b)){
            byte mask = (byte) (((1 << length) - 1) << (bitStart - length + 1));
            data <<= (bitStart - length + 1);
            data &= mask;
            b[0] &= ~(mask);
            b[0] |= data;
            return writeByte(reg, b[0]);
        }
        return false;
    }

    /**
     * Write single byte to an 8-bit device register.
     *
     * @param reg Register address to write to
     * @param data New byte value to write
     * @return Status of operation (true = success)
     */
    public synchronized boolean writeByte(int reg, byte data){
        return write(reg, data);
    }

    /**
     * Write single word to a 16-bit device register.
     * 
     * @param reg Register address to write to
     * @param data New word value to write
     * @return Status of operation (true = success)
     */
    public synchronized boolean writeWord(int reg, short data) {
        return writeWords(reg, new short[]{data}, 1);
    }

    /**
     * Write multiple bytes to an 8-bit device register.
     * 
     * @param reg First register address to write to
     * @param data Buffer to copy new data from
     * @param length Number of bytes to write
     * @return Status of operation (true = success)
     */
    public synchronized boolean writeBytes(int reg, byte[] data, int length){
        ByteBuffer b = ByteBuffer.allocateDirect(data.length + 1);
        b.put((byte)reg);
        b.put(data);
        return I2CJNI.i2CWrite(m_port, m_address, b, (byte)(data.length + 1)) > 0;
    }

    /**
     * Write multiple words to a 16-bit device register.
     * 
     * @param reg First register address to write to
     * @param data Buffer to copy new data from
     * @param length Number of words to write
     * @return Status of operation (true = success)
     */
    public synchronized boolean writeWords(int reg, short[] data, int length){
        byte[] out = new byte[length * 2];
        for(int i = 0; i < length; i++){
            out[i*2] = (byte) (data[i] >> 8);
            out[i*2 + 1] = (byte)data[i];
        }
        return writeBytes(reg, out, length * 2);
    }

    /**
     * Read a single bit from an 8-bit device register.
     *
     * @param reg Register regAddr to read from
     * @param bit Bit position to read (0-7)
     * @param data Container for single bit value
     * @return Status of read operation (true = success)
     */
    public synchronized boolean readBit(int reg, int bit, byte[] data){
        boolean a = readByte(reg, data);
        data[0] = (byte) (data[0] & (1 << bit));
        return a; 
    }
    
    public synchronized boolean readBit(int reg, int bit){
        byte[] a = new byte[1];
        readBit(reg, bit, a);
        return (a[0] & 0x01) != 0;
    }

    /**
     * Read multiple bits from an 8-bit device register.
     *
     * @param reg Register regAddr to read from
     * @param bitStart First bit position to read (0-7)
     * @param length Number of bits to read (not more than 8)
     * @param data Container for right-aligned value (i.e. '101' read from any bitStart position will equal 0x05)
     * @return Status of read operation (true = success)
     */
    public synchronized boolean readBits(int reg, int bitStart, int length, byte[] data){
        byte[] b = new byte[1];
        if(readByte(reg, b)){
            byte mask = (byte) (((1 << length) - 1) << (bitStart - length + 1));
            b[0] &= mask;
            b[0] >>= (byte) (bitStart - length + 1);
            data[0] = b[0];
            return true;
        }
        return false;
    }
    
    /**
     * Read single byte from an 8-bit device register.
     *
     * @param reg Register regAddr to read from
     * @param data Container for byte value read from device
     * @return Status of read operation (true = success)
     */
    public synchronized boolean readByte(int reg, byte[] data){
        return read(reg, data,  1);
    }

    /** Read single word from a 16-bit device register.
     * 
     * @param reg Register regAddr to read from
     * @param data Container for word value read from device
     * @return Status of read operation (true = success)
     */
    public synchronized boolean readWord(int reg, short[] data) {
        return readWords(reg, data, 1);
    }

    /**
     * Read multiple bytes from an 8-bit device register.
     *
     * @param reg First register regAddr to read from
     * @param data Buffer to store read data in
     * @param length Number of bytes to read
     * @return Status of read operation (true = success)
     */
    public synchronized boolean readBytes(int reg, byte[] data, int length){
        return read(reg, data, length);
    }

    /**
     * Read multiple words from a 16-bit device register.
     * 
     * @param reg First register regAddr to read from
     * @param data Buffer to store read data in
     * @param length Number of words to read
     * @return Status of read operation (true = success)
     */
    public synchronized boolean readWords(int reg, short[] data,  int length) {

        byte[] intermediate = new byte[length * 2];
        if (read(reg, intermediate, length * 2)) {
            for (int i = 0; i < length * 2; i++) {
                data[i] = (short) ((intermediate[i] << 8) | intermediate[++i]);
            }
            return true;
        }
        return false;
    }

    /**
     * Execute a read transaction with the device.
     *
     * Read bytes from a device. Most I2C devices will auto-increment the
     * register pointer internally allowing you to read consecutive
     * registers on a device in a single transaction.
     *
     * @param reg The register to read first in the transaction.
     * @param buffer A pointer to the array of bytes to store the data read
     * from the device.
     * @param count The number of bytes to read in the transaction.
     * @return Status of operation (true = success)
     */
    public boolean read(int reg, byte[] buffer, int count){
        /*if(!MathUtils.inBounds(count, 1, 7))
            throw new IllegalArgumentException("Count must be between 1 and 7");*/
                
        return transaction(new byte[]{(byte)reg}, 1, buffer, count);
    }
        
    /**
     * Execute a read only transaction with the device.
     *
     * Read 1 to 7 bytes from a device. This method does not write any data to prompt
     * the device.
     *
     * @param buffer A pointer to the array of bytes to store the data read from
     * the device.
     * @param count The number of bytes to read in the transaction.
     * @return Status of operation (true = success)
     */
    public boolean readOnly(byte[] buffer, int count){
        if(!MathUtils.inBounds(count, 1, 7))
            throw new IllegalArgumentException("Count must be between 1 and 7");
        
        ByteBuffer b = ByteBuffer.allocateDirect(count);
        
        int value = I2CJNI.i2CRead(m_port, m_address, b, (byte)count);
        b.get(buffer);
        return value > 0;
    }
    
    /**
     * Send a broadcast write to all devices on the I2C bus.
     *
     * This is not currently implemented!
     *
     * @param reg The register to write on all devices on the bus.
     * @param data The value to write to the devices.
     */
    public void broadcast(int reg, int data){
        
    }
    
    /**
     * Verify that a device's registers contain expected values.
     *
     * Most devices will have a set of registers that contain a known value that
     * can be used to identify them. This allows an I2C device driver to easily
     * verify that the device contains the expected value.
     *
     * @param reg The base register to start reading from the device.
     * @param count The size of the field to be verified.
     * @param expected A buffer containing the values expected from the device.
     * @return true if the sensor was verified to be connected
     */
    public boolean verifySensor(int reg, int count, byte[] expected){
        byte[] devData = new byte[4];
        
        for(int i = 0, currentAddress = reg; i < count; i += 4, currentAddress += 4){
            int toRead = count - i < 4 ? count - i : 4;
            
            if(!read(currentAddress, devData, toRead))
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
