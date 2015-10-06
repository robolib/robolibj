/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

package io.github.robolib.module.sensor;

import io.github.robolib.jni.UsageReporting;
import io.github.robolib.module.iface.DigitalIO;
import io.github.robolib.module.iface.DigitalIO.DigitalChannel;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class GearTooth extends Counter {

    private static final double GEAR_TOOTH_THRESHOLD = 55e-6;
    
    public GearTooth(DigitalIO source){
        this(source, false);
    }
    
    public GearTooth(DigitalChannel channel){
        this(channel, false);
    }
    
    public GearTooth(DigitalIO source, boolean dirSensitive){
        super(source);
        enableDirectionSensing(dirSensitive);
    }
    
    public GearTooth(DigitalChannel channel, boolean dirSensitive){
        super(channel);
        enableDirectionSensing(dirSensitive);
        
        if(dirSensitive) {
            UsageReporting.report(UsageReporting.ResourceType_GearTooth, channel.ordinal(), 0, "D");
        } else {
            UsageReporting.report(UsageReporting.ResourceType_GearTooth, channel.ordinal(), 0);
        }
    }
    
    public void enableDirectionSensing(boolean dirSensitive){
        if(dirSensitive)
            setPulseLengthMode(GEAR_TOOTH_THRESHOLD);
    }
    
    public String getSmartDashboardType() {
        return "Gear Tooth";
    }
}
