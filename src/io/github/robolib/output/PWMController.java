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

package io.github.robolib.output;

import io.github.robolib.framework.SafetyManager;
import io.github.robolib.iface.PWM;
import io.github.robolib.util.PDP;
import io.github.robolib.util.PDP.PowerChannel;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PWMController extends PWM implements SpeedController, MotorSafety {
    
    protected MotorSafetyHelper m_safetyHelper;
    
    private byte m_inverted = 1;
    
    /**
     * Instantiates a new PWM motor controller.
     *
     * @param channel the pwm channel this controller operates on
     */
    public PWMController(PWMChannel channel){
        this(channel, "PWM Motor Ch" + channel.ordinal());
    }
    
    /**
     * Instantiates a new PWM motor controller.
     * Giving a description helps with debugging. It will be used in log outputs.
     *
     * @param channel the pwm channel this controller operates on
     * @param desc the description of this motor controller
     */
    public PWMController(PWMChannel channel, String desc){
        this(channel, desc, null);
    }
    
    /**
     * Instantiates a new PWM motor controller.
     * Giving a description helps with debugging. It will be used in log outputs.
     * Giving a power channel will help with power logging. It will be shown as this
     * controllers power channel on power log outputs.
     *
     * @param channel the pwm channel this controller operates on
     * @param desc the description of this motor controller
     * @param pwChannel The PDP(Power Distribution Panel) Channel
     */
    public PWMController(PWMChannel channel, String desc, PowerChannel pwChannel){
        super(channel, desc);
        setPeriodMultiplier(PeriodMultiplier.k1X);
        setRaw(getCenterPWM());
        setZeroLatch();
        m_safetyHelper = SafetyManager.addMotor(this);
        if(pwChannel != null){
            PDP.claimChannel(pwChannel, desc);
        }
    }
    
    /**
     * Instantiates a new PWM motor controller.
     * Giving a description helps with debugging. It will be used in log outputs.
     * Giving a power channel will help with power logging. It will be shown as this
     * controllers power channel on power log outputs.
     *
     * @param channel the pwm channel this controller operates on
     * @param desc the description of this motor controller
     * @param pwChannel The PDP(Power Distribution Panel) Channel
     * @param boundsPosMax The maximum PWM pulse in ms
     * @param boundsPosMin The maximum of the pulse deadband in ms
     * @param boundsCenter The center/zero/off pulse width in ms
     * @param boundsNegMax the mimium of the pulse deadband in ms
     * @param boundsNegMin The minimum PWM pulse in ms
     * @param multi The PeriodMultiplier enum
     */
    public PWMController(PWMChannel channel, String desc, PowerChannel pwChannel,
            double boundsPosMax, double boundsPosMin, double boundsCenter,
            double boundsNegMax, double boundsNegMin, PeriodMultiplier multi){
        super(channel, desc, boundsPosMax, boundsPosMin, boundsCenter, boundsNegMax, boundsNegMin, multi);
        setRaw(getCenterPWM());
        setZeroLatch();
        m_safetyHelper = SafetyManager.addMotor(this);
        if(pwChannel != null){
            PDP.claimChannel(pwChannel, desc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MotorSafetyHelper getSafetyHelper() {
        return m_safetyHelper;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double get() {
        return getSpeed();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double getSpeed(){
    	return super.getSpeed() * m_inverted;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpeed(double speed){
        super.setSpeed(speed * m_inverted);
        m_safetyHelper.feed();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setInverted(boolean inverted){
        m_inverted = (byte)(inverted ? -1 : 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopMotor() {
        setRaw(kPWMDisabled);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return m_description;
    }
}
