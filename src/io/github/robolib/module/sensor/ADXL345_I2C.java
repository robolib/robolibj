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

import io.github.robolib.module.iface.I2C;

import edu.wpi.first.wpilibj.networktables.ITable;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class ADXL345_I2C extends I2C implements ADXL345 {

    private AccelRange m_range;
    
    /**
     * @param port
     * @param address
     */
    public ADXL345_I2C(Port port, int address) {
        super(port, address);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccelRange(AccelRange range) {
        write(DATA_FORMAT_REGISTER, DATA_FORMAT_FULL_RES | range.ordinal());
        m_range = range;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccelRange getAccelRange() {
        return m_range;
    }
    
    public double getAcceleration(Axis axis){
        short[] buffer = new short[1];
        readWord(DATA_FORMAT_REGISTER + axis.value, buffer);
        return buffer[0] * GS_PER_LSB;
    }
    
    public double[] getAccelerations(){
        short[] buffer = new short[3];
        readWords(DATA_FORMAT_REGISTER, buffer, 3);
        return new double[]{
                buffer[0] * GS_PER_LSB,
                buffer[0] * GS_PER_LSB,
                buffer[0] * GS_PER_LSB
        };
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable subtable) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTable() {
        // TODO Auto-generated method stub
        
    }
}
