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

package org.warriors2583.robolib;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
//import org.warriors2583.frc2014.RMap;

/**
 * Dashboard Logging Class.
 * @author noriah Reuland
 */
public class SS_DashLogger extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    private static final NetworkTable m_logTable;
    
    private static final String m_tableName;

    private static final SS_DashLogger m_instance = new SS_DashLogger();

    public static SS_DashLogger getInstance(){
        return m_instance;
    }

    static{
        m_tableName = "DashLogger";
        m_logTable = NetworkTable.getTable("Logger");
        
    }

    private SS_DashLogger(){
        super("SS_DashLogger");
    }
    
    public static void logDebug(String str){ 
        m_logTable.putString(m_tableName, "[D]" + str);
    }
    
    public static void logInfo(String str){
        m_logTable.putString(m_tableName, "[I]" + str);
    }
    
    public static void logWarn(String str){
        m_logTable.putString(m_tableName, "[W]" + str);
    }
    
    public static void logErr(String str){
        m_logTable.putString(m_tableName, "[E]" + str);
    }
    
    public static void logCritErr(String str){
        m_logTable.putString(m_tableName, "[CRITERROR]" + str);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
