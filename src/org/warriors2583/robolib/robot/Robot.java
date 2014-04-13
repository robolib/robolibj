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

package org.warriors2583.robolib.robot;

import com.sun.squawk.*;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import java.io.IOException;

/**
 * A better version of the WPILib IterativeRobot class.
 * It extends WPILib RobotBase, and takes care of all the methods.
 * It also handles any compressor the robot may have, it handles Loading the
 * robot and sending NetworkTable values about the current state.
 * 
 * This Class handles switching of Game Modes better than the default
 * IterativeRobot. It has a more complex program flow, however it runs faster
 * and uses less memory.
 * 
 * You must make your own class and have it extend this class to use it.
 * The constructor must call the Super class with either no arguments, 
 * or two strings representing the Name of the robot and the version of
 * the code.
 * @author Austin Reuland
 */
public class Robot extends RobotBase implements IRoboBase{
    
    private String m_name;
    private String m_version;
    private static ITable m_table;
    private static ITableListener m_tableListener;
    private static boolean m_debug = true;
    private static boolean m_run = true;
    private static DriverStation m_rds;
    private static ModeSwitcher m_modeSwitcher;
    
    private static final String NETTABLE_REBOOT_KEY = "Reboot";
    
    //private static RMap m_map = null;
    private String m_map = null;
    
    private static Compressor m_compressor = null;
    
    protected Robot(){
        this("Default", "1.0");
    }

    /**
     * Robot Class Method
     * @param name Name of the Robot
     * @param version Version number of the robot
     */
    protected Robot(String name, String version){
        m_name = name;
        m_version = version;
        m_rds = DriverStation.getInstance();
        m_modeSwitcher = ModeSwitcher.getInstance();
        m_table = NetworkTable.getTable("Robot");
        m_tableListener = new ITableListener(){

            public void valueChanged(ITable table, String key, Object value, boolean isNew) {
                if(key.equalsIgnoreCase(NETTABLE_REBOOT_KEY)){
                    rebootSystem();
                }
            }
            
        };
    }

    /**
     * Send a message to the Console, and to the Dashboard.
     * @param msg The message to be sent.
     */
    public static void msg(String msg){
        System.out.println(msg);
    }

    /**
     * Send a message to the Console, and to the Dashboard.
     * @param msg The message to be sent.
     */
    public static void debug(String msg){
        if(m_debug)
            System.out.println(msg);
    }

    /**
     * Enable or Disable Debug Messages
     * @param debug Enable or Disable
     */
    public void enableDebug(boolean debug){
        m_debug = debug;
    }

    /**
     * Send out an error to the console. Crash the Robot.
     * Lets hope we don't get too many of these
     * @param msg The message to be sent with the RuntimeException
     */
    public static void error(String msg){
        m_run = false;
        throw new RuntimeException(msg);
    }
    
    private void runShutdown(){
        
    }
    
    private void rebootSystem(){
        runShutdown();
        try {
            Runtime.getRuntime().exec("reboot");
        } catch (IOException ex) {}
    }
    
    public static ModeSwitcher.GameMode getGameMode(){
        return m_modeSwitcher.getGameMode();
    }
    
    public static Alliance getAlliance(){
        return m_rds.getAlliance();
    }
    
    public static boolean onField(){
        return m_rds.isFMSAttached();
    }
    
    private int getDSMode(){
        int n = 0;
        n += (isOperatorControl() ? 4 : 0);
        n += (isAutonomous() ? 3 : 0);
        n += (isTest() ? 2 : 0);
        n += (isDisabled() ? 1 : 0);
        return n;
    }

    private boolean inNewMode(int mode){
        return m_modeSwitcher.inNewMode(mode);
    }
    
    public void startCompetition() {
        debug("Robot Library Version 3.11");
        debug("Starting " + m_name);
        UsageReporting.report(UsageReporting.kResourceType_Framework, UsageReporting.kFramework_Iterative);
        //if(m_map == null){
        //    error("you must call Robot.setRMap() in the Class Constructor");
        //}
        debug("Initializing Robot Network Table and Data");
        m_table.putString("name", m_name);
        m_table.putString("version", m_version);
        
        
        debug("Initializing Robot Modes");
        m_modeSwitcher.init();
        
        //Run User initialization
        debug("Running User Initialization code");
        robotInit();
        LiveWindow.setEnabled(false);

        msg(m_name + ", Verion " + m_version + " Initiated");
        
        debug("Starting Main Loop");
        while(m_run){
            int mode = getDSMode();
            if(inNewMode(mode)){
                m_modeSwitcher.switchMode(mode);
            }
            
            if(m_ds.isNewControlData()){
                getWatchdog().feed();
                m_modeSwitcher.run();
            }
            m_ds.waitForData();
        }
    }
    
    /**
     * Add a Compressor to the System
     * @param compressor_switch
     * @param compressor_relay 
     */
    protected static void compressor(int compressor_switch, int compressor_relay){
        debug("Adding Compressor on Relay Port " + compressor_relay + " DIO Port " + compressor_switch);
        m_compressor = new Compressor(compressor_switch, compressor_relay);
    }

    protected static void compressor(Compressor compressor){
        debug("Adding Compressor");
        m_compressor = compressor;
    }

    public static void startCompressor(){
        if(m_compressor != null){
            m_compressor.start();
        }
    }
    
    public static void stopCompressor(){
        if(m_compressor != null){
            m_compressor.stop();
        }
    }
    
    public static ITable getRobotTable(){
        return m_table;
    }

    /**
     * Enable or Disable the Watchdog 
     * @param enabled
     */
    protected void enableWatchdog(boolean enabled){
        if(enabled){
            debug("Enabling Watchdog");
            getWatchdog().setEnabled(true);
        }else{
            debug("Disabling Watchdog");
            getWatchdog().setEnabled(false);
        }
    }

    public void robotInit(){
        debug("Default Robot.robotInit() method... Overload me!");
    }
    
    public IRoboMap getRMap() {
        return null;
    }
}