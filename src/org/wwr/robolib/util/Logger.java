/*
 * Copyright (c) 2014 noriah vix@noriah.dev.
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
 * @author noriah Reuland
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
         * @param msg the message to be sent
         */
        public void sendMsg(String msg);
        
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
    
    public static Logger get(Object o){
        return get(o.getClass());
    }
    
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
    
    public void addLogOutput(LogOutput out){
        if(!m_outs.contains(out))
            m_outs.addElement(out);
    }
    
    private void sendMsg(String msg){
        for(int i = 0; i < m_outs.size(); i++)
            ((LogOutput) m_outs.elementAt(i)).sendMsg(msg);
    }
    
    private static String msgBuilder(Level l, String origin, String s){
        return StringUtils.buildString(new String[]{
            l.m_name, "[", ModeSwitcher.getGameModeName(), "] ", origin, s
        });
    }
        
    public void info(Object o){
        info(o.toString());
    }
    
    public void info(String msg){
        sendMsg(msgBuilder(Level.kInfo, m_origin, msg));
    }
    
    public void debug(Object o){
        debug(o.toString());
    }
    
    public void debug(String msg){
        sendMsg(msgBuilder(Level.kDebug, m_origin, msg));
    }
    
    public void warn(Object o){
        warn(o.toString());
    }
    
    public void warn(String msg){
        sendMsg(msgBuilder(Level.kWarning, m_origin, msg));
    }
    
    public void error(Object o, Throwable e){
        error(o.toString(), e);
    }
    
    public void error(String msg, Throwable e){
        sendMsg(msgBuilder(Level.kError, m_origin, msg));
        sendMsg(msgBuilder(Level.kError, m_origin, e.getMessage()));
    }
    
    public void fatal(Object o, Throwable e){
        error(o.toString(), e);
    }
    
    public void fatal(String msg, Throwable e){
        sendMsg(msgBuilder(Level.kFatal, m_origin, msg));
        sendMsg(msgBuilder(Level.kFatal, m_origin, e.getMessage()));
        System.exit(1);
    }

}