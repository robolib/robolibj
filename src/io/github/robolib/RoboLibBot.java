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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.jar.Manifest;

import io.github.robolib.command.Scheduler;
import io.github.robolib.jni.NetworkCommunications;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.module.Compressor;
import io.github.robolib.module.PDP;
import io.github.robolib.module.RoboRIO;
import io.github.robolib.nettable.ITable;
import io.github.robolib.nettable.NetworkTable;
import io.github.robolib.robot.AutonMode;
import io.github.robolib.robot.DisabledMode;
import io.github.robolib.robot.GameMode;
import io.github.robolib.robot.TeleopMode;
import io.github.robolib.robot.TestMode;
import io.github.robolib.util.MathUtils;
import io.github.robolib.util.TableSender;
import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

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
 * 
 * Handles the Switching of the Game mode and the Execution of the mode.
 * This controls what GameMode the robot should run.
 *
 * @author noriah Reuland <vix@noriah.dev>
 * @since 0.1.0
 */
public class RoboLibBot {

    /** RoboLibJ Major Version */
    public static final int MAJOR_VERSION = 2;
    
    /** RoboLibJ Minor Version */
    public static final int MINOR_VERSION = 1;
    
    /** RoboLibJ Patch Version */
    public static final int PATCH_VERSION = 2;
    
    public static final String FRC_JAVA_VERSION = "RoboLibJ "
            + MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION;
    
    /** The Constant NETTABLE_CURRENT_MODE. */
    private static final String NETTABLE_CURRENT_MODE = "mode";
    
    /** The Constant NETTABLE_CURRENT_MODE_STRING. */
    private static final String NETTABLE_CURRENT_MODE_STRING = "mode-string";
    
    /** The m_current mode. */
    private static GameMode m_currentMode = GameMode.NONE;
    
    /** The m_current robot mode. */
    private static RobotMode m_currentRobotMode;
    
    /** The m_modes. */
    private static final EnumMap<GameMode, RobotMode> MODES_MAP =
            new EnumMap<GameMode, RobotMode>(GameMode.class);

    //private final RobotMode m_modes[];
    
    private static volatile boolean m_thread_keepAlive = true;

    /** The m_name. */
    private static String m_name;
    
    /** The m_version. */
    private static String m_version;
    
    /** The m_log. */
    protected static ILogger m_log;
    
    /** The m_run. */
//    private static boolean m_run = true;
    
    /** The m_table. */
    private static ITable m_table;

    /**
     * Robot Class Method.
     */
    protected RoboLibBot(){
        this("RoboLibBot", "1.0.0");
    }

    /**
     * Robot Class Method.
     *
     * @param name Name of the Robot
     */
    protected RoboLibBot(String name){
        this(name, "1.0.0");
    }
    
    /**
     * Robot Class Method.
     *
     * @param name Name of the Robot
     * @param version Version number of the robot code
     */
    protected RoboLibBot(String name, String version){
        m_name = name;
        m_version = version;
        m_log = Logger.get(this.getClass());
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
        m_log.debug(msg);
    }
    
    /**
     * Enable Debug Messages.
     */
    protected final void enableDebugStatements(){
        enableDebug(true);
    }

    /**
     * Enable or Disable Debug Messages.
     *
     * @param debug Enable or Disable
     */
    protected final void enableDebug(boolean debug){
        m_log.enableDebug(debug);
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
//        m_run = false;
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
//        m_run = false;
        m_log.fatal(msg, e);
    }

    /**
     * Kill the robot.
     */
    public static void die(){
        m_thread_keepAlive = false;
    }
    
