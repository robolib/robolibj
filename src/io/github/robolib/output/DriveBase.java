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
import io.github.robolib.iface.PWM.PWMChannel;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class DriveBase implements MotorSafety {
    
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
    
    private boolean m_allocatedMotors = false;
//    private short m_invertedMotors[];
    
    
    
    /**
     * 
     * @param left
     * @param right
     */
    public DriveBase(PWMChannel left, PWMChannel right){
        this(
                new Victor(left, "DriveBase Front Left"),
                new Victor(right, "DriveBase Front Right"),
                new NullController(),
                new NullController());
        m_allocatedMotors = true;
    }
    
    /**
     * 
     * @param left
     * @param right
     */
    public DriveBase(SpeedController left, SpeedController right){
//        m_
    }
    
    /**
     * 
     * @param frontLeft
     * @param frontRight
     * @param rearLeft
     * @param rearRight
     */
    public DriveBase(PWMChannel frontLeft, PWMChannel frontRight,
            PWMChannel rearLeft, PWMChannel rearRight){
        this(
                new Victor(frontLeft, "DriveBase Front Left"),
                new Victor(frontRight, "DriveBase Front Right"),
                new Victor(rearLeft, "DriveBase Rear Left"),
                new Victor(rearLeft, "DriveBase Rear Right"));
        m_allocatedMotors = true;
        
    }
    
    /**
     * 
     * @param frontLeft
     * @param frontRight
     * @param rearLeft
     * @param rearRight
     */
    public DriveBase(SpeedController frontLeft, SpeedController frontRight,
            SpeedController rearLeft, SpeedController rearRight){
        m_frontLeftMotor = frontLeft;
        m_frontRightMotor = frontRight;
        m_rearLeftMotor = rearLeft;
        m_rearRightMotor = rearRight;
        
        m_frontLeftMotor.setSafetyEnabled(false);
        m_frontRightMotor.setSafetyEnabled(false);
        m_rearLeftMotor.setSafetyEnabled(false);
        m_rearRightMotor.setSafetyEnabled(false);
        
        m_safetyHelper = SafetyManager.addMotor(this);
        m_safetyHelper.setSafetyEnabled(true);
        
        
    }
    
    public void free(){
        if(m_allocatedMotors){
            
        }
        
    }
    
    public void setMotorInverted(MotorType type, boolean inverted){
        switch(type){
        case FRONT_LEFT:
            m_frontLeftMotor.setInverted(inverted);
        case FRONT_RIGHT:
            m_frontRightMotor.setInverted(inverted);
        case REAR_LEFT:
            m_rearLeftMotor.setInverted(inverted);
        case REAR_RIGHT:
            m_rearRightMotor.setInverted(inverted);
        }
    }
    
//    public void mecanum_cartesian(double x, double y, double rot, double gyro){
//        if
//    }
    
    public void arcadeDrive(double forward, double rotation){
        
    }
    
    public void arcadeDrive(double forward, double rotation, boolean squareInputs){
        
    }
    
    /**
     * {@inheritDoc}
     */
    public MotorSafetyHelper getSafetyHelper(){
        return m_safetyHelper;
    }

    /**
     * {@inheritDoc}
     */
    public void stopMotor() {
        m_frontLeftMotor.set(0.0);
        m_frontRightMotor.set(0.0);
        m_rearLeftMotor.set(0.0);
        m_rearRightMotor.set(0.0);
        
//        m_safetyHelper.feed();
    }


    
    public void setLeftMotors(double speed){
        m_frontLeftMotor.set(speed);
        m_rearLeftMotor.set(speed);
        m_safetyHelper.feed();
    }
    
    public void setRightMotors(double speed){
        m_frontRightMotor.set(speed);
        m_rearRightMotor.set(speed);
        m_safetyHelper.feed();
    }
    
    public void setMotors(double left, double right){
        setLeftMotors(left);
        setRightMotors(right);
    }
    
    public void setMotors(double frontLeft, double frontRight, double rearLeft, double rearRight){
        m_frontLeftMotor.set(frontLeft);
        m_frontRightMotor.set(frontRight);
        m_rearLeftMotor.set(rearLeft);
        m_rearRightMotor.set(rearRight);
        m_safetyHelper.feed();
        
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Robot DriveBase";
    }

}
