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

package org.wwr.robolib.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import org.wwr.robolib.util.Logger;
import org.wwr.robolib.util.StringUtils;

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
public class Robot extends RobotBase {
    
    /**
     * An exception for use with robot code.
     * 
     * throw this exception when something goes wrong with the robot that does
     * not fall under any other exception type.
     */
    public static final class RobotException extends RuntimeException{

        public RobotException(String msg){
            super(msg);
        }
    }
        
    private final String m_name;
    private final String m_version;
    private final Logger m_log;
    private static boolean m_run = true;
    private static boolean m_debug = true;
    private static Compressor m_compressor = null;
    private static ITable m_table;
    private static final ModeSwitcher m_modeSwitcher = ModeSwitcher.getInstance();
    
    /**
     * Robot Class Method
     */
    protected Robot(){
        this("Default", "1.0.0");
    }

    /**
     * Robot Class Method
     * @param name Name of the Robot
     */
    protected Robot(String name){
        this(name, "1.0.0");
    }
    
    /**
     * Robot Class Method
     * @param name Name of the Robot
     * @param version Version number of the robot
     */
    protected Robot(String name, String version){
        m_name = name;
        m_version = version;
<<<<<<< HEAD:src/org/wwr/robolib/robot/Robot.java
        m_log = Logger.get(this);
=======
>>>>>>> 1019338f952265480c47f09568b67685eae39916:src/org/wwr/robolib/robot/Robot.java
        m_table = NetworkTable.getTable("Robot");
    }

    /**
     * Send a message to the Console, and to the Dashboard.
     * @param msg The message to be sent.
     */
    protected void msg(String msg){
        m_log.info(msg);
    }

    /**
     * Send a message to the Console, and to the Dashboard.
     * @param msg The message to be sent.
     */
    protected void debug(String msg){
        if(m_debug)
            m_log.debug(msg);
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
    protected void error(String msg){
        error(msg, new RuntimeException());
    }
    
    /**
     * Send out an error to the console. Crash the Robot.
     * Lets hope we don't get too many of these
     * @param msg The message to be sent with the RuntimeException
     * @param e The throwable object to send with this message
     */
    protected void error(String msg, Throwable e){
        m_run = false;
        m_log.error(msg, e);
    }
    
    /**
     * Send out fatal message to the console. Crash the Robot.
     * Lets hope we don't get too many of these
     * @param msg The message to be sent with the RuntimeException
     */
    protected void fatal(String msg){
        fatal(msg, new RuntimeException());
    }
    
    /**
     * Send out a fatal message to the console. Crash the Robot.
     * Lets hope we don't get too many of these
     * @param msg The message to be sent with the RuntimeException
     * @param e The throwable object to send with this message
     */
    protected void fatal(String msg, Throwable e){
        m_run = false;
        m_log.fatal(msg, e);
    }
    
    /**
     * Get the current driver station indicated mode.
     * @return the driver station mode as an int
     */
    private int getDSMode(){
        if(isDisabled())
            return 1;
        else if(isTest())
            return 2;
        else if(isAutonomous())
            return 3;
        else if(isOperatorControl())
            return 4;
        else
            return 1;
    }

    /**
     * The Main loop method for the robot.
     * 
     * This is called to start the robot. It should never exit.
     * If it does exit, it will first throw several exceptions to kill the robot.
     * We don't want an out of control robot.
     */
    public void startCompetition() {
<<<<<<< HEAD:src/org/wwr/robolib/robot/Robot.java
        msg("RoboLibJ Version 1.0.2");
=======
        msg("RoboLibJ Version 1.02");
>>>>>>> 1019338f952265480c47f09568b67685eae39916:src/org/wwr/robolib/robot/Robot.java
        msg("Starting " + m_name);
        UsageReporting.report(UsageReporting.kResourceType_Framework, UsageReporting.kFramework_Iterative);
        
        msg("Initializing Robot Network Table and Data");
        m_table.putString("name", m_name);
        m_table.putString("version", m_version);
        
        //Run User initialization
        msg("Running User Initialization code");
        robotInit();
        
        LiveWindow.setEnabled(false);

        msg("Initializing Robot Modes");
        m_modeSwitcher.init();

        msg(StringUtils.buildString(new String[]{m_name, ", Version ", m_version, " Running"}));
        
        msg("Starting Main Loop");
        while(m_run){
            int mode = getDSMode();
            if(m_modeSwitcher.inNewMode(mode)){
                m_modeSwitcher.switchMode(mode);
            }
            
            if(m_ds.isNewControlData()){
                getWatchdog().feed();
                m_modeSwitcher.run();
            }
            m_ds.waitForData();
        }
        m_log.fatal("Aw Nutz, Something went wrong.", new RobotException("Exited Main Loop"));
    }
    
    /**
     * Add a Compressor to the System
     * @param cs the pressure switch that the compressor is on.
     * @param cr the relay that controls the compressor
     */
<<<<<<< HEAD:src/org/wwr/robolib/robot/Robot.java
    protected void compressor(int cs, int cr){
        compressor(1, cs, 1, cr);
=======
    protected void compressor(int compressor_switch, int compressor_relay){
        msg("Adding Compressor on Relay Port " + compressor_relay + " DIO Port " + compressor_switch);
        m_compressor = new Compressor(compressor_switch, compressor_relay);
>>>>>>> 1019338f952265480c47f09568b67685eae39916:src/org/wwr/robolib/robot/Robot.java
    }
    
    /**
     * Add a Compressor to the System
     * @param csm the module that the dio input is on
     * @param cs the pressure switch that the compressor is on.
     * @param crm the module that the relay output is on
     * @param cr the relay that controls the compressor
     */
    protected void compressor(int csm, int cs,
            int crm, int cr){
        msg("Adding Compressor on Relay Port " + cr + " DIO Port " + cs);
        m_compressor = new Compressor(csm, cs, crm, cr);
    }
    
    /**
     * Add a Compressor to the System
     * @param compressor the Compressor object
     */
    protected void compressor(Compressor compressor){
        msg("Adding Compressor");
        m_compressor = compressor;
    }

    /**
     * Start the compressor.
     */
    public static void startCompressor(){
        if(m_compressor != null){
            m_compressor.start();
        }
    }
    
    /**
     * Stop the compressor.
     */
    public static void stopCompressor(){
        if(m_compressor != null){
            m_compressor.stop();
        }
    }
    
    /**
     * Get the main NetworkTable table for the robot.
     * @return a networktable ITable
     */
    public static ITable getRobotTable(){
        return m_table;
    }

    /**
     * Enable or Disable the Watchdog 
     * @param enabled enable or disable the watchdog
     */
    protected void enableWatchdog(boolean enabled){
        if(enabled){
            msg("Enabling Watchdog");
            getWatchdog().setEnabled(true);
        }else{
            msg("Disabling Watchdog");
            getWatchdog().setEnabled(false);
        }
    }

    /**
     * User Initialization code.
     * 
     * This is run before the robot runs.
     */
    public void robotInit(){
        debug("Default Robot.robotInit() method... Overload me!");
    }
}