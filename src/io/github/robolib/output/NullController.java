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

package io.github.robolib.output;

/**
 * A class that represents a non-existent controller.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class NullController implements SpeedController {

    private final MotorSafetyHelper m_safetyHelper;
    
    public NullController(){
        m_safetyHelper = new NullSafetyHelper(this);
    }
    
    /**
     * Do absolutely nothing
     * @return 0
     */
    public double get() {
        return 0;
    }

    /**
     * Do absolutely nothing
     */
    public void setSpeed(double speed) {}

    /**
     * Do absolutely nothing
     */
    public void setInverted(boolean inverted) {}

    /**
     * {@inheritDoc}
     */
    public MotorSafetyHelper getSafetyHelper() {
        return m_safetyHelper;
    }

    /**
     * Return "Null"
     * @return "Null"
     */
    public String getDescription() {
        return "Null";
    }

}
