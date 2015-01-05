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

package org.team2583.robolib.robot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

//import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.internal.HardwareHLUsageReporting;
import edu.wpi.first.wpilibj.internal.HardwareTimer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import org.team2583.robolib.exception.RobotException;
import org.team2583.robolib.util.log.ILogger;
import org.team2583.robolib.util.log.Logger;

/**
 * A better version of the WPILib IterativeRobot class.
 * It replaces RobotBase and handles much of the robots functions.
 * It also handles any compressor the robot may have, Loading the
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
 * @since 0.1.0
 * @author noriah Reuland <vix@noriah.dev>
 */
public class RoboLibBot {

    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 0;
    public static final int PATCH_VERSION = 0;

    private final String m_name;
    private final String m_version;
    private final ILogger m_log;
    private static boolean m_run = true;
    private static boolean m_debug = true;
    private static ITable m_table;
    protected static final DriverStation m_ds = DriverStation.getInstance();
    private static final ModeSwitcher m_modeSwitcher = ModeSwitcher.getInstance();

    /**
     * Robot Class Method
     */
    protected RoboLibBot(){
        this("RoboLibBot", "1.0.0");
    }

    /**
     * Robot Class Method
     * @param name Name of the Robot
     */
    protected RoboLibBot(String name){
        this(name, "1.0.0");
    }
    
