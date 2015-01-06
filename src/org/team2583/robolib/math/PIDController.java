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

package org.team2583.robolib.math;

/**
 * The Class PIDController.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class PIDController implements IPIDController{
    
    /** The d. */
    private double p, i, d;
    
    /**
     * Instantiates a new PID controller.
     */
    public PIDController(){
        this(0.0, 0.0, 0.0);
    }
    
    /**
     * Instantiates a new PID controller.
     *
     * @param p the p
     * @param i the i
     * @param d the d
     */
    public PIDController(double p, double i, double d){
        this.p = p;
        this.i = i;
        this.d = d;
    }

    /**
     * {@inheritDoc}
     */
    public void setP(double p) {}

    /**
     * {@inheritDoc}
     */
    public void setI(double i) {}

    /**
     * {@inheritDoc}
     */
    public void setD(double d) {}

    /**
     * {@inheritDoc}
     */
    public void setTarget(double t) {}

    /**
     * {@inheritDoc}
     */
    public double getError() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    public double getOut() {
        return 0.0;
    }

}