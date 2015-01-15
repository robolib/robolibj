/*
 * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package org.team2583.robolib.output;

import org.team2583.robolib.iface.PWM;
import org.team2583.robolib.util.PDP;
import org.team2583.robolib.util.PDP.PowerChannel;

/**
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 *
 */
public abstract class PWMController extends PWM implements SpeedController, MotorSafety{
    
    protected MotorSafetyHelper m_safetyHelper;
    
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
        m_safetyHelper = MotorSafetyManager.addMotor(this);
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
        m_safetyHelper = MotorSafetyManager.addMotor(this);
        if(pwChannel != null){
            PDP.claimChannel(pwChannel, desc);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void pidWrite(double output) {
        set(output);
    }

    /**
     * {@inheritDoc}
     */
    public double get() {
        return getSpeed();
    }

    /**
     * {@inheritDoc}
     */
    public void set(double speed, byte syncGroup) {
        setSpeed(speed);
        m_safetyHelper.feed();
    }

    /**
     * {@inheritDoc}
     */
    public void set(double speed) {
        setSpeed(speed);
        m_safetyHelper.feed();
    }
    
    public void setSpeed(double speed){
        super.setSpeed(speed);
        m_safetyHelper.feed();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAlive() {
        return m_safetyHelper.isAlive();
    }

    /**
     * {@inheritDoc}
     */
    public void setSafetyExpiration(double timeout) {
        m_safetyHelper.setExpiration(timeout);
    }

    /**
     * {@inheritDoc}
     */
    public double getSafetyExpiration() {
        return m_safetyHelper.getExpiration();
    }
    
    /**
     * Feed the MotorSafetyHelper
     */
    public void feed(){
        m_safetyHelper.feed();
    }

    /**
     * {@inheritDoc}
     */
    public void stopMotor() {
        setRaw(kPWMDisabled);
    }

    /**
     * {@inheritDoc}
     */
    public void enableSafety(boolean enabled) {
        m_safetyHelper.enableSafety(enabled);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSafetyEnabled() {
        return m_safetyHelper.isSafetyEnabled();
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return m_description;
    }
}
