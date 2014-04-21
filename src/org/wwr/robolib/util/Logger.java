/*
 * Copyright (c) 2014 Westwood Robotics code.westwoodrobotics@gmail.com.
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

package org.wwr.robolib.util;

import java.util.Hashtable;
import java.util.Vector;
import org.wwr.robolib.robot.ModeSwitcher;

/**
 * A Logging class for the robot.
 * 
 * <p>Each Class that wishes to log must call the {@code Logger.get} method with itself
 * as the only argument. This will return a new Logger object.</p>
 * 
 * <p>To log something, the Calling class must call the desired level method
 * {@code info, debug, warn, error, fatal} with the message to be logged. If the
 * calling the err or fatal classes, a Throwable object must be passed as a second
 * argument. Calling the {@code error} method will only print the message and
 * error stack trace. However, calling the {@code fatal} method will print the
 * stack trace, and kill the robot.</p>
 * 
 * @author Austin Reuland
 */
public final class Logger {
    
    public static final class Level {
        public final int m_value;
        public final String m_name;
        
        private Level(int value, String name){
            m_value = value;
            m_name = name;
        }
        
        public static final Level kInfo = new Level(1, "[INFO]");
        public static final Level kDebug = new Level(2, "[DEBUG]");
        public static final Level kWarning = new Level(3, "[WARNING]");
        public static final Level kError = new Level(4, "[ERROR]");
        public static final Level kFatal = new Level(5, "[FATALITY]"); //Hehe
    }
    
    /**
     * Creates a new place for the Log message to be sent.
     * 
     * Found the atalibj Library today and saw their logging format.
     * Thought it was pretty cool, trying it out here.
     * https://github.com/Team4334/atalibj/blob/master/src/edu/first/util/log/Logger.java
     */
    public static interface LogOutput{
        
        /**
         * Sends the message to the output.
         * @param s the message to be sent
         */
        public void sendMsg(String s);
        
    }
    
    private static final LogOutput LOG_OUT_TERM = new LogOutput(){
        public void sendMsg(String msg){
            System.out.println(msg);
        }
    };
    
    private static final LogOutput LOG_OUT_DASH = new LogOutput(){
        public void sendMsg(String msg){
            
        }
    };
    
    private static final Hashtable m_loggers = new Hashtable();
    private final Vector m_outs;
    private final String m_origin;
    
    
    /**
     * Get the logger instance for o
     * @param o the object to get the logger for.
     * @return a logger instance
     */
    public static Logger get(Object o){
        return get(o.getClass());
    }
    
    /**
     * Get the logger instance for c
     * @param c the class to get the logger for.
     * @return a logger instance
     */
    public static Logger get(Class c){
        if(!m_loggers.containsKey(c))
            m_loggers.put(c, new Logger(c));
        
        return (Logger) m_loggers.get(c);
    }
    
    /**
     * 
     * @param c the calling Class
     */
    private Logger(Class c){
        m_outs = new Vector();
        addLogOutput(LOG_OUT_TERM);
        m_origin = StringUtils.buildString(new String[]{"@", c.getName(), ": "});
    }
    
    /**
     * Add an output to the logger for this object.
     * 
     * Adding a new output will add that output to the list so that it will output
     * log data same as the console.
     * @param out 
     */
    public void addLogOutput(LogOutput out){
        if(!m_outs.contains(out))
            m_outs.addElement(out);
    }
    
    /**
     * Log a message
     * @param s the String to log
     */
    private void sendMsg(String s){
        for(int i = 0; i < m_outs.size(); i++)
            ((LogOutput) m_outs.elementAt(i)).sendMsg(s);
    }
    
