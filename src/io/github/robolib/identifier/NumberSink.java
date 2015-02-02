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

package io.github.robolib.identifier;

/**
 * An interface for a class that takes a double as a value.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface NumberSink {
    
    /**
     * Set the value of this sink    
     * @param value new value
     */
    void set(double value);

}
