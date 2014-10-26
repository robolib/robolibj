/*
 * Copyright (c) 2014 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package org.team2583.robolib.util.log;

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
 * @see Logger
 * @see LogOutput
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 */
public abstract class ILogger {

    /**
     * Add an output to the logger for this object.
     * 
     * Adding a new output will add that output to the list so that it will output
     * log data same as the console.
     * @param out a {@link LogOutput} instance
     */
    public abstract void registerOutput(LogOutput out);

    /**
     * Log an {@code Object} to the logger, with info status.
     * @param obj the {@code Object} to log
     */
    public void info(Object obj){
        info(String.valueOf(obj));
    }
    
    /**
     * Log a {@code boolean} to the logger, with info status.
     * @param b the {@code boolean} to log
     */
    public void info(boolean b){
        info(String.valueOf(b));
    }
    
    /**
     * Log an {@code char} to the logger, with info status.
     * @param c the {@code char} to log
     */
    public void info(char c){
        info(String.valueOf(c));
    }
    
    /**
     * Log an {@code char} to the logger, with info status.
     * @param s the {@code char} array to log
     */
    public void info(char[] s){
        info(String.valueOf(s));
    }
    
    /**
     * Log an {@code double} to the logger, with info status.
     * @param d the {@code double} to log
     */
    public void info(double d){
        info(String.valueOf(d));
    }
    
    /**
     * Log an {@code float} to the logger, with info status.
     * @param f the {@code float} to log
     */
    public void info(float f){
        info(String.valueOf(f));
    }
    
    /**
     * Log an {@code int} to the logger, with info status.
     * @param i the {@code int} to log
     */
    public void info(int i){
        info(String.valueOf(i));
    }
    
    /**
     * Log an {@code long} to the logger, with info status.
     * @param l the {@code long} to log
     */
    public void info(long l){
        info(String.valueOf(l));
    }
    
    /**
     * Log an {@code String} to the logger, with info status.
     * @param s the {@code String} to log
     */
    public abstract void info(String s);
    
    /**
     * Log an {@code Object} to the logger, with debug status.
     * @param obj the {@code Object} to log
     */
    public void debug(Object obj){
        debug(String.valueOf(obj));
    }
    
    /**
     * Log a {@code boolean} to the logger, with debug status.
     * @param b the {@code boolean} to log
     */
    public void debug(boolean b){
        debug(String.valueOf(b));
    }
    
    /**
     * Log an {@code char} to the logger, with debug status.
     * @param c the {@code char} to log
     */
    public void debug(char c){
        debug(String.valueOf(c));
    }
    
    /**
     * Log an {@code char} to the logger, with debug status.
     * @param s the {@code char} array to log
     */
    public void debug(char[] s){
        debug(String.valueOf(s));
    }
    
    /**
     * Log an {@code double} to the logger, with debug status.
     * @param d the {@code double} to log
     */
    public void debug(double d){
        debug(String.valueOf(d));
    }
    
    /**
     * Log an {@code float} to the logger, with debug status.
     * @param f the {@code float} to log
     */
    public void debug(float f){
        debug(String.valueOf(f));
    }
    
    /**
     * Log an {@code int} to the logger, with debug status.
     * @param i the {@code int} to log
     */
    public void debug(int i){
        debug(String.valueOf(i));
    }
    
    /**
     * Log an {@code long} to the logger, with debug status.
     * @param l the {@code long} to log
     */
    public void debug(long l){
        debug(String.valueOf(l));
    }
    
    /**
     * Log an {@code String} to the logger, with debug status.
     * @param s the {@code String} to log
     */
    public abstract void debug(String s);
    
    /**
     * Log an {@code Object} to the logger, with warn status.
     * @param obj the {@code Object} to log
     */
    public void warn(Object obj){
        warn(String.valueOf(obj));
    }
    
