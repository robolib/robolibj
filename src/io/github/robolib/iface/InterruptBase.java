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

import io.github.robolib.iface.AnalogInput.AnalogTriggerType;
import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.InterruptJNI;
import io.github.robolib.lang.ResourceAllocationException;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class InterruptBase extends Interface {
    
    protected ByteBuffer m_interrupt = null;
    
    protected boolean m_isSyncInterrupt = false;
    
    protected int m_interruptIndex;
    
    private static byte m_allocated = 0;
    
    static {
        IntBuffer status = getLE4IntBuffer();
        InterruptJNI.initializeInterruptJVM(status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Construct a new interrupt
     * 
     * @param iType the InterfaceType of this Interrupt
     */
    protected InterruptBase(InterfaceType iType) {
        super(iType);
    }
    
    /**
     * Get whether this interrupt is an analog trigger or not
     * @return true if this interrupt is an AnalogTrigger
     * @see AnalogInput#initTrigger()
     * @see AnalogInput#createTriggerOutput(AnalogTriggerType)
     * @see AnalogTriggerOutput
     * @see AnalogTriggerType
     */
    abstract boolean isAnalogTrigger();
    
    /**
     * Get the channel number for this interrupt.
     * 
     * @return channel number
     */
    abstract int getChannelNumber();
    
    /**
     * Get the module number for this interrupt.
     * 
     * @return module number
     */
    abstract byte getModuleNumber();
    
    
    /**
     * Request one of the 8 interrupts asynchronously on this digital input.
     *
     * @param handler
     *            The {@link InterruptHandlerFunction} that contains the method
     *            {@link InterruptHandlerFunction#interruptFired(int, Object)} that
     *            will be called whenever there is an interrupt on this device.
     *            Request interrupts in synchronous mode where the user program
     *            interrupt handler will be called when an interrupt occurs. The
     *            default is interrupt on rising edges only.
     */
    public void requestInterrupt(InterruptHandlerFunction<?> handler){
        if(m_interrupt != null)
            throw new ResourceAllocationException("Interrupt already allocated");
        
        allocateInterrupt(false);
        assert(m_interrupt != null);

        IntBuffer status = getLE4IntBuffer();
        InterruptJNI.attachInterruptHandler(m_interrupt, handler, handler.getParameter(), status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Request one of the 8 interrupts synchronously on this digital input. Request
     * interrupts in synchronous mode where the user program will have to
     * explicitly wait for the interrupt to occur using {@link #waitForInterrupt}. 
     * The default is interrupt on rising edges only.
     */
    public void requestInterrupt(){
        if(m_interrupt != null)
            throw new ResourceAllocationException("Interrupt already allocated");
        
        allocateInterrupt(true);

        assert(m_interrupt != null);
    }
    
    /**
     * Allocate the interrupt
     *
     * @param watcher true if the interrupt should be in synchronous mode where the user
     * program will have to explicitly wait for the interrupt to occur.
     */
    protected void allocateInterrupt(boolean watcher){
        if(m_allocated >= 8)
            throw new ResourceAllocationException("No more interrupts available");
        
        m_isSyncInterrupt = watcher;
        IntBuffer status = getLE4IntBuffer();
        m_interrupt = InterruptJNI.initializeInterrupts(m_allocated++, (byte)(watcher?1:0), status);
        HALUtil.checkStatus(status);
        
        InterruptJNI.requestInterrupts(m_interrupt, getModuleNumber(), getChannelNumber(),
                (byte)(isAnalogTrigger() ? 1 : 0), status);
        HALUtil.checkStatus(status);
        InterruptJNI.setInterruptUpSourceEdge(m_interrupt, (byte)1, (byte)0, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Cancel interrupts on this device. This deallocates all the chipobject
     * structures and disables any interrupts.
     */
    public void cancelInterrupt(){
        validate();
        IntBuffer status = getLE4IntBuffer();
        InterruptJNI.cleanInterrupts(m_interrupt, status);
        HALUtil.checkStatus(status);
        m_interrupt = null;
        m_allocated--;
    }
    /**
     * In synchronous mode, wait for the defined interrupt to occur.
     *
     * @param timeout Timeout in seconds
     */
    public void waitForInterrupt(double timeout){
        waitForInterrupt(timeout, true);
    }
    
    /**
     * In synchronous mode, wait for the defined interrupt to occur.
     *
     * @param timeout Timeout in seconds
     * @param ignorePrevious If true, ignore interrupts that happened
     * before waitForInterrupt was called.
     */
    public void waitForInterrupt(double timeout, boolean ignorePrevious){
        validate();
        IntBuffer status = getLE4IntBuffer();
        InterruptJNI.waitForInterrupt(m_interrupt, timeout, ignorePrevious, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Enable interrupts to occur on this input. Interrupts are disabled when
     * the RequestInterrupt call is made. This gives time to do the setup of the
     * other options before starting to field interrupts.
     */
    public void enableInterrupts(){
        validate();
        if(m_isSyncInterrupt) return;
        
        IntBuffer status = getLE4IntBuffer();
        InterruptJNI.enableInterrupts(m_interrupt, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Disable Interrupts without without deallocating structures.
     */
    public void disableInterrupts(){
        validate();
        if(m_isSyncInterrupt)
            throw new IllegalStateException("You can not disable synchronous interrupts");
        
        IntBuffer status = getLE4IntBuffer();
        InterruptJNI.disableInterrupts(m_interrupt, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Return the timestamp for the rising interrupt that occurred most
     * recently. This is in the same time domain as getClock().
     * The rising-edge interrupt should be enabled with
     * {@link #setUpSourceEdge}
     * @return Timestamp in seconds since boot.
     */
    public double readRisingTimestamp(){
        validate();
        IntBuffer status = getLE4IntBuffer();
        double timestamp = InterruptJNI.readRisingTimestamp(m_interrupt, status);
        HALUtil.checkStatus(status);
        return timestamp;
    }
    
    /**
     * Return the timestamp for the falling interrupt that occurred most
     * recently. This is in the same time domain as getClock().
     * The falling-edge interrupt should be enabled with
     * {@link #setUpSourceEdge}
     * @return Timestamp in seconds since boot.
     */
    public double readFallingTimestamp(){
        validate();
        IntBuffer status = getLE4IntBuffer();
        double timestamp = InterruptJNI.readFallingTimestamp(m_interrupt, status);
        HALUtil.checkStatus(status);
        return timestamp;
    }
    
    /**
     * Set which edge to trigger interrupts on
     *
     * @param risingEdge true to interrupt on rising edge
     * @param fallingEdge true to interrupt on falling edge
     */
    public void setUpSourceEdge(boolean risingEdge, boolean fallingEdge){
        validate();
        IntBuffer status = getLE4IntBuffer();
        InterruptJNI.setInterruptUpSourceEdge(m_interrupt, (byte)(risingEdge?1:0),
                (byte)(fallingEdge?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Make sure that the interrupt is not null.
     */
    protected void validate(){
        if(m_interrupt == null)
            throw new IllegalStateException("Interrupt not allocated.");
    }
    

}
