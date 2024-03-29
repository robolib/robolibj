/*
 * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
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

import io.github.robolib.identifier.LiveWindowSendable;
import io.github.robolib.jni.AccelerometerJNI;
import io.github.robolib.nettable.ITable;

/**
 * The Class RIO_Accelerometer.
 *
 * @author noriah <vix@noriah.dev>
 */
public final class InternalAccelerometer implements SensorModule, IAccelerometer, LiveWindowSendable {

    /** The m_table. */
    private ITable m_table;

    private static AccelRange m_range;

    static {
        setAccelRange(AccelRange.k8G);
    }

    /**
     * Instantiates a new RI o_ accelerometer.
     *
     * @param range the range
     */
    private InternalAccelerometer() {
    }

    /**
     * {@inheritDoc}
     */
    public static void setAccelRange(AccelRange range) {
        AccelerometerJNI.setAccelerometerActive(false);

        if (range == AccelRange.k16G) {
            throw new RuntimeException("16G range not supported (use k2G, k4G, or k8G)");
        }
        AccelerometerJNI.setAccelerometerRange(range.ordinal());
        m_range = range;

        AccelerometerJNI.setAccelerometerActive(true);
    }

    /**
     * {@inheritDoc}
     */
    public static AccelRange getAccelRange() {
        return m_range;
    }

    /**
     * Gets the x.
     *
     * @return The acceleration of the RoboRIO along the X axis in g-forces
     */
    public static double getAccelerationX() {
        return AccelerometerJNI.getAccelerometerX();
    }

    /**
     * Gets the y.
     *
     * @return The acceleration of the RoboRIO along the Y axis in g-forces
     */
    public static double getAccelerationY() {
        return AccelerometerJNI.getAccelerometerY();
    }

    /**
     * Gets the z.
     *
     * @return The acceleration of the RoboRIO along the Z axis in g-forces
     */
    public static double getAccelerationZ() {
        return AccelerometerJNI.getAccelerometerZ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSmartDashboardType() {
        return "Internal3AxisAccelerometer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
    public void startLiveWindowMode() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {

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
