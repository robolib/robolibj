/*
 * Copyright (c) 2014 noriah <vix@noriah.dev>.
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

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import org.team2583.robolib.math.PIDController;

/**
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PIDMotor implements SpeedController, PIDOutput{
    
    private final SpeedController motor;
    private final PIDController pidC;
    
    public PIDMotor(SpeedController motor){
        this(motor, 0.0, 0.0, 0.0);
    }
    
    public PIDMotor(SpeedController motor, double p, double i, double d){
        this(motor, new PIDController());
    }
    
    public PIDMotor(SpeedController motor, PIDController pidC){
        this.motor = motor;
        this.pidC = pidC;
    }
    
    public double get() {
        return 0.0;
    }

    public void set(double d, byte b) {
        
    }

    public void set(double d) {
        
    }

    public void disable() {
        
    }

    public void pidWrite(double d) {
        
    }
    
}