/*
 * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
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

import static io.github.robolib.util.Common.allocateInt;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.SerialPortJNI;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.util.log.Logger;

/**
 * Driver for the RS-232 serial port on the RoboRIO.
 *
 * The current implementation uses the VISA formatted I/O mode. This means that
 * all traffic goes through the formatted buffers. This allows the intermingled
 * use of print(), readString(), and the raw buffer accessors read() and
 * write().
 *
 * More information can be found in the NI-VISA User Manual here:
 * http://www.ni.com/pdf/manuals/370423a.pdf and the NI-VISA Programmer's
 * Reference Manual here: http://www.ni.com/pdf/manuals/370132c.pdf
 *
 * @author noriah <vix@noriah.dev>
 */
public final class Serial extends Interface {

    /**
     * Valid ports for the serial port.
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum Port {
        ONBOARD, MXP, USB;
    }

    /**
     * Represents the parity to use for serial communications
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum Parity {
        NONE, ODD, EVEN, MARK, SPACE;
    }

    /**
     * Represents what type of flow control to use for serial communication
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum FlowControl {
        NONE(0), XONXOFF(1), RSTCTS(2), DTRDSR(4);

        public byte value;

        FlowControl(int val) {
            value = (byte) val;
        }
    }

    /**
     * Represents the number of stop bits to use for Serial Communication
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum StopBits {
        ONE(10), ONEPOINTFIVE(15), TWO(20);

        public byte value;

        StopBits(int val) {
            value = (byte) val;
        }
    }

    /**
     * Represents which type of buffer mode to use when writing to a serial port
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum BufferMode {
        FLUSH_ON_ACCESS(1), FLUSH_WHEN_FULL(2);

        public byte value;

        BufferMode(int val) {
            value = (byte) val;
        }
    }

    private static final boolean[] m_allocated = new boolean[3];

    private byte m_port;

    /**
     * Create an instance of a Serial Port class.
     *
     * @param port     The Serial port to use
     * @param baudRate The baud rate to configure the serial port.
     * @param dataBits The number of data bits per transfer. Valid values are
     *                 between 5 and 8 bits.
     * @param parity   Select the type of parity checking to use.
     * @param stopBits The number of stop bits to use
     */
    public Serial(Port port, final int baudRate, final int dataBits, Parity parity, StopBits stopBits) {
        super(InterfaceType.SERIAL);

        if (m_allocated[port.ordinal()])
            throw new ResourceAllocationException("Cannot allocate serial port '" + port.name() + "', already in use.");

        if (port == Port.MXP) {
            allocateMXPPin(14);
            allocateMXPPin(10);
        }

        m_allocated[port.ordinal()] = true;

        m_port = (byte) port.ordinal();

        IntBuffer status = allocateInt();
        SerialPortJNI.serialInitializePort(m_port, status);
        HALUtil.checkStatus(status);
        SerialPortJNI.serialSetBaudRate(m_port, baudRate, status);
        HALUtil.checkStatus(status);
        SerialPortJNI.serialSetStopBits(m_port, (byte) dataBits, status);
        HALUtil.checkStatus(status);
        SerialPortJNI.serialSetParity(m_port, (byte) parity.ordinal(), status);
        HALUtil.checkStatus(status);
        SerialPortJNI.serialSetStopBits(m_port, stopBits.value, status);
        HALUtil.checkStatus(status);

        setReadBufferSize(1);

        setTimeout(5.0f);

        setBufferMode(BufferMode.FLUSH_ON_ACCESS);

        disableTermination();

        UsageReporting.report(UsageReporting.ResourceType_SerialPort, port.ordinal());
    }

    /**
     * Destructor.
     */
    public void free() {

    }