    /**
     * Get the main NetworkTable table for the robot.
     * @return a networktable ITable
     */
    public static final ITable getRobotTable(){
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
     * The Main method for the robot.
     * 
     * Starting point for the applications. Starts the OtaServer and then runs
     * the robot.
     * 
     * This is called to start the robot. It should never exit.
     * If it does exit, it will first throw several exceptions to kill the robot.
     * We don't want an out of control robot.
     * 
     * Initializes the Game Mode Switcher.
     * This method must be called when ready to start the robot.
     * 
     * Checks each Mode to see if there is code to run, if not, a Default class
     * will be added.
     * 
     * Main task for the Game Manager
     * 
     * <p>This method does three main things.</p>
     * <p>Checks to see whether we need to switch RobotMode or not</p>
     * <p>If needed, switch the current game mode. This will first call the end()
     * Method of the current {@link RobotMode}, then set the current {@link GameMode},
     * call the System Garbace Collector, and then call the new RobotMode init() method.</p>
     * <p>Finally, Runs the Current RobotMode. Catches any Throwable objects that may be thrown.
     * Any caught Throwable Object is treated as fatal and will kill the RoboLibBot.
     * They are treated as fatal because any uncaught Throwables can only be 
     * RuntimeExceptions or Errors, which are Fatal</p>
     *
     * @param args the arguments
     * @see DisabledMode
     * @see TestMode
     * @see AutonMode
     * @see TeleopMode
     */
    public static final void main(String args[]) {
        Thread.currentThread().setName("Framework Thread");
//        System.out.println("waiting 30 seconds");
        
//        Thread.currentThread().setPriority(9);
        NetworkCommunications.NetworkCommunicationReserve();
        ILogger log = Logger.get(RoboLibBot.class, "Framework");
        
//        long time = System.currentTimeMillis();
        
//        while(System.currentTimeMillis() - time < 30000){}

        log.info("RoboLibJ v" + MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION);
        
        log.info("Initializing Network Tables");
        
        try {
            NetworkTable.initialize();
        } catch (Throwable t) {
            log.fatal("Failed initialize Network Tables", t);
        }

        m_table = NetworkTable.getTable("Robot");

        NetworkTable.getTable("LiveWindow").getSubTable("~STATUS~").putBoolean("LW Enabled", false);

        try {
            DriverStation.initialize();
            Scheduler.initialize();
            PDP.initialize();
            Compressor.initialize();
            RoboRIO.initialize();
            TableSender.addFramework(PDP.getInstance(), "Power/PDP");
            TableSender.addFramework(RoboRIO.getInstance(), "Power/RIO");
            TableSender.addFramework(Compressor.getInstance(), "Compressor");
        }catch(Throwable t){
            log.fatal("Failure creating framework", t);
        }
        
        
        String robotName = "";
        InputStream is = RoboLibBot.class.getResourceAsStream("/META-INF/MANIFEST.MF");
        Manifest manifest;
        try {
            manifest = new Manifest(is);
            robotName = manifest.getMainAttributes().getValue("Robot-Class");
        } catch (IOException e) {
            log.fatal("Could not open manifest file.", e);
        }
        
        if(robotName == null)
            log.fatal("No 'Robot-Class' in manifest file.");

        RoboLibBot robot;
        try {
            robot = (RoboLibBot) Class.forName(robotName).newInstance();
        } catch (Throwable t) {
            log.fatal("Could not instantiate robot " + robotName + "!", t);
            System.exit(1);
            return;
        }
        log.debug("Debug statements Enabled");
        
        log.info("Starting '" + m_name + "'");

        UsageReporting.report(UsageReporting.ResourceType_Language, UsageReporting.Language_Java);
        
            
        log.info("Initializing Robot Network Table and Data");
        try{
            m_table.putString("name", m_name);
            m_table.putString("version", m_version);
        }catch(Throwable t){
            log.error("Could not set Robot Name and Version in the Network Table. Did Something Screw Up?", t);
        }
        

        //Run User initialization
        log.info("Running User Initialization code");
        try{
            robot.robotInit();
        }catch(Throwable t){
            log.fatal("Error running User Init Code", t);
            System.exit(1);
            return;
        }

        checkVersionFile(new File("/tmp/frc_versions/FRC_Lib_Version.ini"));
        UsageReporting.report(UsageReporting.ResourceType_Framework, UsageReporting.Framework_Iterative);
        LiveWindow.setEnabled(false);

        log.info("Initializing Robot Modes");

        if(!MODES_MAP.containsKey(GameMode.DISABLED)){
            log.debug("No Disabled Robot Mode Defined");
            log.debug("Creating Empty Disabled Mode");
            new DisabledMode(){};
        }
        
        if(!MODES_MAP.containsKey(GameMode.TEST)){
            log.debug("No Test Robot Mode Defined");
            log.debug("Creating Empty Test Mode");
            new TestMode(){};
        }
        
        if(!MODES_MAP.containsKey(GameMode.AUTON)){
            log.debug("No Autonomous Robot Mode Defined");
            log.debug("Creating Empty Autonomous Mode");
            new AutonMode(){};
        }
        
        if(!MODES_MAP.containsKey(GameMode.TELEOP)){
            log.debug("No Teleop Robot Mode Defined");
            log.debug("Creating Empty Teleop Mode");
            new TeleopMode(){};
        }
        m_currentMode = GameMode.DISABLED;
        m_currentRobotMode = MODES_MAP.get(m_currentMode);
        m_table.putNumber(NETTABLE_CURRENT_MODE, m_currentMode.ordinal());
        m_table.putString(NETTABLE_CURRENT_MODE_STRING, m_currentRobotMode.getName());

        PDP.resetFaults();
        Compressor.clearCompressorStickyFaults();
        
        NetworkCommunications.ObserveUserProgramStarting();
        
        DriverStation ds = DriverStation.getInstance();
        ds.startThread();

        log.info(m_name + ", Version " + m_version + " Running");
        
        log.info("Starting Main Loop");

        GameMode gMode;
        try{
            while(m_thread_keepAlive){
                ds.waitForData();
                gMode = DriverStation.getGameMode();
                
                if(m_currentMode != gMode){
                    log.info("Switching to " + gMode.getName());

                    try{
                        m_currentRobotMode.modeEnd();
                    }catch(Throwable e){
                        Logger.get(m_currentRobotMode).fatal("Fatal error in RobotMode end method", e);
                    }
                    
                    m_currentMode = gMode;
                    m_currentRobotMode = MODES_MAP.get(m_currentMode);
                    m_table.putNumber(NETTABLE_CURRENT_MODE, gMode.ordinal());
                    m_table.putString(NETTABLE_CURRENT_MODE_STRING, m_currentRobotMode.getName());
                    System.gc();
                    
                    try{
                        m_currentRobotMode.modeInit();
                    }catch(Throwable e){
                        Logger.get(m_currentRobotMode).fatal("Fatal error in RobotMode init method", e);
                    }
                }
                
                if(ds.isNewControlData()){
                    m_currentRobotMode.modeRun();
                }
            }
        }catch(Throwable t){
            log.fatal("Error in Main Loop. Something should have caught this!!!", t);
        }finally{
            log.fatal("ROBOTS DON'T QUIT!!!", "Exited Main Loop");
            System.exit(1);
        }
    }
    
    /**
     * Check version file.
     *
     * @param file the file
     */
    private static final void checkVersionFile(File file){
        
        if(!file.exists()){
            writeVersionFile(file);
        }else{
            byte[] data = FRC_JAVA_VERSION.getBytes();
            try{
                FileInputStream input = new FileInputStream(file);
                input.read(data);
                input.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            if(!data.equals(FRC_JAVA_VERSION.getBytes())){
                file.delete();
                writeVersionFile(file);
            }
        }   
    }
    
    /**
     * Write version file.
     *
     * @param file the file
     */
    private static final void writeVersionFile(File file){
        FileOutputStream output = null;
        try {
            file.createNewFile();
            output = new FileOutputStream(file);
            output.write(FRC_JAVA_VERSION.getBytes());

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
    
    public static final void registerAutonomous(AutonMode aMode, String name){
        
    }
    
    /**
     * Add the a {@link RobotMode} to the ModeSwitcher this will overwrite any previous
     * mode that has the same {@link GameMode}.
     * @param gMode the {@link GameMode} that this RobotMode will be run at
     * @param rMode the {@link RobotMode} to add
     * @see GameMode
     * @see RobotMode
     */
    protected static final void set(GameMode gMode, RobotMode rMode){
        if(!getGameMode().equals(gMode)){
            MODES_MAP.put(gMode, rMode);
        }
    }
    
    /**
     * Gets the current {@link RobotMode}.
     * 
     * If there is no current Mode, then something went wrong,
     * and an error mode is returned that will cause the robot to quit.
     * 
     * @return the current {@link RobotMode}
     */
    public static final RobotMode getCurrentRobotMode(){
        return m_currentRobotMode;
    }
    
    /**
     * Gets the current {@link GameMode}.
     * 
     * @return the current {@link GameMode} 
     */
    public static final GameMode getGameMode(){
        return m_currentMode;
    }
    
    /**
     * Get the {@link RobotMode} for the given {@link GameMode}.
     *
     * @param mode the mode
     * @return {@link RobotMode}
     */
    public static final RobotMode getRobotMode(GameMode mode){
        if(!MODES_MAP.containsKey(mode))
            return new RobotMode(){};

        return MODES_MAP.get(mode);
    }

    /**
     * Do we have a {@link RobotMode} for this {@link GameMode}.
     *
     * @param mode the mode
     * @return boolean do we have it?
     */
    public static final boolean hasMode(GameMode mode){
        return MODES_MAP.containsKey(mode);
    }
    
    /**
     * An approxamation of the time left in the current period
     * 
     * @return Time remaining in current match period (auto or teleop) in seconds 
     */
    public static final double getMatchTime() {
        return NetworkCommunications.HALGetMatchTime();
    }
    
    public static final Alliance getAlliance(){
        int sID = NetworkCommunications.HALGetAllianceStation();
        if(!MathUtils.inBounds(sID, 0, 5)){
            return Alliance.NONE;
        }
        return Alliance.values()[sID / 3];
    }
    
    public static final StationID getStation(){
        int sID = NetworkCommunications.HALGetAllianceStation();
        if(!MathUtils.inBounds(sID, 0, 5)){
            return StationID.NONE;
        }
        return StationID.values()[sID];
    }

    /**
     * Checks if is simulation.
     *
     * @return If the robot is running in simulation.
     */
    public static final boolean isSimulation() {
        return false;
    }

    /**
     * Checks if is real.
     *
     * @return If the robot is running in the real world.
     */
    public static final boolean isReal() {
        return true;
    }

    /**
     * Determine if the Robot is currently disabled.
     *
     * @return True if the Robot is currently disabled by the field controls.
     */
    public static final boolean isDisabled() {
        return DriverStation.isDisabled();
    }

    /**
     * Determine if the Robot is currently enabled.
     *
     * @return True if the Robot is currently enabled by the field controls.
     */
    public static final boolean isEnabled() {
        return DriverStation.isEnabled();
    }

    /**
     * Determine if the robot is currently in Autonomous mode.
     *
     * @return True if the robot is currently operating Autonomously as
     * determined by the field controls.
     */
    public static final boolean isAutonomous() {
        return DriverStation.isAutonomous();
    }

    /**
     * Determine if the robot is currently in Test mode.
     *
     * @return True if the robot is currently operating in Test mode as
     * determined by the driver station.
     */
    public static final boolean isTest() {
        return DriverStation.isTest();
    }
    
    /**
     * Determine if the robot is currently Emergency stopped.
     * 
     * @return True if the robot is currently emergency stopped.
     */
    public static final boolean isEStopped(){
        return DriverStation.isEStopped();
    }

    /**
     * Determine if the robot is currently in Operator Control mode.
     *
     * @return True if the robot is currently operating in Tele-Op mode as
     * determined by the field controls.
     */
    public static final boolean isOperatorControl() {
        return DriverStation.isOperatorControl();
    }
    
    /**
     * Check on the overall status of the system.
     *
     * @return Is the system active (i.e. PWM motor outputs, etc. enabled)?
     */
    public static final boolean isSysActive() {
        return DriverStation.isSysActive();
    }
    
    /**
     * Check if the system is browned out.
     * 
     * @return True if the system is browned out
     */
    public static final boolean isBrownedOut() {
        return DriverStation.isBrownedOut();
    }
    
    /**
     * Enum representation of Alliance
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum Alliance {
        RED,
        BLUE,
        NONE;
    }
    
    /**
     * Enum representation of alliance station location
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum StationID {
        RED1,
        RED2,
        RED3,
        BLUE1,
        BLUE2,
        BLUE3,
        NONE;
    }

}
