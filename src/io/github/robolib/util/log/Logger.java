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

package io.github.robolib.util.log;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.robolib.framework.DriverStation;
import io.github.robolib.framework.RoboLibBot;
import io.github.robolib.util.RoboRIO;

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
 * @author noriah Reuland <vix@noriah.dev>
 * @see ILogger
 * @see LogOutput
 * @see LogLevel
 */

@SuppressWarnings("rawtypes")
public final class Logger extends ILogger {
    
    
	/** The Constant m_loggers. */
	private static final Map<Object, Logger> m_loggers = new HashMap<>();
    
    /** The Constant m_defOuts. */
    private static final List<LogOutput> m_defOuts = new ArrayList<LogOutput>();
    
    /** The m_debugEnabled. */
    private static boolean m_debugEnabled = false;
    
    /** The m_outs. */
    private final List<LogOutput> m_outs;
    
    /** The m_label. */
    private final String m_label;
    
    /** The m_acceptGlobals. */
    private boolean m_acceptGlobals = true;
    
    
    /**
     * Get an ILogger instance for o.
     *
     * @param o the object to get the logger for.
     * @return an ILogger instance
     */
    public static ILogger get(Object o){
        return get(o, o.getClass().getSimpleName());
    }

    /**
     * Get an ILogger instance for c.
     *
     * @param c the class to get the logger for.
     * @return an ILogger instance
     */
    public static ILogger get(Class c){
        return get(c, "@" + c.getSimpleName());
    }

    /**
     * Get an ILogger instance for c.
     *
     * @param o the object to get the logger for.
     * @param s the string to be prefixed to log messages.
     * @return an ILogger instance
     */
    public static Logger get(Object o, String s){
        if(!m_loggers.containsKey(o))
            m_loggers.put(o, new Logger(s));
        
        return m_loggers.get(o);
    }
    
    

    /**
     * Instantiates a new logger.
     *
     * @param label the label
     */
    private Logger(String label){
        m_outs = new ArrayList<>();
        m_outs.addAll(m_defOuts);
        m_label = "(" + label + ")";
    }
    
    /** The Constant TERM_OUT. */
    public static final LogOutput TERM_OUT = System.out::println;

    /** The Constant TERM_ERR. */
    public static final LogOutput TERM_ERR = System.err::println;

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
            get(LogOutput.class).warn("Cannot Find/Create Log File: " + s);
            return null;
        } catch (UnsupportedEncodingException ex) {
            get(LogOutput.class).warn("What the Heck? - PrintWriter Rejected UTF-8 Encoding...");
            return null;
        }
        
        return (String msg) -> {
            mWriter.println(msg);
            mWriter.flush();
        };
    }
    
    /**
     * Adds a {@link LogOutput} to the list of default outputs.
     * Also adds the output to all of the current Loggers.
     * (If they don't already have it)
     * 
     * @param out a {@link LogOutput} instance
     */
    public static void registerDefaultOutput(LogOutput out){
        if(!m_defOuts.contains(out) && (!out.equals(TERM_OUT) || !out.equals(TERM_ERR)))
            m_defOuts.add(out);
    }

    /**
     * Register default file output.
     *
     * @param s the s
     */
    public static void registerDefaultFileOutput(String s){
        registerDefaultOutput(fileOutput(s));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOutput(LogOutput out){
        if(!m_outs.contains(out) && (!out.equals(TERM_OUT) || !out.equals(TERM_ERR)))
            m_outs.add(out);
    }
    
    public void clearOutputs(){
        m_acceptGlobals = false;
        m_outs.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableDebug(boolean enable){
        m_debugEnabled = enable;
    }

    /**
     * Add an output to all the loggers.
     *
     * @param out a {@link LogOutput} instance
     */
    public static void registerToAll(LogOutput out){
        m_loggers.values().forEach(l -> {
            if(l.m_acceptGlobals)
                l.registerOutput(out);
        });
    }
    
    /**
     * Log a message.
     *
     * @param l the Logging level
     * @param s the String to log
     */
    private void sendMsg(LogLevel l, String s){
        String sout = "[" + RoboRIO.getFPGATimestamp() + "] " + l.m_name + " <" + RoboLibBot.getRobotMode().getName() + "> " + m_label + ": " + s;
        TERM_OUT.sendMsg(sout);
        m_outs.forEach(lo -> lo.sendMsg(sout));
    }

    /**
     * Log an error message.
     *
     * @param l the Logging level
     * @param s the String to log
     */
    private void sendErrMsg(LogLevel l, String s){
        String sout = "[" + RoboRIO.getFPGATimestamp() + "] " + l.m_name + " <" + RoboLibBot.getRobotMode().getName() + "> " + m_label + ": " + s;
        DriverStation.reportError(sout + "\n");
        TERM_ERR.sendMsg(sout);
        m_outs.forEach(lo -> lo.sendMsg(sout));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void log(LogLevel lvl, String s){
        if(lvl.ordinal() < LogLevel.WARN.ordinal()){
            sendMsg(lvl, s);
        }else if(lvl.equals(LogLevel.WARN)){
            warn(s);
        }else if(lvl.equals(LogLevel.ERROR)){
            error(s);
        }else if(lvl.equals(LogLevel.SEVERE)){
            severe(s);
        }else{
            fatal(s);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String s){
        sendMsg(LogLevel.INFO, s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String s){
        if(m_debugEnabled)
            sendMsg(LogLevel.DEBUG, s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String s){
        sendErrMsg(LogLevel.WARN, s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String s, Object o){
        sendErrMsg(LogLevel.ERROR, s);
        if(o != null){
            if(o instanceof Throwable){
                StringWriter errors = new StringWriter();
                ((Throwable)o).printStackTrace(new PrintWriter(errors));
                sendErrMsg(LogLevel.ERROR, errors.toString());
            }else if(o instanceof String){
                sendErrMsg(LogLevel.ERROR, (String)o);
            }else{
                sendErrMsg(LogLevel.ERROR, o.toString());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void severe(String s, Object o){
        sendErrMsg(LogLevel.SEVERE, s);
        if(o != null){
            if(o instanceof Throwable){
                StringWriter errors = new StringWriter();
                ((Throwable)o).printStackTrace(new PrintWriter(errors));
                sendErrMsg(LogLevel.SEVERE, errors.toString());
            }else if(o instanceof String){
                sendErrMsg(LogLevel.SEVERE, (String)o);
            }else{
                sendErrMsg(LogLevel.SEVERE, o.toString());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatal(String s, Object o){
        sendErrMsg(LogLevel.FATAL, s);
        if(o != null){
            if(o instanceof Throwable){
                StringWriter errors = new StringWriter();
                ((Throwable)o).printStackTrace(new PrintWriter(errors));
                sendErrMsg(LogLevel.FATAL, errors.toString());
            }else if(o instanceof String){
                sendErrMsg(LogLevel.FATAL, (String)o);
            }else{
                sendErrMsg(LogLevel.FATAL, o.toString());
            }
        }
    }
}

/*StringWriter errors = new StringWriter();
errors.write(s + '\n');

if(o != null){
    if(o instanceof Throwable){
        ((Throwable)o).printStackTrace(new PrintWriter(errors));
    }else if(o instanceof String){
        errors.write('\t' + (String)o);
    }else{
        errors.write('\t' + o.toString());
    }
}
sendErrMsg(LogLevel.FATAL, errors.toString());*/