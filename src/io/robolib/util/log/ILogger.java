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

package io.robolib.util.log;

import io.robolib.framework.RoboLibBot;

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
 * @author noriah Reuland <vix@noriah.dev>
 * @see Logger
 * @see LogOutput
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
     * Enable the output of debug statements.
     */
    public final void enableDebug(){
        enableDebug(true);
    }
    
    /**
     * Enable the output of debug statements.
     * 
     * @param enable enable the output of debug statements
     */
    public abstract void enableDebug(boolean enable);

    /**
     * Log an {@code Object} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     * @param obj the {@code Object} to log
     */
    public final void log(LogLevel lvl, Object obj){
        log(lvl, String.valueOf(obj));
    }

    /**
     * Log a {@code boolean} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     * @param b the {@code boolean} to log
     */
    public final void log(LogLevel lvl, boolean b){
        log(lvl, String.valueOf(b));
    }

    /**
     * Log an {@code char} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     * @param c the {@code char} to log
     */
    public final void log(LogLevel lvl, char c){
        log(lvl, String.valueOf(c));
    }

    /**
     * Log an {@code char} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     *
     * @param c the c
     */
    public final void log(LogLevel lvl, char[] c){
        log(lvl, String.valueOf(c));
    }

    /**
     * Log an {@code double} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     * @param d the {@code double} to log
     */
    public final void log(LogLevel lvl, double d){
        log(lvl, String.valueOf(d));
    }

    /**
     * Log an {@code float} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     * @param f the {@code float} to log
     */
    public final void log(LogLevel lvl, float f){
        log(lvl, String.valueOf(f));
    }

    /**
     * Log an {@code int} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     * @param i the {@code int} to log
     */
    public final void log(LogLevel lvl, int i){
        log(lvl, String.valueOf(i));
    }

    /**
     * Log an {@code long} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     * @param l the {@code long} to log
     */
    public final void log(LogLevel lvl, long l){
        log(lvl, String.valueOf(l));
    }

    /**
     * Log an {@code String} to the logger, with log status.
     * @param lvl the {@code LogLevel} to send with this message
     * @param s the {@code String} to log
     */
    public abstract void log(LogLevel lvl, String s);
    
    /**
     * Log an {@code Object} to the logger, with info status.
     * @param obj the {@code Object} to log
     */
    public final void info(Object obj){
        info(String.valueOf(obj));
    }
    
    /**
     * Log a {@code boolean} to the logger, with info status.
     * @param b the {@code boolean} to log
     */
    public final void info(boolean b){
        info(String.valueOf(b));
    }
    
    /**
     * Log an {@code char} to the logger, with info status.
     * @param c the {@code char} to log
     */
    public final void info(char c){
        info(String.valueOf(c));
    }
    
    /**
     * Log an {@code char} to the logger, with info status.
     *
     * @param c the c
     */
    public final void info(char[] c){
        info(String.valueOf(c));
    }
    
    /**
     * Log an {@code double} to the logger, with info status.
     * @param d the {@code double} to log
     */
    public final void info(double d){
        info(String.valueOf(d));
    }
    
    /**
     * Log an {@code float} to the logger, with info status.
     * @param f the {@code float} to log
     */
    public final void info(float f){
        info(String.valueOf(f));
    }
    
    /**
     * Log an {@code int} to the logger, with info status.
     * @param i the {@code int} to log
     */
    public final void info(int i){
        info(String.valueOf(i));
    }
    
    /**
     * Log an {@code long} to the logger, with info status.
     * @param l the {@code long} to log
     */
    public final void info(long l){
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
    public final void debug(Object obj){
        debug(String.valueOf(obj));
    }
    
    /**
     * Log a {@code boolean} to the logger, with debug status.
     * @param b the {@code boolean} to log
     */
    public final void debug(boolean b){
        debug(String.valueOf(b));
    }
    
    /**
     * Log an {@code char} to the logger, with debug status.
     * @param c the {@code char} to log
     */
    public final void debug(char c){
        debug(String.valueOf(c));
    }
    
    /**
     * Log an {@code char} to the logger, with debug status.
     *
     * @param c the c
     */
    public final void debug(char[] c){
        debug(String.valueOf(c));
    }
    
    /**
     * Log an {@code double} to the logger, with debug status.
     * @param d the {@code double} to log
     */
    public final void debug(double d){
        debug(String.valueOf(d));
    }
    
    /**
     * Log an {@code float} to the logger, with debug status.
     * @param f the {@code float} to log
     */
    public final void debug(float f){
        debug(String.valueOf(f));
    }
    
    /**
     * Log an {@code int} to the logger, with debug status.
     * @param i the {@code int} to log
     */
    public final void debug(int i){
        debug(String.valueOf(i));
    }
    
    /**
     * Log an {@code long} to the logger, with debug status.
     * @param l the {@code long} to log
     */
    public final void debug(long l){
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
    public final void warn(Object obj){
        warn(String.valueOf(obj));
    }
    
    /**
     * Log a {@code boolean} to the logger, with warn status.
     * @param b the {@code boolean} to log
     */
    public final void warn(boolean b){
        warn(String.valueOf(b));
    }
    
    /**
     * Log an {@code char} to the logger, with warn status.
     * @param c the {@code char} to log
     */
    public final void warn(char c){
        warn(String.valueOf(c));
    }
    
    /**
     * Log an {@code char} to the logger, with warn status.
     *
     * @param c the c
     */
    public final void warn(char[] c){
        warn(String.valueOf(c));
    }
    
    /**
     * Log an {@code double} to the logger, with warn status.
     * @param d the {@code double} to log
     */
    public final void warn(double d){
        warn(String.valueOf(d));
    }
    
    /**
     * Log an {@code float} to the logger, with warn status.
     * @param f the {@code float} to log
     */
    public final void warn(float f){
        warn(String.valueOf(f));
    }
    
    /**
     * Log an {@code int} to the logger, with warn status.
     * @param i the {@code int} to log
     */
    public final void warn(int i){
        warn(String.valueOf(i));
    }
    
    /**
     * Log an {@code long} to the logger, with warn status.
     * @param l the {@code long} to log
     */
    public final void warn(long l){
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
    public final void error(Object obj, Object o){
        error(String.valueOf(obj), o);
    }
    
    /**
     * Log a {@code boolean} to the logger, with error status.
     * @param b the {@code boolean} to log
     * @param o the {@code Object} to send with the message
     */
    public final void error(boolean b, Object o){
        error(String.valueOf(b), o);
    }
    
    /**
     * Log an {@code char} to the logger, with error status.
     * @param c the {@code char} to log
     * @param o the {@code Object} to send with the message
     */
    public final void error(char c, Object o){
        error(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code char} to the logger, with error status.
     *
     * @param c the c
     * @param o the {@code Object} to send with the message
     */
    public final void error(char[] c, Object o){
        error(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code double} to the logger, with error status.
     * @param d the {@code double} to log
     * @param o the {@code Object} to send with the message
     */
    public final void error(double d, Object o){
        error(String.valueOf(d), o);
    }
    
    /**
     * Log an {@code float} to the logger, with error status.
     * @param f the {@code float} to log
     * @param o the {@code Object} to send with the message
     */
    public final void error(float f, Object o){
        error(String.valueOf(f), o);
    }
    
    /**
     * Log an {@code int} to the logger, with error status.
     * @param i the {@code int} to log
     * @param o the {@code Object} to send with the message
     */
    public final void error(int i, Object o){
        error(String.valueOf(i), o);
    }
    
    /**
     * Log an {@code long} to the logger, with error status.
     * @param l the {@code long} to log
     * @param o the {@code Object} to send with the message
     */
    public final void error(long l, Object o){
        error(String.valueOf(l), o);
    }

    /**
     * Log an {@code String} to the logger, with error status.
     * @param s the {@code String} to log
     */
    public final void error(String s){
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
    public final void severe(Object obj, Object o){
        severe_imp(String.valueOf(obj), o);
    }
    
    /**
     * Log a {@code boolean} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param b the {@code boolean} to log
     * @param o the {@code Object} to send with the message
     */
    public final void severe(boolean b, Object o){
        severe_imp(String.valueOf(b), o);
    }
    
    /**
     * Log an {@code char} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param c the {@code char} to log
     * @param o the {@code Object} to send with the message
     */
    public final void severe(char c, Object o){
        severe_imp(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code char} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     *
     * @param c the c
     * @param o the {@code Object} to send with the message
     */
    public final void severe(char[] c, Object o){
        severe_imp(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code double} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param d the {@code double} to log
     * @param o the {@code Object} to send with the message
     */
    public final void severe(double d, Object o){
        severe_imp(String.valueOf(d), o);
    }
    
    /**
     * Log an {@code float} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param f the {@code float} to log
     * @param o the {@code Object} to send with the message
     */
    public final void severe(float f, Object o){
        severe_imp(String.valueOf(f), o);
    }
    
    /**
     * Log an {@code int} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param i the {@code int} to log
     * @param o the {@code Object} to send with the message
     */
    public final void severe(int i, Object o){
        severe_imp(String.valueOf(i), o);
    }
    
    /**
     * Log an {@code long} to the logger, with severe status.
     * If the exception passed to this method is an Error or
     * Runtime Exception, the Program will Quit
     * @param l the {@code long} to log
     * @param o the {@code Object} to send with the message
     */
    public final void severe(long l, Object o){
        severe_imp(String.valueOf(l), o);
    }

    /**
     * Log an {@code String} to the logger, with severe status.
     * @param s the {@code String} to log
     */
    public final void severe(String s){
        severe_imp(s, null);
    }
    
    /**
     * Severe_imp.
     *
     * @param s the s
     * @param o the o
     */
    private final void severe_imp(String s, Object o){
        severe(s, o);
        if(o instanceof Error || o instanceof RuntimeException)
            RoboLibBot.die();
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
     * Program will exit with status 1
     * @param obj the {@code Object} to log
     * @param o the {@code Object} to send with the message
     */
    public final void fatal(Object obj, Object o){
        fatal_imp(String.valueOf(obj), o);
    }
    
    /**
     * Log a {@code boolean} to the logger, with fatal status.
     * @param b the {@code boolean} to log
     * @param o the {@code Object} to send with the message
     */
    public final void fatal(boolean b, Object o){
        fatal_imp(String.valueOf(b), o);
    }
    
    /**
     * Log an {@code char} to the logger, with fatal status.
     * Program will exit with status 1
     * @param c the {@code char} to log
     * @param o the {@code Object} to send with the message
     */
    public final void fatal(char c, Object o){
        fatal_imp(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code char} to the logger, with fatal status.
     * Program will exit with status 1
     *
     * @param c the c
     * @param o the {@code Object} to send with the message
     */
    public final void fatal(char[] c, Object o){
        fatal_imp(String.valueOf(c), o);
    }
    
    /**
     * Log an {@code double} to the logger, with fatal status.
     * Program will exit with status 1
     * @param d the {@code double} to log
     * @param o the {@code Object} to send with the message
     */
    public final void fatal(double d, Object o){
        fatal_imp(String.valueOf(d), o);
    }
    
    /**
     * Log an {@code float} to the logger, with fatal status.
     * Program will exit with status 1
     * @param f the {@code float} to log
     * @param o the {@code Object} to send with the message
     */
    public final void fatal(float f, Object o){
        fatal_imp(String.valueOf(f), o);
    }
    
    /**
     * Log an {@code int} to the logger, with fatal status.
     * Program will exit with status 1
     * @param i the {@code int} to log
     * @param o the {@code Object} to send with the message
     */
    public final void fatal(int i, Object o){
        fatal_imp(String.valueOf(i), o);
    }
    
    /**
     * Log an {@code long} to the logger, with fatal status.
     * Program will exit with status 1
     * @param l the {@code long} to log
     * @param o the {@code Object} to send with the message
     */
    public final void fatal(long l, Object o){
        fatal_imp(String.valueOf(l), o);
    }

    /**
     * Log an {@code String} to the logger, with fatal status.
     * Program will exit with status 1
     * @param s the {@code String} to log
     */
    public final void fatal(String s){
        fatal_imp(s, null);
    }
    
    /**
     * Fatal_imp.
     *
     * @param s the s
     * @param o the o
     */
    private final void fatal_imp(String s, Object o){
        fatal(s, o);
        RoboLibBot.die();
    }
    
    /**
     * Log an {@code String} to the logger, with fatal status.
     * Program will exit with status 1
     * @param s the {@code String} to log
     * @param o the {@code Object} to send with the message
     */
    public abstract void fatal(String s, Object o);

}