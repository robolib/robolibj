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

package org.team2583.robolib.math;

/**
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PIDController implements IPIDController{
    
    private double p, i, d;
    
    public PIDController(){
        this(0.0, 0.0, 0.0);
    }
    
    public PIDController(double p, double i, double d){
        this.p = p;
        this.i = i;
        this.d = d;
    }

    public void setP(double p) {}

    public void setI(double i) {}

    public void setD(double d) {}

    public void setTarget(double t) {}

    public double getError() {
        return 0.0;
    }

    public double getOut() {
        return 0.0;
    }

}