    /**
     * Robot Class Method
     * @param name Name of the Robot
     * @param version Version number of the robot code
     */
    protected RoboLibBot(String name, String version){
        m_name = name;
        m_version = version;

        NetworkTable.setServerMode();
        NetworkTable.getTable("");
        NetworkTable.getTable("LiveWindow").getSubTable("~STATUS~").putBoolean("LW Enabled", false);

        m_table = NetworkTable.getTable("Robot");
        m_log = Logger.get(this);
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
     *  Lets hope we don't get too many of these
     * @param msg The message to be sent with the RuntimeException
     * @param e The throwable object to send with this message
     */
    protected void fatal(String msg, Throwable e){
        m_run = false;
        m_log.fatal(msg, e);
    }
    
    /**
     * Get the main NetworkTable table for the robot.
     * @return a networktable ITable
     */
    public static ITable getRobotTable(){
        return m_table;
    }

    /**
     * User Initialization code.
     * 
     * This is run before the robot runs through its modes.
     */
    public void robotInit(){
        debug("Default Robot.robotInit() method... Overload me!");
    }

    /**
     * Get the current driver station indicated mode.
     * @return the driver station mode as a {@link GameMode}
     */
    public GameMode getDSMode(){
        if(isDisabled())
            return GameMode.DISABLED;
        else if(isTest())
            return GameMode.TEST;
        else if(isAutonomous())
            return GameMode.AUTON;
        else
            return GameMode.TELEOP;
    }

    /**
     * The Main method for the robot.
     *
     * Starting point for the applications. Starts the OtaServer and then runs
     * the robot.
     *
     * This is called to start the robot. It should never exit.
     * If it does exit, it will first throw several exceptions to kill the robot.
     * We don't want an out of control robot.
     *
     */
    protected final void main(String args[]) {//throws MIDletStateChangeException {
        
        msg("RoboLibJ v" + MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION);
        msg("Starting " + m_name);
        
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationReserve();
        Timer.SetImplementation(new HardwareTimer());
        HLUsageReporting.SetImplementation(new HardwareHLUsageReporting());
        RobotState.SetImplementation(DriverStation.getInstance());

        UsageReporting.report(tResourceType.kResourceType_Language, tInstances.kLanguage_Java);
        
            
        msg("Initializing Robot Network Table and Data");
        try{
            m_table.putString("name", m_name);
            m_table.putString("version", m_version);
        }catch(Throwable t){
            fatal("Could not set Robot Name and Version in the Network Table. Did Something Screw Up?", t);
        }
        

        //Run User initialization
        msg("Running User Initialization code");
        try{
            robotInit();
        }catch(Throwable t){
            DriverStation.reportError("ERROR Unhandled exception instantiating robot " + m_name + " " + t.toString() + " at " + Arrays.toString(t.getStackTrace()), false);
            fatal("Error running User Init Code", t);
        }

        checkVersionFile(new File("/tmp/frc_versions/FRC_Lib_Version.ini"));
        UsageReporting.report(tResourceType.kResourceType_Framework, tInstances.kFramework_Iterative);
        LiveWindow.setEnabled(false);

        msg("Initializing Robot Modes");
        m_modeSwitcher.init();

        msg(m_name + ", Version " + m_version + " Running");
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramStarting();
        
        msg("Starting Main Loop");
        try{
            while(m_run){
                GameMode mode = getDSMode();
                if(m_modeSwitcher.inNewMode(mode)){
                    m_modeSwitcher.switchMode(mode);
                }
                
                if(isNewDataAvailable()){
                    m_modeSwitcher.run();
                }
                m_ds.waitForData();
            }
            
        }catch(Throwable t){
            DriverStation.reportError("ERROR Unhandled exception: " + t.toString() + " at " + Arrays.toString(t.getStackTrace()), false);
            error("Error in Main Loop. Something should have caught this!!!", t);
        }finally{
            fatal("ROBOTS DON'T QUIT!!!", new RobotException("Exited Main Loop"));
        }
        System.exit(1);
    }
    
    @SuppressWarnings("null")
    private static void checkVersionFile(File file){
        
        if(!file.exists()){
            writeVersionFile(file);
        }else{
            byte[] data = null;
            try{
                FileInputStream input = new FileInputStream(file);
                input.read(data);
                input.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            if(!data.equals("2015 Java 1.0.0".getBytes())){
                file.delete();
                writeVersionFile(file);
            }
        }
        
    }
    
    private static void writeVersionFile(File file){
        FileOutputStream output = null;
        try {
            file.createNewFile();
            output = new FileOutputStream(file);
            output.write("2015 Java 1.0.0".getBytes());

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    
    /**
     * Free the resources for a RoboLibBot class.
     */
    public void free() {
    }

    /**
     * @return If the robot is running in simulation.
     */
    public static boolean isSimulation() {
        return false;
    }

    /**
     * @return If the robot is running in the real world.
     */
    public static boolean isReal() {
        return true;
    }

    /**
     * Determine if the Robot is currently disabled.
     *
     * @return True if the Robot is currently disabled by the field controls.
     */
    public static boolean isDisabled() {
        return !isEnabled();
    }

    /**
     * Determine if the Robot is currently enabled.
     *
     * @return True if the Robot is currently enabled by the field controls.
     */
    public static boolean isEnabled() {
        return m_ds.isEnabled();
    }

    /**
     * Determine if the robot is currently in Autonomous mode.
     *
     * @return True if the robot is currently operating Autonomously as
     * determined by the field controls.
     */
    public static boolean isAutonomous() {
        return m_ds.isAutonomous();
    }

    /**
     * Determine if the robot is currently in Test mode
     *
     * @return True if the robot is currently operating in Test mode as
     * determined by the driver station.
     */
    public static boolean isTest() {
        return m_ds.isTest();
    }

    /**
     * Determine if the robot is currently in Operator Control mode.
     *
     * @return True if the robot is currently operating in Tele-Op mode as
     * determined by the field controls.
     */
    public static boolean isOperatorControl() {
        return m_ds.isOperatorControl();
    }
    
    /**
     * Check on the overall status of the system.
     *
     * @return Is the system active (i.e. PWM motor outputs, etc. enabled)?
     */
    public static boolean isSysActive() {
        return m_ds.isSysActive();
    }
    
    /**
     * Check if the system is browned out.
     * 
     * @return True if the system is browned out
     */
    public static boolean isBrownedOut() {
        return m_ds.isBrownedOut();
    }

    /**
     * Indicates if new data is available from the driver station.
     *
     * @return Has new data arrived over the network since the last time this
     * function was called?
     */
    public static boolean isNewDataAvailable() {
        return m_ds.isNewControlData();
    }

}
