/*
 * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package org.team2583.robolib.input.limitswitch;

/**
 * Interface for a Limit Switch.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public interface IStandardSwitch {
    
    /**
     * State.
     *
     * @return true, if successful
     */
    public boolean state();
    
    /**
     * Gets the channel.
     *
     * @return the channel
     */
    public int getChannel();
    
    /**
     * Gets the type.
     *
     * @return the type
     */
    public ESwitchType getType();
}
