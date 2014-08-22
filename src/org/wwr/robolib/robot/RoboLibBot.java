/*
 * Copyright (c) 2014 noriah <vix@noriah.dev>.
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

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.communication.FRCControl;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.wwr.robolib.util.Logger;
import org.wwr.robolib.util.StringUtils;
import javax.microedition.io.Connector;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * A better version of the WPILib IterativeRobot class.
 * It replaces RobotBase and handles all of the robot.
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
public class RoboLibBot extends MIDlet {

    public static final int MAJOR_VERSION = 0;
    public static final int MINOR_VERSION = 1;
    public static final int PATCH_VERSION = 8;
    
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
    protected final DriverStation m_ds;
    private final Watchdog m_watchdog = Watchdog.getInstance();
    private static final ModeSwitcher m_modeSwitcher = ModeSwitcher.getInstance();

    /**
     * Robot Class Method
     */
    protected RoboLibBot(){
        this("Default", "1.0.0");
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
     * @param version Version number of the robot
     */
    protected RoboLibBot(String name, String version){
        m_name = name;
        m_version = version;

        NetworkTable.setServerMode();
        NetworkTable.getTable("");
        NetworkTable.getTable("LiveWindow").getSubTable("~STATUS~");

        m_ds = DriverStation.getInstance();
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
     * Add a Compressor to the System
     * @param cs the pressure switch that the compressor is on.
     * @param cr the relay that controls the compressor
     */
    protected void compressor(int cs, int cr){
        compressor(1, cs, 1, cr);
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
     * This is run before the robot runs through its modes.
     */
    public void robotInit(){
        debug("Default Robot.robotInit() method... Overload me!");
    }

    /**
     * Get the current driver station indicated mode.
     * @return the driver station mode as a {@link GameMode}
     */
    public ModeSwitcher.GameMode getDSMode(){
        if(isDisabled())
            return ModeSwitcher.GameMode.kDisabled;
        else if(isTest())
            return ModeSwitcher.GameMode.kTest;
        else if(isAutonomous())
            return ModeSwitcher.GameMode.kAuton;
        else
            return ModeSwitcher.GameMode.kTeleop;
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
     * @throws javax.microedition.midlet.MIDletStateChangeException
     */
    protected final void startApp() throws MIDletStateChangeException {
        
        msg("RoboLibJ v" + MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION);
        msg("Starting " + m_name);

        getWatchdog().setExpiration(0.1);
        getWatchdog().setEnabled(false);
        FRCControl.observeUserProgramStarting();
        UsageReporting.report(UsageReporting.kResourceType_Language, UsageReporting.kLanguage_Java);
        
        //This bit right here eats away at the cRIOs non-volatile memory.
        //It always writes the same data, to the same point.
        //It can create a dead spot on the disk.
        //writeVersionString();
        
        UsageReporting.report(UsageReporting.kResourceType_Framework, UsageReporting.kFramework_Iterative);
            
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
            fatal("Error running User Init Code", t);
        }
            
        LiveWindow.setEnabled(false);

        msg("Initializing Robot Modes");
        m_modeSwitcher.init();

        msg(m_name + ", Version " + m_version + " Running");
        
        msg("Starting Main Loop");
        try{
            while(m_run){
                ModeSwitcher.GameMode mode = getDSMode();
                if(m_modeSwitcher.inNewMode(mode)){
                    m_modeSwitcher.switchMode(mode);
                }
                
                if(isNewDataAvailable()){
                    getWatchdog().feed();
                    m_modeSwitcher.run();
                }
                m_ds.waitForData();
            }
            
        }catch(Throwable t){
            error("Error in Main Loop. Something should have caught this!!!", t);
        }finally{
            fatal("ROBOTS DON'T QUIT!!!", new RobotException("Exited Main Loop"));
        }
    }

    /**
     * Pauses the application
     */
    protected final void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system. I.e. if startApp throws
     * any exception other than MIDletStateChangeException, if the isolate
     * running the MIDlet is killed with Isolate.exit(), or if VM.stopVM() is
     * called.
     *
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true when this method is called, the MIDlet must
     * cleanup and release all resources. If false the MIDlet may throw
     * MIDletStateChangeException to indicate it does not want to be destroyed
     * at this time.
     * @throws MIDletStateChangeException if there is an exception in
     * terminating the midlet
     */
    protected final void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }
    
    /**
     * Free the resources for a RoboLibBot class.
     */
    public void free() {
    }


    //WPI RobotBase Methods
    //Put here for compatibility.
    /**
     * Check on the overall status of the system.
     *
     * @return Is the system active (i.e. PWM motor outputs, etc. enabled)?
     */
    public boolean isSystemActive() {
        return m_watchdog.isSystemActive();
    }

    /**
     * Return the instance of the Watchdog timer. Get the watchdog timer so the
     * user program can either disable it or feed it when necessary.
     *
     * @return The Watchdog timer.
     */
    public Watchdog getWatchdog() {
        return m_watchdog;
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
    public boolean isDisabled() {
        return m_ds.isDisabled();
    }

    /**
     * Determine if the Robot is currently enabled.
     *
     * @return True if the Robot is currently enabled by the field controls.
     */
    public boolean isEnabled() {
        return m_ds.isEnabled();
    }

    /**
     * Determine if the robot is currently in Autonomous mode.
     *
     * @return True if the robot is currently operating Autonomously as
     * determined by the field controls.
     */
    public boolean isAutonomous() {
        return m_ds.isAutonomous();
    }

    /**
     * Determine if the robot is currently in Test mode
     *
     * @return True if the robot is currently operating in Test mode as
     * determined by the driver station.
     */
    public boolean isTest() {
        return m_ds.isTest();
    }

    /**
     * Determine if the robot is currently in Operator Control mode.
     *
     * @return True if the robot is currently operating in Tele-Op mode as
     * determined by the field controls.
     */
    public boolean isOperatorControl() {
        return m_ds.isOperatorControl();
    }

    /**
     * Indicates if new data is available from the driver station.
     *
     * @return Has new data arrived over the network since the last time this
     * function was called?
     */
    public boolean isNewDataAvailable() {
        return m_ds.isNewControlData();
    }

}
