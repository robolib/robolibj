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
@SuppressWarnings("unused")
public final class SystemMonitor implements Runnable {
    
    private static double m_interval = 0.1;
    
    private static double m_voltWarn;
    private static double m_voltCrit;
    private static double m_voltDisable;
        
    private static double m_sysCurrentWarn;
    private static double m_sysCurrentCrit;
    private static double m_sysCurrentDisable;
    
    private static double m_memWarn;
    private static double m_memCrit;
    
    private static volatile boolean m_enabled = true;
    private static volatile boolean m_enableVoltage = true;
    private static volatile boolean m_enableMemory = true;
    private static volatile boolean m_enablePDP = true;
    private static volatile boolean m_enablePCM = true;
    private static volatile boolean m_enableRIO = true;
    private static volatile boolean m_enableNetwork = true;
    
    private static final ILogger m_log = Logger.get(SystemMonitor.class);
    
    
    private SystemMonitor(){
        
    }
    
    public static void setMonitoringInterval(double interval){
        m_interval = interval;
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enableVoltageMonitoring(boolean enabled){
        m_enableVoltage = enabled;
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enablePDPFaultMonitoring(boolean enabled){
        m_enablePDP = enabled;
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enablePCMFaultMonitoring(boolean enabled){
        m_enablePCM = enabled;
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enableRIOFaultMonitoring(boolean enabled){
        m_enableRIO = enabled;
    }
    
    /**
     * 
     * @param enabled
     */
    public static void enableNetworkMonitoring(boolean enabled){
        m_enableNetwork = enabled;
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
    
    public static void monitorSystemCurrent(double warnLevel, double critLevel, double disableLevel){
        m_sysCurrentWarn = warnLevel;
        m_sysCurrentCrit = critLevel;
        m_sysCurrentDisable = disableLevel;
    }
    
    /**
     * 
     * @param enabled
     */
    public static void monitorMemory(boolean enabled){
        
    }
    
    public void run(){
        while(true){
            
        }
        
    }
    

}
