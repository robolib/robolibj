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

package io.github.robolib.module.actuator;

import io.github.robolib.jni.UsageReporting;
import io.github.robolib.module.PDP.PowerChannel;
import io.github.robolib.module.controller.PWMController;

/**
 * Cross the Road Electronics (CTRE) Talon SRX Speed Controller with PWM control
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class TalonSRX extends PWMController implements ActuatorModule {

    /**
     * Instantiates a new TalonSRX motor controller.
     *
     * @param channel the pwm channel this controller operates on
     */
    public TalonSRX(PWMChannel channel) {
        this(channel, "PWM TalonSRX Ch" + channel.ordinal(), null);
    }
    
    /**
     * Instantiates a new TalonSRX motor controller.
     * Giving a description helps with debugging. It will be used in log outputs.
     *
     * @param channel the pwm channel this controller operates on
     * @param desc the description of this motor controller
     */
    public TalonSRX(PWMChannel channel, String desc){
        this(channel, desc, null);
    }
    
    /**
     * Instantiates a new TalonSRX motor controller.
     * Giving a description helps with debugging. It will be used in log outputs.
     * Giving a power channel will help with power logging. It will be shown as this
     * controllers power channel on power log outputs.
     *
     * @param channel the pwm channel this controller operates on
     * @param desc the description of this motor controller
     * @param pwChannel The PDP(Power Distribution Panel) Channel
     */
    public TalonSRX(PWMChannel channel, String desc, PowerChannel pwChannel){
        super(channel, desc, pwChannel, 2.004, 1.52, 1.50, 1.48, 0.997, PeriodMultiplier.k1X);
        UsageReporting.report(UsageReporting.ResourceType_Talon, channel.ordinal());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableModule() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableModule() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pidWrite(double output) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeSafe() {
        // TODO Auto-generated method stub
        
    }
    
}
