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

package io.github.robolib.module.sensor;

import io.github.robolib.identifier.AngleSource;
import io.github.robolib.module.iface.AnalogInput;

/**
 * Potentiometer Class.
 * Wraps the AnalogInput class.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Potentiometer extends AnalogInput implements SensorModule, AngleSource {
    
    /**
     * Instantiates a new potentiometer.
     *
     * @param channel AnalogIO Channel of the Potentiometer
     * @param avgBits Number of Averaging Bits
     * @param ovrBits Number of Oversample Bits
     */
    public Potentiometer(AnalogChannel channel, int avgBits, int ovrBits){
        super(channel);
        setAverageBits(avgBits);
        setOversampleBits(ovrBits);
    }
    
    public double getAngle(){
        return 0.0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getSmartDashboardType() {
        return "Potentiometer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableModule() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableModule() {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getModuleEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}