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

package io.robolib.util.log;

/**
 * A Logging class.
 * 
 * <p>Each Class that wishes to log must call the {@code Logger.get} method with itself
 * as the only argument, or with an optional string value. This will return a new Logger
 * object.</p>
 * 
 * <p>To log something, the Calling class must call the desired level method
 * {@code info, debug, warn, error, fatal} with the message to be logged. If the
 * calling the err or fatal classes, a Throwable object must be passed as a second
 * argument. Calling the {@code error} method will only print the message and
 * error stack trace. However, calling the {@code fatal} method will print the
 * stack trace, and kill the program.</p>
 *
 * @author Austin Reuland <amreuland@gmail.com>
 * @see ILogger
 * @see Logger
 * @see LogOutput
 */
public enum LogLevel {
   
    /** The info. */
    INFO("[INFO]"),
    
    /** The debug. */
    DEBUG("[DEBUG]"),
    
    /** The warn. */
    WARN("[WARNING]"),
    
    /** The error. */
    ERROR("[ERROR]"),
    
    /** The severe. */
    SEVERE("[SEVERE]"),
    
    /** The fatal. */
    FATAL("[FATALITY]");
    
    /** The m_name. */
    public final String m_name;
        
    /**
     * Instantiates a new e log level.
     *
     * @param name the name
     */
    private LogLevel(String name){
        m_name = name;
    }

}
