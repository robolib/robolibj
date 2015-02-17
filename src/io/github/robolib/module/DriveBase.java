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

package io.github.robolib.module;

import java.util.Arrays;
import java.util.function.BiConsumer;

import io.github.robolib.SafetyManager;
import io.github.robolib.module.controller.NullController;
import io.github.robolib.module.controller.SpeedController;
import io.github.robolib.module.controller.Victor;
import io.github.robolib.module.iface.PWM.PWMChannel;
import io.github.robolib.util.MathUtils;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class DriveBase implements MotorSafety {
    
    /**
     * Enum representation of each motor
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
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
    
    protected boolean m_allocatedFrontMotors = false;
    protected boolean m_allocatedRearMotors = false;

    protected byte m_syncGroup = 0;
    protected double[] m_wheelSpeeds;
    
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
        m_allocatedFrontMotors = true;
        m_allocatedRearMotors = true;
    }
    
    /**
     * 
     * @param left
     * @param right
     */
    public DriveBase(SpeedController left, SpeedController right){
        this(left, right, new NullController(), new NullController());
        m_allocatedRearMotors = true;
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
        m_allocatedFrontMotors = true;
        m_allocatedRearMotors = true;
        
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
        
        m_frontLeftMotor.setInverted(true);
        m_rearLeftMotor.setInverted(true);
        
        m_wheelSpeeds = new double[4];

        m_safetyHelper = SafetyManager.addMotor(this);
        m_safetyHelper.setSafetyEnabled(true);
        
    }
    
    public void free(){
        if(m_allocatedFrontMotors){
            
        }
        
        if(m_allocatedRearMotors){
            
        }
        
    }
    
    /**
     * Invert a motor direction.
     * This is used when a motor should run in the opposite direction as the drive
     * code would normally run it. Motors that are direct drive would be inverted, the
     * drive code assumes that the motors are geared with one reversal.
     * @param type the {@link MotorType} to set
     * @param inverted True if the motor should be inverted when operated.
     */
    public void setMotorInverted(MotorType type, boolean inverted){
        switch(type){
        case FRONT_LEFT:
            m_frontLeftMotor.setInverted(inverted);
        case FRONT_RIGHT:
            m_frontRightMotor.setInverted(false & inverted);
        case REAR_LEFT:
            m_rearLeftMotor.setInverted(inverted);
        case REAR_RIGHT:
            m_rearRightMotor.setInverted(false & inverted);
        }
    }
    
    public <T extends Object> void bind(T o, BiConsumer<DriveBase, T> q){
        
    }
    
    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * @param forward The value to use for forwards/backwards
     * @param rotation The value to use for the rotate right/left
     */
    public void arcade(double forward, double rotation){
        forward = MathUtils.clamp(forward, -1.0, 1.0);
        rotation = MathUtils.clamp(rotation, -1.0, 1.0);
        
//        setMotors(forward - rotation);
        setMotors(forward + rotation, forward - rotation);
        
        /*double left;
        double right;
        
        if(forward > 0){
            if(rotation > 0){
                left = forward - rotation;
                right = Math.max(forward, rotation);
            }else{
                left = Math.max(forward, rotation);
                right = forward - rotation;
            }
        }else{
            if(rotation > 0){
                left = -Math.max(forward, rotation);
                right = forward + rotation;
            }else{
                left = forward - rotation;
                right = -Math.max(forward, rotation);
            }
        }
        
        setMotors(left, right);*/
    }
    
    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * @param forward The value to use for forwards/backwards
     * @param rotation The value to use for the rotate right/left
     * @param squared If set, decreases the sensitivity at low speeds
     */
    public void arcade(double forward, double rotation, boolean squared){
        if(squared){
            forward = squareInput(forward);
            rotation = squareInput(rotation);
        }
        
        arcade(forward, rotation);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you directly provide joystick values from any source.
     * @param left The value of the left stick.
     * @param right The value of the right stick.
     */
    public void tank(double left, double right){
        setMotors(left, right);
    }
    
    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you directly provide joystick values from any source.
     * @param left The value of the left stick.
     * @param right The value of the right stick.
     * @param squared If set, decreases the sensitivity at low speeds
     */
    public void tank(double left, double right, boolean squared){
        if(squared){
            left = squareInput(left);
            right = squareInput(right);
        }
        setMotors(left, right);
    }
    
    /**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * This is designed to be directly driven by joystick axes.
     *
     * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y The speed that the robot should drive in the Y direction.
     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     */
    public void mecanum(double x, double y, double rotation){
        
        m_wheelSpeeds[0] = x + y + rotation;
        m_wheelSpeeds[1] = -x + y - rotation;
        m_wheelSpeeds[2] = -x + y + rotation;
        m_wheelSpeeds[3] = x + y - rotation;
        
        normalize();
        
        setMotors();
    }
    
    /**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * This is designed to be directly driven by joystick axes.
     *
     * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y The speed that the robot should drive in the Y direction.
     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     * @param squared If set, decreases the sensitivity at low speeds
     */
    public void mecanum(double x, double y, double rotation, boolean squared){
        if(squared){
            x = squareInput(x);
            y = squareInput(y);
            rotation = squareInput(rotation);
        }
        
        mecanum(x, y, rotation);
    }
    
    /**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * This is designed to be directly driven by joystick axes.
     *
     * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y The speed that the robot should drive in the Y direction.
     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     * @param gyro The current angle reading from the gyro.  Use this to implement field-oriented controls.
     */
    public void mecanum(double x, double y, double rotation, double gyro){
        double[] rotated = rotateVector(x, y, gyro);
        mecanum(rotated[0], rotated[1], rotation);
    }
    
    /**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * This is designed to be directly driven by joystick axes.
     *
     * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y The speed that the robot should drive in the Y direction.
     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the translation. [-1.0..1.0]
     * @param gyro The current angle reading from the gyro.  Use this to implement field-oriented controls.
     * @param squared If set, decreases the sensitivity at low speeds
     */
    public void mecanum(double x, double y, double rotation, double gyro, boolean squared){
        if(squared){
            x = squareInput(x);
            y = squareInput(y);
            rotation = squareInput(rotation);
        }
        double[] rotated = rotateVector(x, y, gyro);
        mecanum(rotated[0], rotated[1], rotation);
    }
    
    private static final double sqrt2 = Math.sqrt(2.0);
    
    /**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * @param magnitude The speed that the robot should drive in a given direction.
     * @param direction The direction the robot should drive in degrees. The direction and maginitute are
     * independent of the rotation rate.
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the magnitute or direction. [-1.0..1.0]
     */
    public void mecanum_polar(double magnitude, double direction, double rotation){
        
        magnitude = MathUtils.clamp(magnitude, -1.0, 1.0) * sqrt2;
        direction = (direction + 45.0) * MathUtils.PI_OVER_180;
        double cosD = Math.cos(direction) * magnitude;
        double sinD = Math.sin(direction) * magnitude;
        
        m_wheelSpeeds[0] = sinD + rotation;
        m_wheelSpeeds[1] = cosD - rotation;
        m_wheelSpeeds[2] = cosD + rotation;
        m_wheelSpeeds[3] = sinD - rotation;
        
        normalize();
        
        setMotors();        
    }
    
    /**
     * Drive method for Mecanum wheeled robots.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * @param magnitude The speed that the robot should drive in a given direction.
     * @param direction The direction the robot should drive in degrees. The direction and maginitute are
     * independent of the rotation rate.
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the magnitute or direction. [-1.0..1.0]
     * @param squared If set, decreases the sensitivity at low speeds
     */
    public void mecanum_polar(double magnitude, double direction, double rotation, boolean squared){
        if(squared){
            magnitude = squareInput(magnitude);
            direction = squareInput(direction);
            rotation = squareInput(rotation);
        }
        
        mecanum_polar(magnitude, direction, rotation);
    }

    /**
     * Square an input, keeping the sign(-/+)
     * @param input the value to be squared
     * @return A squared value of the input with the sign kept
     */
    protected static double squareInput(double input){
        if(input < 0) return -(input*input);
        return input * input;
    }
    
    /**
     * Normalize all wheel speeds if the magnitude of any wheel is greater than 1.0.
     */
    protected void normalize(){
        double tmp = Arrays.stream(m_wheelSpeeds)
                .reduce(1.0, (double x, double val) -> (Math.abs(val) > x) ? val : x);

        if(tmp > 1.0)
            m_wheelSpeeds = Arrays.stream(m_wheelSpeeds).map(val -> val/tmp).toArray();
    }

    /**
     * Rotate a vector in Cartesian space.
     * 
     * @param x the x component
     * @param y the y component
     * @param angle the angle of rotation
     * @return an array of doubles [x, y]
     */
    protected static double[] rotateVector(double x, double y, double angle){
        double tmp = angle * MathUtils.PI_OVER_180;
        double cosA = Math.cos(tmp);
        double sinA = Math.sin(tmp);
        return new double[]{
                x * cosA - y * sinA,
                x * sinA + y * cosA
        };
    }

    /**
     * Set the speed of the left motors.
     * 
     * The motors on the left side of the robot are set to "speed".
     * @param speed The speed to send to the left side of the robot.
     */
    public void setLeftMotors(double speed){
        m_frontLeftMotor.setSpeed(speed);
        m_rearLeftMotor.setSpeed(speed);
        m_safetyHelper.feed();
    }
    
    /**
     * Set the speed of the right motors.
     * 
     * The motors on the right side of the robot are set to "speed".
     * @param speed The speed to send to the right side of the robot.
     */
    public void setRightMotors(double speed){
        m_frontRightMotor.setSpeed(speed);
        m_rearRightMotor.setSpeed(speed);
        m_safetyHelper.feed();
    }
    
    /**
     * Set the speed of all the motors to a single value.
     * 
     * The motors are set to "speed".
     * @param speed The speed to send to all the motors.
     */
    public void setMotors(double speed){
        setMotors(speed, speed, speed, speed);
    }
    
    /**
     * Set the speed of the right and left motors.
     * 
     * The motors are set to "left" and "right".
     * @param left The speed to send to the left side of the robot.
     * @param right The speed to send to the right side of the robot.
     */
    public void setMotors(double left, double right){
        setMotors(left, right, left, right);
    }
    
    /**
     * Set the speed of all the motors.
     * 
     * @param frontLeft The speed to send to the front left motor.
     * @param frontRight The speed to send to the front right motor.
     * @param rearLeft The speed to send to the rear left motor.
     * @param rearRight The speed to send to the rear right motor.
     */
    public void setMotors(double frontLeft, double frontRight, double rearLeft, double rearRight){
        m_frontLeftMotor.setSpeed(frontLeft);
        m_frontRightMotor.setSpeed(frontRight);
        m_rearLeftMotor.setSpeed(rearLeft);
        m_rearRightMotor.setSpeed(rearRight);
        m_safetyHelper.feed();
    }
    
    /**
     * Set the speed of the motors to the values in m_wheelSpeeds.
     * 
     * Set all the motors to their respective values in the
     * m_wheelSpeeds array.
     */
    protected void setMotors(){
        m_frontLeftMotor.setSpeed(m_wheelSpeeds[0]);
        m_frontRightMotor.setSpeed(m_wheelSpeeds[1]);
        m_rearLeftMotor.setSpeed(m_wheelSpeeds[2]);
        m_rearRightMotor.setSpeed(m_wheelSpeeds[3]);
        m_safetyHelper.feed();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MotorSafetyHelper getSafetyHelper(){
        return m_safetyHelper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopMotor() {
        m_frontLeftMotor.stopMotor();
        m_frontRightMotor.stopMotor();
        m_rearLeftMotor.stopMotor();
        m_rearRightMotor.stopMotor();
        
        // m_safetyHelper.feed();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Robot DriveBase";
    }

}