    /**
     * Log a {@code boolean} to the logger, with warn status.
     * @param b the {@code boolean} to log
     */
    public void warn(boolean b){
        warn(String.valueOf(b));
    }
    
    /**
     * Log an {@code char} to the logger, with warn status.
     * @param c the {@code char} to log
     */
    public void warn(char c){
        warn(String.valueOf(c));
    }
    
    /**
     * Log an {@code char} to the logger, with warn status.
     * @param s the {@code char} array to log
     */
    public void warn(char[] s){
        warn(String.valueOf(s));
    }
    
    /**
     * Log an {@code double} to the logger, with warn status.
     * @param d the {@code double} to log
     */
    public void warn(double d){
        warn(String.valueOf(d));
    }
    
    /**
     * Log an {@code float} to the logger, with warn status.
     * @param f the {@code float} to log
     */
    public void warn(float f){
        warn(String.valueOf(f));
    }
    
    /**
     * Log an {@code int} to the logger, with warn status.
     * @param i the {@code int} to log
     */
    public void warn(int i){
        warn(String.valueOf(i));
    }
    
    /**
     * Log an {@code long} to the logger, with warn status.
     * @param l the {@code long} to log
     */
    public void warn(long l){
        warn(String.valueOf(l));
    }
    
    /**
     * Log an {@code String} to the logger, with warning status.
     * @param s the {@code String} to log
     */
    public abstract void warn(String s);
    
    /**
     * Log an {@code Object} to the logger, with error status.
     * @param obj the {@code Object} to log
     * @param o the {@code Object} to send with the message
     */
    public void error(Object obj, Object o){
        error(String.valueOf(obj), o);
    }
    
    /**
     * Log a {@code boolean} to the logger, with error status.
     * @param b the {@code boolean} to log
     * @param o the {@code Object} to send with the message
     */
    public void error(boolean b, Object o){
        error(String.valueOf(b), o);
    }
    
