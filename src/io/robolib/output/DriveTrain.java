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

package io.robolib.output;

import io.robolib.iface.PWM.PWMChannel;


/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public class DriveTrain implements MotorSafety {
    
    public static enum MotorType {
        FRONT_LEFT,
        FRONT_RIGHT,
        REAR_LEFT,
        REAR_RIGHT;
    }
    
    protected MotorSafetyHelper m_safetyHelper;
    
    protected SpeedController m_frontLeftMotor;
    protected SpeedController m_frontRightMotor;
    protected SpeedController m_rearLeftMotor;
    protected SpeedController m_rearRightMotor;
//    private short m_invertedMotors[];
    
    
    
    public DriveTrain(PWMChannel left, PWMChannel right){
        
    }
    
    public DriveTrain(SpeedController left, SpeedController right){
        
    }
    
    public DriveTrain(PWMChannel frontLeft, PWMChannel frontRight,
            PWMChannel rearLeft, PWMChannel rearRight){
        
    }
    
    public DriveTrain(SpeedController frontLeft, SpeedController frontRight,
            SpeedController rearLeft, SpeedController rearRight){
        
    }
    
//    public void mecanum_cartesian(double x, double y, double rot, double gyro){
//        if
//    }
    
    

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
     * {@inheritDoc}
     */
    public void stopMotor() {
        if(m_frontLeftMotor != null){
            m_frontLeftMotor.set(0.0);
        }
        
        if(m_frontRightMotor != null){
            m_frontRightMotor.set(0.0);
        }
        
        m_rearLeftMotor.set(0.0);
        m_rearRightMotor.set(0.0);
        
        m_safetyHelper.feed();
    }

    /**
     * {@inheritDoc}
     */
    public void enableSafety(boolean enabled){
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
        return "Robot DriveTrain";
    }

}
