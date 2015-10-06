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

package io.github.robolib.identifier;

/**
 * General interface for an object that can return a count.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface CounterSource {

    /**
     * Read the current counter value. Read the value at this instant. It may
     * still be running, so it reflects the current value. Next time it is read,
     * it might have a different value.
     * @return the count of this counter
     */
    int getCount();
    
    /**
     * Resets the count of this object
     */
    void reset();

}
