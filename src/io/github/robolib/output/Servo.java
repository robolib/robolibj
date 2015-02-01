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

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.iface.PWM;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Servo extends PWM {
    
    private static final double kMaxServoAngle = 180.0;
    private static final double kMinServoAngle = 0.0;

    protected static final double kDefaultMaxServoPWM = 2.4;
    protected static final double kDefaultMinServoPWM = 0.6;
    
    public Servo(PWMChannel channel){
        super(channel, "PWM Servo Ch" + channel.ordinal());
        setBounds(2.4, 0, 0, 0, 0.6);
        setPeriodMultiplier(PeriodMultiplier.k4X);
        
        UsageReporting.report(UsageReporting.kResourceType_Servo, getChannelNumber());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void set(double value){
        setPosition(value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double get(){
        return getPosition();
    }
    
    
    public void setAngle(double degrees){
        if (degrees < kMinServoAngle) {
            degrees = kMinServoAngle;
        } else if (degrees > kMaxServoAngle) {
            degrees = kMaxServoAngle;
        }

        setPosition(((degrees - kMinServoAngle)) / getServoAngleRange());
    }
    
    public double getAngle() {
        return getPosition() * getServoAngleRange() + kMinServoAngle;
    }

    private double getServoAngleRange() {
        return kMaxServoAngle - kMinServoAngle;
    }
}
