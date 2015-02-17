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

package io.github.robolib;

import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class SystemMonitor implements Runnable {
    
    private static double m_voltWarn;
    private static double m_voltCrit;
    private static double m_voltDisable;
    
    private static double m_memWarn;
    private static double m_memCrit;
    
    private static final ILogger m_log = Logger.get(SystemMonitor.class);
    
    
    private SystemMonitor(){
        
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enableVoltageMonitoring(boolean enabled){
        
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enablePDPFaultMonitoring(boolean enabled){
        
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enablePCMFaultMonitoring(boolean enabled){
        
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enableRIOFaultMonitoring(boolean enabled){
        
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enableNetworkMonitoring(boolean enabled){
        
    }
    
    /**
     * 
     * @param warnLevel
     */
    public static void setVoltageWarnLevel(double warnLevel){
        m_voltWarn = warnLevel;
    }
    
    /**
     * 
     * @param critLevel
     */
    public static void setVoltageCritLevel(double critLevel){
        m_voltCrit = critLevel;
    }
    
    /**
     * 
     * @param disableLevel
     */
    public static void setVoltageDisableLevel(double disableLevel){
        m_voltDisable = disableLevel;
    }
    
    /**
     * 
     * @param warnLevel
     * @param critLevel
     * @param disableLevel
     */
    public static void monitorVoltage(double warnLevel, double critLevel, double disableLevel){
        m_voltWarn = warnLevel;
        m_voltCrit = critLevel;
        m_voltDisable = disableLevel;
    }
    
    /**
     * 
     * @param enabled
     */
    public static void monitorMemory(boolean enabled){
        
    }
    
    public void run(){
        
    }
    

}