    /**
     * 
     * @param l the Logging level
     * @param origin the logger m_origin
     * @param s the Log Message
     * @return the built string
     */
    private static String msgBuilder(Level l, String origin, String s){
        return StringUtils.buildString(new String[]{
            l.m_name, "[", ModeSwitcher.getGameModeName(), "] ", origin, s
        });
    }
        
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
    public void info(String s){
        sendMsg(msgBuilder(Level.kInfo, m_origin, s));
    }
    
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
    public void debug(String s){
        sendMsg(msgBuilder(Level.kDebug, m_origin, s));
    }
    
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
    public void warn(String s){
        sendMsg(msgBuilder(Level.kWarning, m_origin, s));
    }
    
    /**
     * Log an {@code Object} to the logger, with error status.
     * @param obj the {@code Object} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(Object obj, Throwable e){
        error(String.valueOf(obj), e);
    }
    
    /**
     * Log a {@code boolean} to the logger, with error status.
     * @param b the {@code boolean} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(boolean b, Throwable e){
        error(String.valueOf(b), e);
    }
    
    /**
     * Log an {@code char} to the logger, with error status.
     * @param c the {@code char} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(char c, Throwable e){
        error(String.valueOf(c), e);
    }
    
    /**
     * Log an {@code char} to the logger, with error status.
     * @param s the {@code char} array to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(char[] s, Throwable e){
        error(String.valueOf(s), e);
    }
    
    /**
     * Log an {@code double} to the logger, with error status.
     * @param d the {@code double} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(double d, Throwable e){
        error(String.valueOf(d), e);
    }
    
    /**
     * Log an {@code float} to the logger, with error status.
     * @param f the {@code float} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(float f, Throwable e){
        error(String.valueOf(f), e);
    }
    
    /**
     * Log an {@code int} to the logger, with error status.
     * @param i the {@code int} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(int i, Throwable e){
        error(String.valueOf(i), e);
    }
    
    /**
     * Log an {@code long} to the logger, with error status.
     * @param l the {@code long} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(long l, Throwable e){
        error(String.valueOf(l), e);
    }
    
    /**
     * Log an {@code String} to the logger, with error status.
     * @param s the {@code String} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void error(String s, Throwable e){
        sendMsg(msgBuilder(Level.kError, m_origin, s));
        sendMsg(msgBuilder(Level.kError, m_origin, e.getMessage()));
    }
    
    /**
     * Log an {@code Object} to the logger, with fatal status.
     * @param obj the {@code Object} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(Object obj, Throwable e){
        fatal(String.valueOf(obj), e);
    }
    
    /**
     * Log a {@code boolean} to the logger, with fatal status.
     * @param b the {@code boolean} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(boolean b, Throwable e){
        fatal(String.valueOf(b), e);
    }
    
    /**
     * Log an {@code char} to the logger, with fatal status.
     * @param c the {@code char} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(char c, Throwable e){
        fatal(String.valueOf(c), e);
    }
    
    /**
     * Log an {@code char} to the logger, with fatal status.
     * @param s the {@code char} array to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(char[] s, Throwable e){
        fatal(String.valueOf(s), e);
    }
    
    /**
     * Log an {@code double} to the logger, with fatal status.
     * @param d the {@code double} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(double d, Throwable e){
        fatal(String.valueOf(d), e);
    }
    
    /**
     * Log an {@code float} to the logger, with fatal status.
     * @param f the {@code float} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(float f, Throwable e){
        fatal(String.valueOf(f), e);
    }
    
    /**
     * Log an {@code int} to the logger, with fatal status.
     * @param i the {@code int} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(int i, Throwable e){
        fatal(String.valueOf(i), e);
    }
    
    /**
     * Log an {@code long} to the logger, with fatal status.
     * @param l the {@code long} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(long l, Throwable e){
        fatal(String.valueOf(l), e);
    }
    
    /**
     * Log an {@code String} to the logger, with fatal status.
     * @param s the {@code String} to log
     * @param e the {@code Throwable} object to send with the message
     */
    public void fatal(String s, Throwable e){
        sendMsg(msgBuilder(Level.kFatal, m_origin, s));
        sendMsg(msgBuilder(Level.kFatal, m_origin, e.getMessage()));
        System.exit(1);
    }

}