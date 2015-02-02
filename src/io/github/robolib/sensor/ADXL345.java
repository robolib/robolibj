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

import io.github.robolib.livewindow.LiveWindowSendable;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface ADXL345 extends IAccelerometer, LiveWindowSendable {
    
    /**
     * Axis representation
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    static enum Axis{
        X(0),
        Z(2),
        Y(4);
        public byte value;
        Axis(int val){
            value = (byte)val;
        }
    }
    
    static final int POWER_CONTROL_REGISTER = 0x2D;
    static final int DATA_FORMAT_REGISTER = 0x31;
    static final int DATA_REGISTER = 0x32;

    static final int ADDRESS_MULTI_BYTE = 0x40;
    static final int ADDRESS_READ = 0x80;

    static final int POWER_CONTROL_SLEEP = 0x04;
    static final int POWER_CONTROL_MEASURE = 0x08;
    static final int POWER_CONTROL_AUTOSLEEP = 0x10;
    static final int POWER_CONTROL_LINK = 0x20;

    static final int DATA_FORMAT_JUSTIFY = 0x04;
    static final int DATA_FORMAT_FULL_RES = 0x08;
    static final int DATA_FORMAT_INT_INVERT = 0x20;
    static final int DATA_FORMAT_SPI = 0x40;
    static final int DATA_FORMAT_SELF_TEST = 0x80;

    static final byte ADDRESS_READ_MULTI_REG = (byte) (ADDRESS_READ | ADDRESS_MULTI_BYTE | DATA_FORMAT_REGISTER);
    
    static final double GS_PER_LSB = 0.00390625;

    /**
     * {@inheritDoc}
     */
    @Override
    default double getAccelerationX() {
        return getAcceleration(Axis.X);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default double getAccelerationY() {
        return getAcceleration(Axis.Y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default double getAccelerationZ() {
        return getAcceleration(Axis.Z);
    }
    
    /**
     * Get the acceleration of one axis in Gs.
     *
     * @param axis The axis to read from.
     * @return Acceleration of the ADXL345 in Gs.
     */
    double getAcceleration(Axis axis);
    
    /**
     * Get the acceleration of all axes in Gs.
     *
     * @return An array of doubles containing the
     * acceleration measured on each axis of the ADXL345 in Gs.
     */
    double[] getAccelerations();
    
    /**
     * {@inheritDoc}
     */
    @Override
    default String getSmartDashboardType(){
        return "3AxisAccelerometer";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    default void startLiveWindowMode() {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    default void stopLiveWindowMode(){}
}