    /**
     * Log an {@code char} to the logger, with error status.
     * @param c the {@code char} to log
     * @param o the {@code Object} to send with the message
     */
    public void error(char c, Object o){
        error(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code char} to the logger, with error status.
     * @param s the {@code char} array to log
     * @param o the {@code Object} to send with the message
     */
    public void error(char[] s, Object o){
        error(String.valueOf(s), o);
    }
    
    /**
     * Log an {@code double} to the logger, with error status.
     * @param d the {@code double} to log
     * @param o the {@code Object} to send with the message
     */
    public void error(double d, Object o){
        error(String.valueOf(d), o);
    }
    
    /**
     * Log an {@code float} to the logger, with error status.
     * @param f the {@code float} to log
     * @param o the {@code Object} to send with the message
     */
    public void error(float f, Object o){
        error(String.valueOf(f), o);
    }
    
    /**
     * Log an {@code int} to the logger, with error status.
     * @param i the {@code int} to log
     * @param o the {@code Object} to send with the message
     */
    public void error(int i, Object o){
        error(String.valueOf(i), o);
    }
    
    /**
     * Log an {@code long} to the logger, with error status.
     * @param l the {@code long} to log
     * @param o the {@code Object} to send with the message
     */
    public void error(long l, Object o){
        error(String.valueOf(l), o);
    }

    /**
     * Log an {@code String} to the logger, with error status.
     * @param s the {@code String} to log
     */
    public void error(String s){
        error(s, null);
    }
    
    /**
     * Log an {@code String} to the logger, with error status.
     * @param s the {@code String} to log
     * @param o the {@code Object} to send with the message
     */
    public abstract void error(String s, Object o);

    /**
     * Log an {@code Object} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param obj the {@code Object} to log
     * @param o the {@code Object} to send with the message
     */
    public void severe(Object obj, Object o){
        severe(String.valueOf(obj), o);
    }
    
    /**
     * Log a {@code boolean} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param b the {@code boolean} to log
     * @param o the {@code Object} to send with the message
     */
    public void severe(boolean b, Object o){
        severe(String.valueOf(b), o);
    }
    
    /**
     * Log an {@code char} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param c the {@code char} to log
     * @param o the {@code Object} to send with the message
     */
    public void severe(char c, Object o){
        severe(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code char} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param s the {@code char} array to log
     * @param o the {@code Object} to send with the message
     */
    public void severe(char[] s, Object o){
        severe(String.valueOf(s), o);
    }
    
    /**
     * Log an {@code double} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param d the {@code double} to log
     * @param o the {@code Object} to send with the message
     */
    public void severe(double d, Object o){
        severe(String.valueOf(d), o);
    }
    
    /**
     * Log an {@code float} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param f the {@code float} to log
     * @param o the {@code Object} to send with the message
     */
    public void severe(float f, Object o){
        severe(String.valueOf(f), o);
    }
    
    /**
     * Log an {@code int} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param i the {@code int} to log
     * @param o the {@code Object} to send with the message
     */
    public void severe(int i, Object o){
        severe(String.valueOf(i), o);
    }
    
    /**
     * Log an {@code long} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param l the {@code long} to log
     * @param o the {@code Object} to send with the message
     */
    public void severe(long l, Object o){
        severe(String.valueOf(l), o);
    }

    /**
     * Log an {@code String} to the logger, with severe status.
     * @param s the {@code String} to log
     */
    public void severe(String s){
        severe(s, null);
    }
    
    /**
     * Log an {@code String} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param s the {@code String} to log
     * @param o the {@code Object} to send with the message
     */
    public abstract void severe(String s, Object o);
    
    /**
     * Log an {@code Object} to the logger, with fatal status.
     * @param obj the {@code Object} to log
     * @param o the {@code Object} to send with the message
     */
    public void fatal(Object obj, Object o){
        fatal(String.valueOf(obj), o);
    }
    
    /**
     * Log a {@code boolean} to the logger, with fatal status.
     * @param b the {@code boolean} to log
     * @param o the {@code Object} to send with the message
     */
    public void fatal(boolean b, Object o){
        fatal(String.valueOf(b), o);
    }
    
    /**
     * Log an {@code char} to the logger, with fatal status.
     * @param c the {@code char} to log
     * @param o the {@code Object} to send with the message
     */
    public void fatal(char c, Object o){
        fatal(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code char} to the logger, with fatal status.
     * @param s the {@code char} array to log
     * @param o the {@code Object} to send with the message
     */
    public void fatal(char[] s, Object o){
        fatal(String.valueOf(s), o);
    }
    
    /**
     * Log an {@code double} to the logger, with fatal status.
     * @param d the {@code double} to log
     * @param o the {@code Object} to send with the message
     */
    public void fatal(double d, Object o){
        fatal(String.valueOf(d), o);
    }
    
    /**
     * Log an {@code float} to the logger, with fatal status.
     * @param f the {@code float} to log
     * @param o the {@code Object} to send with the message
     */
    public void fatal(float f, Object o){
        fatal(String.valueOf(f), o);
    }
    
    /**
     * Log an {@code int} to the logger, with fatal status.
     * @param i the {@code int} to log
     * @param o the {@code Object} to send with the message
     */
    public void fatal(int i, Object o){
        fatal(String.valueOf(i), o);
    }
    
    /**
     * Log an {@code long} to the logger, with fatal status.
     * @param l the {@code long} to log
     * @param o the {@code Object} to send with the message
     */
    public void fatal(long l, Object o){
        fatal(String.valueOf(l), o);
    }

    /**
     * Log an {@code String} to the logger, with fatal status.
     * @param s the {@code String} to log
     */
    public void fatal(String s){
        fatal(s, null);
    }
    
    /**
     * Log an {@code String} to the logger, with fatal status.
     * @param s the {@code String} to log
     * @param o the {@code Object} to send with the message
     */
    public abstract void fatal(String s, Object o);

}