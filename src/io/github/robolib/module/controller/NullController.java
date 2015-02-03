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

package io.github.robolib.module.controller;

/**
 * A class that represents a non-existent controller.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class NullController implements SpeedController {
    
    /**
     * Do absolutely nothing
     * @return 0
     */
    @Override
    public double get() {
        return 0;
    }

    /**
     * Do absolutely nothing
     */
    @Override
    public void set(double speed) {}

    /**
     * Do absolutely nothing
     */
    @Override
    public void setInverted(boolean inverted) {}

}
