/*
 * Copyright (c) 2014 noriah <vix@noriah.dev>.
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

package org.wwr.robolib.input.limitswitch;

import org.wwr.robolib.input.limitswitch.LimitSwitch.SwitchType;

/**
 * Interface for a Limit Switch
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface IStandardSwitch {
    
    public boolean state();
    
    public int getChannel();
    
    public SwitchType getType();
}
