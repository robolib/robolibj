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

package org.team2583.robolib.output;

// TODO: Auto-generated Javadoc
/**
 * The Enum ControllerType.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public enum ControllerType {
    
    /** The Victor. */
    Victor(2.027, 1.525, 1.507, 1.49, 1.026),
    
    /** The Victor sp. */
    VictorSP(2.004, 1.52, 1.50, 1.48, 0.997),
    
    /** The Talon. */
    Talon(2.037, 1.539, 1.513, 1.487, 0.989),
    
    /** The Talon srx. */
    TalonSRX(2.004, 1.52, 1.50, 1.48, 0.997),
    
    /** The Jaguar. */
    Jaguar(2.31, 1.55, 1.507, 1.454, 0.697);
    
    
    
    /** The max. */
    public final double max;
    
    /** The dead max. */
    public final double deadMax;
    
    /** The center. */
    public final double center;
    
    /** The dead min. */
    public final double deadMin;
    
    /** The min. */
    public final double min;
    
    /**
     * Instantiates a new controller type.
     *
     * @param max the max
     * @param deadMax the dead max
     * @param center the center
     * @param deadMin the dead min
     * @param min the min
     */
    private ControllerType(double max, double deadMax, double center, double deadMin, double min){
        this.max = max;
        this.deadMax = deadMax;
        this.center = center;
        this.deadMin = deadMin;
        this.min = min;
    }

}
