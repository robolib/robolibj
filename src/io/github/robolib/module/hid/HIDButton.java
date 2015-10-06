/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

package io.github.robolib.module.hid;

import io.github.robolib.identifier.ButtonTrigger;


/**
 * A class representation of a Joystick Button.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface HIDButton extends ButtonTrigger {
    
    /**
     * Get the Value of the Button.
     * @return the value of the Button.
     */
    @Override
    public boolean getState();
}