    /**
     * Set the type of flow control to enable on this port.
     *
     * By default, flow control is disabled.
     *
     * @param flowControl the FlowControl value to use
     */
    public void setFlowControl(FlowControl flowControl) {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialSetFlowControl(m_port, flowControl.value, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Enable termination and specify the termination character.
     *
     * Termination is currently only implemented for receive. When the the
     * terminator is received, the read() or readString() will return fewer bytes
     * than requested, stopping after the terminator.
     *
     * @param terminator The character to use for termination.
     */
    public void enableTermination(char terminator) {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialEnableTermination(m_port, terminator, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Enable termination with the default terminator '\n'
     *
     * Termination is currently only implemented for receive. When the the
     * terminator is received, the read() or readString() will return fewer bytes
     * than requested, stopping after the terminator.
     *
     * The default terminator is '\n'
     */
    public void enableTermination() {
        enableTermination('\n');
    }

    /**
     * Disable termination behavior.
     */
    public void disableTermination() {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialDisableTermination(m_port, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Get the number of bytes currently available to read from the serial port.
     *
     * @return The number of bytes available to read.
     */
    public int getBytesReceived() {
        int retVal = 0;
        IntBuffer status = allocateInt();
        retVal = SerialPortJNI.serialGetBytesRecieved(m_port, status);
        HALUtil.checkStatus(status);
        return retVal;
    }

    /**
     * Read a string out of the buffer. Reads the entire contents of the buffer
     *
     * @return The read string
     */
    public String readString() {
        return readString(getBytesReceived());
    }

    /**
     * Read a string out of the buffer. Reads the entire contents of the buffer
     *
     * @param count the number of characters to read into the string
     * @return The read string
     */
    public String readString(int count) {
        byte[] out = read(count);
        try {
            return new String(out, 0, count, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            Logger.get(Serial.class).error("Recieved data has Unsupported Encoding!", e);
            return new String();
        }
    }

    /**
     * Read raw bytes out of the buffer.
     *
     * @param count The maximum number of bytes to read.
     * @return An array of the read bytes
     */
    public byte[] read(final int count) {
        IntBuffer status = allocateInt();
        ByteBuffer data = ByteBuffer.allocateDirect(count);
        int got = SerialPortJNI.serialRead(m_port, data, count, status);
        HALUtil.checkStatus(status);
        byte[] retVal = new byte[got];
        data.get(retVal);
        return retVal;
    }

    /**
     * Write raw bytes to the serial port.
     *
     * @param buffer The buffer of bytes to write.
     * @param count  The maximum number of bytes to write.
     * @return The number of bytes actually written into the port.
     */
    public int write(byte[] buffer, int count) {
        IntBuffer status = allocateInt();
        ByteBuffer data = ByteBuffer.allocateDirect(count);
        data.put(buffer);
        int retVal = SerialPortJNI.serialWrite(m_port, data, count, status);
        HALUtil.checkStatus(status);
        return retVal;
    }

    /**
     * Write a string to the serial port
     *
     * @param data The string to write to the serial port.
     * @return The number of bytes actually written into the port.
     */
    public int writeString(String data) {
        return write(data.getBytes(), data.length());
    }

    /**
     * Configure the timeout of the serial port.
     *
     * This defines the timeout for transactions with the hardware. It will affect
     * reads if less bytes are available than the read buffer size (defaults to 1)
     * and very large writes.
     *
     * @param timeout The number of seconds to to wait for I/O.
     */
    public void setTimeout(double timeout) {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialSetTimeout(m_port, (float) timeout, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Specify the size of the input buffer.
     *
     * Specify the amount of data that can be stored before data from the device is
     * returned to Read. If you want data that is received to be returned
     * immediately, set this to 1.
     *
     * It the buffer is not filled before the read timeout expires, all data that
     * has been received so far will be returned.
     *
     * @param size The read buffer size.
     */
    public void setReadBufferSize(int size) {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialSetReadBufferSize(m_port, size, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Specify the size of the output buffer.
     *
     * Specify the amount of data that can be stored before being transmitted to the
     * device.
     *
     * @param size The write buffer size.
     */
    public void setWriteBufferSize(int size) {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialSetWriteBufferSize(m_port, size, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Specify the flushing behavior of the output buffer.
     *
     * When set to kFlushOnAccess, data is synchronously written to the serial port
     * after each call to either print() or write().
     *
     * When set to kFlushWhenFull, data will only be written to the serial port when
     * the buffer is full or when flush() is called.
     *
     * @param mode The write buffer mode.
     */
    public void setBufferMode(BufferMode mode) {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialSetWriteMode(m_port, mode.value, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Force the output buffer to be written to the port.
     *
     * This is used when setWriteBufferMode() is set to kFlushWhenFull to force a
     * flush before the buffer is full.
     */
    public void flush() {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialFlush(m_port, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Reset the serial port driver to a known state.
     *
     * Empty the transmit and receive buffers in the device and formatted I/O.
     */
    public void reset() {
        IntBuffer status = allocateInt();
        SerialPortJNI.serialClear(m_port, status);
        HALUtil.checkStatus(status);
    }

}
