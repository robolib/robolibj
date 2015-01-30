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

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.hal.AccelerometerJNI;
import io.github.robolib.livewindow.LiveWindowSendable;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The Class RIO_Accelerometer.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class RIO_Accelerometer implements IAccelerometer, LiveWindowSendable {

    
    /** The m_table. */
    private ITable m_table;
    
    private AccelRange m_range;
    
    /**
     * Instantiates a new RI o_ accelerometer.
     */
    public RIO_Accelerometer(){
        this(AccelRange.k8G);
    }
    
    /**
     * Instantiates a new RI o_ accelerometer.
     *
     * @param range the range
     */
    public RIO_Accelerometer(AccelRange range){
        setAccelRange(range);
        UsageReporting.report(UsageReporting.kResourceType_Accelerometer, 0, 0, "Built-in accelerometer");
    }
    
    /**
     * {@inheritDoc}
     */
    public void setAccelRange(AccelRange range){
        m_range = range;
        AccelerometerJNI.setAccelerometerActive(false);
        
        switch(range) {
        case k2G:
            AccelerometerJNI.setAccelerometerRange(0);
            break;
        case k4G:
            AccelerometerJNI.setAccelerometerRange(1);
            break;
        case k8G:
            AccelerometerJNI.setAccelerometerRange(2);
            break;
        case k16G:
            throw new RuntimeException("16G range not supported (use k2G, k4G, or k8G)");
        }
        
        AccelerometerJNI.setAccelerometerActive(true);
    }
    
    /**
     * {@inheritDoc}
     */
    public AccelRange getAccelRange(){
        return m_range;        
    }
    
    /**
     * Gets the x.
     *
     * @return The acceleration of the RoboRIO along the X axis in g-forces
     */
    public double getAccelerationX() {
        return AccelerometerJNI.getAccelerometerX();
    }

    /**
     * Gets the y.
     *
     * @return The acceleration of the RoboRIO along the Y axis in g-forces
     */
    public double getAccelerationY() {
        return AccelerometerJNI.getAccelerometerY();
    }

    /**
     * Gets the z.
     *
     * @return The acceleration of the RoboRIO along the Z axis in g-forces
     */
    public double getAccelerationZ() {
        return AccelerometerJNI.getAccelerometerZ();
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    public ITable getTable(){
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    public String getSmartDashboardType(){
        return "Internal3AxisAccelerometer";
    }

    /**
     * {@inheritDoc}
     */
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("X", getAccelerationX());
            m_table.putNumber("Y", getAccelerationY());
            m_table.putNumber("Z", getAccelerationZ());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void startLiveWindowMode() {

    }

    /**
     * {@inheritDoc}
     */
    public void stopLiveWindowMode() {

    }
}
