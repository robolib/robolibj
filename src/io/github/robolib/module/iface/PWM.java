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

import io.github.robolib.identifier.LiveWindowSendable;
import io.github.robolib.identifier.NumberSink;
import io.github.robolib.jni.DIOJNI;
import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.PWMJNI;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.util.MathUtils;
import io.github.robolib.util.log.Logger;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * The PWM Interface class.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PWM extends Interface implements LiveWindowSendable, NumberSink {

    /**
     * The PWM Channel enum.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum PWMChannel{

        /**  PWM Channel 0 On-board. */
        Channel0,

        /**  PWM Channel 1 On-board. */
        Channel1,

        /**  PWM Channel 2 On-board. */
        Channel2,

        /**  PWM Channel 3 On-board. */
        Channel3,

        /**  PWM Channel 4 On-board. */
        Channel4,

        /**  PWM Channel 5 On-board. */
        Channel5,

        /**  PWM Channel 6 On-board. */
        Channel6,

        /**  PWM Channel 7 On-board. */
        Channel7,

        /**  PWM Channel 8 On-board. */
        Channel8,

        /**  PWM Channel 9 On-board. */
        Channel9,

        /**  PWM Channel 10, Channel 0 on MXP. */
        Channel10(11),

        /**  PWM Channel 11, Channel 1 on MXP. */
        Channel11(13),

        /**  PWM Channel 12, Channel 2 on MXP. */
        Channel12(15),

        /**  PWM Channel 13, Channel 3 on MXP. */
        Channel13(17),

        /**  PWM Channel 14, Channel 4 on MXP. */
        Channel14(27),

        /**  PWM Channel 15, Channel 5 on MXP. */
        Channel15(29),

        /**  PWM Channel 16, Channel 6 on MXP. */
        Channel16(31),

        /**  PWM Channel 17, Channel 7 on MXP. */
        Channel17(18),

        /**  PWM Channel 18, Channel 8 on MXP. */
        Channel18(22),

        /**  PWM Channel 19, Channel 9 on MXP. */
        Channel19(26);

        /** The Pin on the MXP port that this channel is on. */
        public final int m_mxpPin;

        /**
         * Instantiates a new channel.
         */
        PWMChannel(){
            m_mxpPin = 0;
        }

        /**
         * Instantiates a new channel.
         *
         * @param mxpPin the mxp pin
         */
        PWMChannel(int mxpPin){
            m_mxpPin = mxpPin;
        }
    }
    
    /**
     * The PeriodMultiplier enumeration
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum PeriodMultiplier {

        /**  Dont skip pulses. */
        k1X(0),

        /**  Skip every other pulse. */
        k2X(1),

        /**  Skip three of four pulses. */
        k4X(3);

        /** The value. */
        public final int value;

        /**
         * Instantiates a new period multiplier.
         *
         * @param var the var
         */
        private PeriodMultiplier(final int var){
            value = var;
        }
    }

    /** The default PWM Period. */
    protected static final double kDefaultPWMPeriod = 5.05;

    /** The default PWM center pulse width in ms. */
    protected static final double kDefaultPWMCenter = 1.5;

    /** The default steps down for a PWM? */
    protected static final int kDefaultPWMStepsDown = 1000;

    /** The default disable value for a PWM. */
    public static final int kPWMDisabled = 0;
    
    public static final int kSystemClockTicksPerMicrosecond = 40;

    /** Keep track of already used channels. */
    private static final boolean m_usedChannels[] = new boolean[MAX_PWM_CHANNELS];

    /** Should we be destroying the deadband? Only used by setBounds() */
    private boolean m_eliminateDeadband = false;

    /** The positive bounds scaling factor */
    private int m_scaleFactorPositive;

    /** The high end of the positive bounds. */
    private int m_boundsPositiveMax;

    /** The positive low end value used to eliminate deadband. */
    private int m_boundsPositiveMinDeadband;

    /** The low end of the positive bounds. */
    private int m_boundsPositiveMin;

    /** The center of the bounds between positive and negative. */
    private int m_boundsCenter;

    /** The high end of the negative bounds. */
    private int m_boundsNegativeMax;

    /** The negative high end value used to eliminate deadband. */
    private int m_boundsNegativeMaxDeadband;

    /** The low end of the negative bounds. */
    private int m_boundsNegativeMin;

    /** The negative bounds scaling factor. */
    private int m_scaleFactorNegative;

    /** The full range bounds scaling factor. */
    private int m_scaleFactorFull;
    
    protected final String m_description;

    /** The The RoboRIO port identifier. */
    private ByteBuffer m_port;

    /** The PWM Channel this PWM is operating on. */
    private PWMChannel m_channel;

    /**
     *
     * @param channel the channel for this pwm
     */
    public PWM(PWMChannel channel){
        this(channel, "PWM Output Ch" + channel.ordinal());
    }

    /**
     * Instantiates a new pwm.
     *
     * @param channel the channel for this pwm
     * @param desc 
     */
    public PWM(PWMChannel channel, String desc) {
        super(InterfaceType.PWM);
        m_description = desc;

        allocateChannel(channel);

        m_channel = channel;

        IntBuffer status = getLE4IntBuffer();

        m_port = DIOJNI.initializeDigitalPort(DIOJNI.getPort((byte) getChannelNumber()), status);
        HALUtil.checkStatus(status);

        if(!PWMJNI.allocatePWMChannel(m_port,  status)){
            throw new ResourceAllocationException("Cannot create '" + desc + "', PWM channel '"
                    + getChannelName() + "' already in use.");
        }
        HALUtil.checkStatus(status);

        PWMJNI.setPWM(m_port, (short) 0, status);
        HALUtil.checkStatus(status);

        m_eliminateDeadband = false;

        UsageReporting.report(UsageReporting.ResourceType_PWM, channel.ordinal());

    }
    
    public PWM(PWMChannel channel, String desc, double boundsPosMax, double boundsPosMin,
            double boundsCenter, double boundsNegMax, double boundsNegMin, PeriodMultiplier multi){
        this(channel, desc);
        setBounds(boundsPosMax, boundsPosMin, boundsCenter, boundsNegMax, boundsNegMin);
        setPeriodMultiplier(multi);
    }

    /**
     * Free the PWM channel.
     *
     * Free the resource associated with the PWM channel and set the value to 0.
     */
    public void free() {
        if(freeChannel(getChannel())){
            IntBuffer status = getLE4IntBuffer();

            PWMJNI.setPWM(m_port, (short) 0, status);
            HALUtil.checkStatus(status);

            PWMJNI.freePWMChannel(m_port, status);
            HALUtil.checkStatus(status);

            PWMJNI.freeDIO(m_port, status);
            HALUtil.checkStatus(status);

            m_channel = null;
        }
    }

    /**
     * Allocate a PWM channel.
     *
     * @param channel the PWM channel to allocate
     */
    private void allocateChannel(PWMChannel channel){
        if(channel.ordinal() > 9){
            allocateMXPPin(channel.m_mxpPin);
        }

        if(m_usedChannels[channel.ordinal()] == false){
            m_usedChannels[channel.ordinal()] = true;
        }else{
            throw new ResourceAllocationException("Cannot create '" + m_description + "', PWM channel '"
                    + getChannelName() + "' already in use.");
        }
    }

    /**
     * Free a PWM channel.
     *
     * @param channel the PWM channel to free
     */
    private boolean freeChannel(PWMChannel channel){
        if(channel.ordinal() > 9){
            freeMXPPin(channel.m_mxpPin);
        }

        if(m_usedChannels[channel.ordinal()] == true){
            m_usedChannels[channel.ordinal()] = false;
            return true;
        }else{
            Logger.get(PWM.class).error("PWM Channel '" + getChannelName()
                        + "' was not allocated. How did you get here?");
            return false;
        }
    }

    /**
     * The channel this PWM is operating on.
     *
     * @return {@link PWMChannel} representation of the PWM channel
     */
    public PWMChannel getChannel(){
        return m_channel;
    }

    /**
     * The channel this PWM is operating on, in integer form.
     *
     * @return integer representation of the PWM channel
     */
    public int getChannelNumber(){
        return m_channel.ordinal();
    }

    /**
     * The channel this PWM is operating on, in string form.
     *
     * @return string representation of the PWM channel
     */
    public String getChannelName(){
        return m_channel.name();
    }

    /**
     * Set the bounds on pulse widths for this PWM.
     *
     * @param max The maximum PWM pulse in ms
     * @param deadMax The maximum of the pulse deadband in ms
     * @param center The center/zero/off pulse width in ms
     * @param deadMin the mimium of the pulse deadband in ms
     * @param min The minimum PWM pulse in ms
     */
    public void setBounds(double max, double deadMax, double center, double deadMin, double min){
        double loopTime = DIOJNI.getLoopTiming(getLE4IntBuffer())/
                (kSystemClockTicksPerMicrosecond*1e3);
        m_boundsPositiveMax = (int) ((max - kDefaultPWMCenter)/loopTime + kDefaultPWMStepsDown - 1);
        m_boundsNegativeMin = (int) ((min - kDefaultPWMCenter)/loopTime + kDefaultPWMStepsDown - 1);
        m_boundsCenter = (int) ((center - kDefaultPWMCenter)/loopTime + kDefaultPWMStepsDown - 1);
        m_boundsPositiveMinDeadband = (int) ((deadMax - kDefaultPWMCenter)/
                loopTime + kDefaultPWMStepsDown - 1);
        m_boundsNegativeMaxDeadband = (int) ((deadMin - kDefaultPWMCenter)/
                loopTime + kDefaultPWMStepsDown - 1);

        eliminateDeadband(m_eliminateDeadband);
    }

    /**
     * Enable Deadband Elimination.
     *
     * @param eliminateDeadband Yes or No
     */
    public void eliminateDeadband(boolean eliminateDeadband){
        m_eliminateDeadband = eliminateDeadband;
        if(eliminateDeadband){
            m_boundsPositiveMin = m_boundsPositiveMinDeadband;
            m_boundsNegativeMax = m_boundsNegativeMaxDeadband;
        }else{
            m_boundsPositiveMin = m_boundsCenter + 1;
            m_boundsNegativeMax = m_boundsCenter - 1;
        }

        m_scaleFactorPositive = m_boundsPositiveMax - m_boundsPositiveMin;
        m_scaleFactorNegative = m_boundsNegativeMax - m_boundsNegativeMin;
        m_scaleFactorFull = m_boundsPositiveMax - m_boundsNegativeMin;
    }

    /**
     * Set the position of the servo.
     *
     * @param angle the servo position
     */
    public void setPosition(double angle){
        angle = MathUtils.clamp(angle, 0.0, 1.0);
        int raw;
        raw = (int) ((angle * m_scaleFactorFull) + m_boundsNegativeMin);
        setRaw(raw);
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public double getPosition(){
        int val = getRaw();
        if(val < m_boundsNegativeMin){
            return 0.0;
        }else if(val > m_boundsPositiveMax){
            return 1.0;
        }else{
            return (double)(val - m_boundsNegativeMin) / (double)m_scaleFactorFull;
        }
    }

    /**
     * Sets the speed.
     *
     * @param speed the new speed
     */
    public void setSpeed(double speed){
        speed = MathUtils.clamp(speed, -1.0, 1.0);

        int raw;
        if(speed == 0.0){
            raw = m_boundsCenter;
        }else if(speed > 0.0){
            raw = (int) (speed * m_scaleFactorPositive + m_boundsPositiveMin + 0.5);
        }else{
            raw = (int) (speed * m_scaleFactorNegative + m_boundsNegativeMax + 0.5);
        }

        setRaw(raw);
    }

    /**
     * Gets the speed.
     *
     * @return the speed
     */
    public double getSpeed(){
        int raw = getRaw();
        if(raw > m_boundsPositiveMax){
            return 1.0;
        }else if(raw < m_boundsNegativeMin){
            return -1.0;
        }else if(raw > m_boundsPositiveMin){
            return (double) (raw - m_boundsPositiveMin) / (double) m_scaleFactorPositive;
        }else if(raw < m_boundsNegativeMax){
            return (double) (raw - m_boundsNegativeMax) / (double) m_scaleFactorNegative;
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
     * Sets the speed.
     *
     * @param speed the new speed
     */
    @Override
    public void set(double value){
        setSpeed(value);
    }
    
    /**
     * Gets the speed.
     *
     * @return the speed
     */
    public double get(){
        return getSpeed();
    }
    
    /**
     * Get the center PWM value.
     * 
     * @return the center pwm value
     */
    public int getCenterPWM(){
        return m_boundsCenter;
    }

    /**
     * Set the period multiplier for older/newer devices.
     *
     * @param multi The PeriodMultiplier enum
     */
    public void setPeriodMultiplier(PeriodMultiplier multi){
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.setPWMPeriodScale(m_port, multi.value, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Sets the zero latch.
     */
    protected void setZeroLatch(){
        IntBuffer status = getLE4IntBuffer();
        PWMJNI.latchPWMZero(m_port, status);
        HALUtil.checkStatus(status);
    }

    protected ITable m_table;
    protected ITableListener m_table_listener;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSmartDashboardType() {
        return m_description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTable() {
        if(m_table != null){
            m_table.putNumber("Value",  get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startLiveWindowMode() {
        setSpeed(0);
        m_table_listener = new ITableListener(){
            @Override
            public void valueChanged(ITable itable, String key, Object value, boolean bln){
                set(((Double) value).doubleValue());
            }
        };
        m_table.addTableListener("Value", m_table_listener, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {
        setSpeed(0);
        m_table.removeTableListener(m_table_listener);
    }
}
