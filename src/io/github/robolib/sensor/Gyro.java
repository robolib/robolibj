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

package io.github.robolib.sensor;

import io.github.robolib.iface.AnalogInput;
import io.github.robolib.livewindow.LiveWindowSendable;
import io.github.robolib.pid.PIDSource;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Gyro extends AnalogInput implements IGyro, PIDSource, LiveWindowSendable {
    
    public static final int OVERSAMPLE_BITS = 10;
    public static final int AVERAGE_BITS = 0;
    
    public static final double SAMPLES_PER_SEC = 50.0;
    
    public static final double CALIBRATE_SAMPLE_TIME = 5.0;
    
    public static final double DEF_VOLTAGE_PER_DEGREE = 0.007;
    
    public Gyro(AnalogChannel channel){
        super(channel);
        
        
    }

}
