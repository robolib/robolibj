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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
 * @see Logger
 * @see ILogger
 */
public abstract class LogOutput{
        
    /**
     * Sends the message to the output.
     * @param s the message to be sent
     */
    public abstract void sendMsg(String s);
        
    
    /** The Constant TERM_OUT. */
    public static final LogOutput TERM_OUT = new LogOutput(){
        public void sendMsg(String msg){
            System.out.println(msg);
        }
    };

    /** The Constant TERM_ERR. */
    public static final LogOutput TERM_ERR = new LogOutput(){
        public void sendMsg(String msg){
            System.err.println(msg);
        }
    };

    /**
     * File output.
     *
     * @param s the s
     * @return the log output
     */
    @SuppressWarnings("resource")
	public static LogOutput fileOutput(final String s){
        
        final PrintWriter mWriter;
        try {
            mWriter = new PrintWriter(s, "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.get(LogOutput.class).warn("Cannot Find/Create Log File: " + s);
            return null;
        } catch (UnsupportedEncodingException ex) {
            Logger.get(LogOutput.class).warn("What the Heck? - PrintWriter Rejected UTF-8 Encoding...");
            return null;
        }
        
        return new LogOutput(){
            private final PrintWriter writer = mWriter;

            public void sendMsg(String msg){
                writer.println(msg);
                writer.flush();
            }
        };
    }
}