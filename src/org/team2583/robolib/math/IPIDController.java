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

package org.team2583.robolib.math;

/**
 * The Interface IPIDController.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface IPIDController {
    
    /**
     * Sets the p.
     *
     * @param p the new p
     */
    public void setP(double p);
    
    /**
     * Sets the i.
     *
     * @param i the new i
     */
    public void setI(double i);
    
    /**
     * Sets the d.
     *
     * @param d the new d
     */
    public void setD(double d);
    
    /**
     * Sets the target.
     *
     * @param t the new target
     */
    public void setTarget(double t);
    
    /**
     * Gets the error.
     *
     * @return the error
     */
    public double getError();
    
    /**
     * Gets the out.
     *
     * @return the out
     */
    public double getOut();
}
