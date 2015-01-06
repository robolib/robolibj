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

package org.team2583.robolib.iface;

import static org.team2583.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.team2583.robolib.exception.ResourceAllocationException;
import org.team2583.robolib.util.MathUtils;

import edu.wpi.first.wpilibj.hal.DIOJNI;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.PWMJNI;

/**
 * The Class PWM.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PWM extends Interface {

    /**
     * The Enum Channel.
     */
    public static enum Channel{
        
        /** PWM Channel 0 On-board */
        Channel0,
        /** PWM Channel 1 On-board */
        Channel1,
        /** PWM Channel 2 On-board */
        Channel2,
        /** PWM Channel 3 On-board */
        Channel3,
        /** PWM Channel 4 On-board */
        Channel4,
        /** PWM Channel 5 On-board */
        Channel5,
        /** PWM Channel 6 On-board */
        Channel6,
        /** PWM Channel 7 On-board */
        Channel7,
        /** PWM Channel 8 On-board */
        Channel8,
        /** PWM Channel 9 On-board */
        Channel9,
        /** PWM Channel 10, Channel 0 on MXP */
        Channel10(11),
        /** PWM Channel 11, Channel 1 on MXP */
        Channel11(13),
        /** PWM Channel 12, Channel 2 on MXP */
        Channel12(15),
        /** PWM Channel 13, Channel 3 on MXP */
        Channel13(17),
        /** PWM Channel 14, Channel 4 on MXP */
        Channel14(27),
        /** PWM Channel 15, Channel 5 on MXP */
        Channel15(29),
        /** PWM Channel 16, Channel 6 on MXP */
        Channel16(31),
        /** PWM Channel 17, Channel 7 on MXP */
        Channel17(18),
        /** PWM Channel 18, Channel 8 on MXP */
        Channel18(22),
        /** PWM Channel 19, Channel 9 on MXP */
        Channel19(26);

        public final int m_mxpPin;
        private Channel(){ m_mxpPin = 0; }
        private Channel(int mxpPin){ m_mxpPin = mxpPin; }
    }
    
    public static enum PeriodMultiplier {
        
        /** Dont skip pulses */
        k1X(0),
        /** Skip every other pulse */
        k2X(1),
        /** Skip three of four pulses */
        k4X(3);
        
        public final int value;
        private PeriodMultiplier(final int var){ value = var; }
    }
    
    protected static final double kDefaultPWMPeriod = 5.05;
    
    protected static final double kDefaultPWMCenter = 1.5;
    
    protected static final int kDefaultPWMStepsDown = 1000;
    
    public static final int kPWMDisabled = 0;
    
    private boolean m_destroyDeadband;
    private double m_maxW;
    private int m_maxB;
    private double m_deadMaxW;
    private int m_deadMaxB;
    private double m_centerW;
    private int m_centerB;
    private double m_deadMinW;
    private int m_deadMinB;
    private double m_minW;
    private int m_minB;
    
    /** Keep track of already used channels. */
    private static boolean m_usedChannels[] = new boolean[kMaxPWMChannels];
    
    private ByteBuffer m_port;
    private Channel m_channel;
    
    /**
     * Instantiates a new pwm.
     *
     * @param channel the channel for this pwm
     */
    public PWM(Channel channel) {
        super(InterfaceType.PWM, channel.ordinal());
        
        if(channel.ordinal() > 9){
            checkMXPPin(InterfaceType.PWM, channel.m_mxpPin);
        }
        
        if(m_usedChannels[channel.ordinal()] == true){
            throw new ResourceAllocationException("PWM channel '" + channel.name() + "' already in use.");
        }else{
            m_usedChannels[channel.ordinal()] = true;
        }
        
        m_channel = channel;
        
        IntBuffer status = getLE4IntBuffer();
        
        m_port = DIOJNI.initializeDigitalPort(DIOJNI.getPort((byte) m_channel.ordinal()), status);
        HALUtil.checkStatus(status);
        
        if(!PWMJNI.allocatePWMChannel(m_port,  status)){
            throw new ResourceAllocationException("PWM channel '" + m_channel.name() + "' already in use.");
        }
        HALUtil.checkStatus(status);
        
        PWMJNI.setPWM(m_port, (short) 0, status);
        HALUtil.checkStatus(status);
        
        m_destroyDeadband = false;

    }
    
    /**
     * Set the bounds on pulse widths for this PWM
     * @param max The maximum PWM pulse in ms
     * @param deadMax The maximum of the pulse deadband in ms
     * @param center The center/zero/off pulse width in ms
     * @param deadMin the mimium of the pulse deadband in ms
     * @param min The minimum PWM pulse in ms
     */
    public void setBounds(double max, double deadMax, double center, double deadMin, double min){
        double loopTime = (DIOJNI.getLoopTiming(getLE4IntBuffer())/40000);
        m_maxW = max;
        m_maxB = ((int) ((max - kDefaultPWMCenter)/loopTime)) + kDefaultPWMStepsDown - 1;
        m_deadMaxW = deadMax;
        m_deadMaxB = ((int) ((deadMax - kDefaultPWMCenter)/loopTime)) + kDefaultPWMStepsDown - 1;
        m_centerW = center;
        m_centerB = ((int) ((center - kDefaultPWMCenter)/loopTime)) + kDefaultPWMStepsDown - 1;
        m_deadMinW = deadMin;
        m_deadMinB = ((int) ((deadMin - kDefaultPWMCenter)/loopTime)) + kDefaultPWMStepsDown - 1;
        m_minW = min;
        m_minB = ((int) ((min - kDefaultPWMCenter)/loopTime)) + kDefaultPWMStepsDown - 1;
    }
    
    /**
     * Enable Deadband Elimination
     * @param eliminateDeadband Yes or No
     */
    public void enableDeadbandDestruction(boolean eliminateDeadband){
        m_destroyDeadband = eliminateDeadband;
    }
    
    /**
     * The channel this PWM is operating on
     * @return {@link Channel} representation of the PWM channel
     */
    public Channel getChannel(){
        return m_channel;
    }
    
    /**
     * The channel this PWM is operating on, in integer form
     * @return integer representation of the PWM channel
     */
    public int getChannelNum(){
        return m_channel.ordinal();
    }
    
    /**
     * Set the position of the servo
     * @param angle the servo position
     */
    public void setPosition(double angle){
        angle = MathUtils.clamp(angle, 0.0, 1.0);
        int raw;
        raw = (int) ((angle * (double)getFullPWMScaleFactor()) + getMinNegPWM());
        setRaw(raw);
    }
    
    /**
     * 
     * @return
     */
    public double getPosition(){
        int val = getRaw();
        if(val < getMinNegPWM()){
            return 0.0;
        }else if(val > getMaxPosPWM()){
            return 1.0;
        }else{
            return (double)(val - getMinNegPWM()) / (double)getFullPWMScaleFactor();
        }
    }
    
    /**
     * 
     * @param speed
     */
    public final void setSpeed(double speed){
        speed = MathUtils.clamp(speed, -1.0, 1.0);
        
        int raw;
        if(speed == 0.0){
            raw = getCenterPWM();
        }else if(speed > 0.0){
            raw = (int) (speed * ((double)(getPosPWMScaleFactor() + getMinPosPWM())) + 0.5);
        }else{
            raw = (int) (speed * ((double)(getNegPWMScaleFactor() + getMaxNegPWM())) + 0.5);
        }
        
        setRaw(raw);
    }
    
    /**
     * 
     * @return
     */
    public final double getSpeed(){
        int raw = getRaw();
        if(raw >= getMaxPosPWM()){
            return 1.0;
        }else if(raw <= getMinNegPWM()){
            return -1.0;
        }else if(raw >= getMinPosPWM()){
            return (double) (raw - getMinPosPWM()) / (double) getPosPWMScaleFactor();
        }else if(raw <= getMaxNegPWM()){
            return (double) (raw - getMaxNegPWM()) / (double) getNegPWMScaleFactor();
        }else{
            return 0.0;
        }
    }
    
    /**
     * Set the PWM value directly to the hardware.
     *
     * Write a raw value to a PWM channel.
     *
     * @param value Raw PWM value.  Range 0 - 255.
     */
    public void setRaw(int value) {
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWM(m_port, (short) value, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Get the PWM value directly from the hardware.
     *
     * Read a raw value from a PWM channel.
     *
     * @return Raw PWM control value.  Range: 0 - 255.
     */
    public int getRaw() {
        IntBuffer status = getLE4IntBuffer();
        int value = PWMJNI.getPWM(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Set the period multiplier for older/newer devices
     * @param multi The PeriodMultiplier enum
     */
    public void setPeriodMultiplier(PeriodMultiplier multi){
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWMPeriodScale(m_port, multi.value, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * 
     */
    protected void setZeroLatch(){
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.latchPWMZero(m_port, status);
        HALUtil.checkStatus(status);
    }
    

    /**
     * Get the maximum positive pulse width in ms
     * @return The maximum positive pulse width in ms
     */
    public double getMaxPosWidth(){
        return m_maxW;
    }

    /**
     * Get the maximum positive pulse width inside the range of 0 - 255
     * @return The maximum positive pulse width
     */
    public int getMaxPosPWM(){
        return m_maxB;
    }
    
    /**
     * Get the minimum positive pulse width in ms
     * @return The minimum positive pulse width in ms
     */
    public double getMinPosWidth(){
        return m_destroyDeadband ? m_deadMaxW : m_centerW + 0.01;
    }

    /**
     * 
     * @return
     */
    public int getMinPosPWM(){
        return m_destroyDeadband ? m_deadMaxB : m_centerB + 1;
    }
    
    /**
     * Get the center/stop pulse width in ms
     * @return The center pulse width in ms
     */
    public double getCenterWidth(){
        return m_centerW;
    }

    /**
     * 
     * @return
     */
    public int getCenterPWM(){
        return m_centerB;
    }
    
    /**
     * Get the maximum negative pulse width in ms
     * @return The maximum negative pulse width in ms
     */
    public double getMaxNegWidth(){
        return m_destroyDeadband ? m_deadMinW : m_centerW - 0.01;
    }
    
    /**
     * 
     * @return
     */
    public int getMaxNegPWM(){
        return m_destroyDeadband ? m_deadMinB : m_centerB - 1;
    }
    
    /**
     * Get the minimum negative pulse width in ms
     * @return The minimum negative pulse width in ms
     */
    public double getMinNegWidth(){
        return m_minW;
    }

    /**
     * 
     * @return
     */
    public int getMinNegPWM(){
        return m_minB;
    }
    
    /**
     * 
     * @return
     */
    public int getPosPWMScaleFactor(){
        return getMaxPosPWM() - getMinPosPWM();
    }
    
    /**
     * 
     * @return
     */
    public int getNegPWMScaleFactor(){
        return getMaxNegPWM() - getMinNegPWM();
    }

    /**
     * 
     * @return
     */
    public int getFullPWMScaleFactor(){
        return getMaxPosPWM() - getMinNegPWM();
    }
}
