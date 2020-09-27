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

/**
 * Interface for 3-axis accelerometers
 *
 * @author noriah <vix@noriah.dev>
 */
public interface IAccelerometer {

    /**
     * Accelerometer measurement ranges
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum AccelRange {
        k2G, k4G, k8G, k16G;
    }

    /**
     * Common interface for setting the measuring range of an accelerometer.
     *
     * @param range The maximum acceleration, positive or negative, that the
     *              accelerometer will measure. Not all accelerometers support all
     *              ranges.
     */
    // void setAccelRange(AccelRange range);

    // AccelRange getAccelRange();

    /**
     * Common interface for getting the x axis acceleration
     *
     * @return The acceleration along the x axis in g-forces
     */
    // double getAccelerationX();

    /**
     * Common interface for getting the y axis acceleration
     *
     * @return The acceleration along the y axis in g-forces
     */
    // double getAccelerationY();

    /**
     * Common interface for getting the z axis acceleration
     *
     * @return The acceleration along the z axis in g-forces
     */
    // double getAccelerationZ();

}
