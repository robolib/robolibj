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

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.iface.PWM;
import io.github.robolib.util.MathUtils;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Servo extends PWM {
    
    private static final double SERVO_MAX_ANGLE = 180.0;
    private static final double SERVO_MIN_ANGLE = 0.0;
    
    private static final double SERVO_ANGLE_RANGE = SERVO_MAX_ANGLE - SERVO_MIN_ANGLE;

    protected static final double SERVO_PWM_DEFAULT_MAX = 2.4;
    protected static final double SERVO_PWM_DEFAULT_MIN = 0.6;
    
    /**
     * 
     * @param channel
     */
    public Servo(PWMChannel channel){
        super(channel, "PWM Servo Ch" + channel.ordinal());
        setBounds(2.4, 0, 0, 0, 0.6);
        setPeriodMultiplier(PeriodMultiplier.k4X);
        
        UsageReporting.report(UsageReporting.ResourceType_Servo, getChannelNumber());
    }
    
    /**
     * Set the servo position.
     *
     * Servo values range from 0.0 to 1.0 corresponding to the range of full left to full right.
     *
     * @param value Position from 0.0 to 1.0.
     */
    @Override
    public void set(double value){
        setPosition(value);
    }
    
    /**
     * Get the servo position.
     *
     * Servo values range from 0.0 to 1.0 corresponding to the range of full left to full right.
     *
     * @return Position from 0.0 to 1.0.
     */
    @Override
    public double get(){
        return getPosition();
    }
    
    /**
     * Set the servo angle.
     *
     * Assume that the servo angle is linear with respect to the PWM value (big assumption, need to test).
     *
     * Servo angles that are out of the supported range of the servo simply "saturate" in that direction
     * In other words, if the servo has a range of (X degrees to Y degrees) than angles of less than X
     * result in an angle of X being set and angles of more than Y degrees result in an angle of Y being set.
     *
     * @param degrees The angle in degrees to set the servo.
     */
    public void setAngle(double degrees){
        degrees = MathUtils.clamp(degrees, SERVO_MIN_ANGLE, SERVO_MAX_ANGLE);

        setPosition(((degrees - SERVO_MIN_ANGLE)) / SERVO_ANGLE_RANGE);
    }
    
    /**
     * Get the servo angle.
     *
     * Assume that the servo angle is linear with respect to the PWM value (big assumption, need to test).
     * @return The angle in degrees to which the servo is set.
     */
    public double getAngle() {
        return getPosition() * SERVO_ANGLE_RANGE + SERVO_MIN_ANGLE;
    }
}
