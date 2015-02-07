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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.github.robolib.jni.UsageReporting;
import io.github.robolib.module.iface.SPI;

import edu.wpi.first.wpilibj.networktables.ITable;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class ADXL345_SPI extends SPI implements ADXL345 {

    private AccelRange m_range;
    
    /**
     * 
     * @param port
     * @param range
     */
    public ADXL345_SPI(Port port, AccelRange range) {
        super(port);
        setClockRate(500000);
        setMSBFirst();
        setSampleDataOnFalling();
        setClockActiveLow();
        setChipSelectActiveHigh();

        write(new byte[]{
                POWER_CONTROL_REGISTER,
                POWER_CONTROL_MEASURE
        }, 2);

        setAccelRange(range);

        UsageReporting.report(UsageReporting.ResourceType_ADXL345, UsageReporting.ADXL345_SPI);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccelRange(AccelRange range) {
        write(new byte[]{
                DATA_FORMAT_REGISTER,
                (byte)(DATA_FORMAT_FULL_RES | range.ordinal())
        }, 2);
        m_range = range;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccelRange getAccelRange() {
        return m_range;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAcceleration(Axis axis){
        byte[] buffer = new byte[3];
        buffer[0] = (byte) (ADDRESS_READ_MULTI_REG + axis.value);
        transaction(buffer, buffer, 3);
        ByteBuffer raw = ByteBuffer.wrap(buffer, 1, 2);
        raw.order(ByteOrder.LITTLE_ENDIAN);
        return raw.getShort() * GS_PER_LSB;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getAccelerations(){
        byte[] buffer = new byte[7];
        buffer[0] = ADDRESS_READ_MULTI_REG;
        transaction(buffer, buffer, 7);
        ByteBuffer raw = ByteBuffer.wrap(buffer, 1, 6);
        raw.order(ByteOrder.LITTLE_ENDIAN);
        return new double[]{
                raw.getShort() * GS_PER_LSB,
                raw.getShort() * GS_PER_LSB,
                raw.getShort() * GS_PER_LSB
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
