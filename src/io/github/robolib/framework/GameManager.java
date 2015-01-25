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

package io.github.robolib.framework;

import java.util.EnumMap;

import io.github.robolib.command.Scheduler;
import io.github.robolib.communication.NetworkCommunications;
import io.github.robolib.exception.RobotException;
import io.github.robolib.robot.AutonMode;
import io.github.robolib.robot.DisabledMode;
import io.github.robolib.robot.GameMode;
import io.github.robolib.robot.TeleopMode;
import io.github.robolib.robot.TestMode;
import io.github.robolib.util.MathUtils;
import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

/**
 * Handles the Switching of the Game mode and the Execution of the mode.
 * This controls what GameMode the robot should run.
 * 
 * @author noriah Reuland <vix@noriah.dev>
 * @since 2.0.1
 */
public class GameManager {
    
    
    public static enum Alliance {
        RED,
        BLUE,
        NONE;
    }
    
    public static enum StationID {
        RED1,
        RED2,
        RED3,
        BLUE1,
        BLUE2,
        BLUE3,
        NONE;
    }
    
    /** The Constant NETTABLE_CURRENT_MODE. */
    private static final String NETTABLE_CURRENT_MODE = "mode";
    
    /** The Constant NETTABLE_CURRENT_MODE_STRING. */
    private static final String NETTABLE_CURRENT_MODE_STRING = "mode-string";
    
    /** The m_current mode. */
    private GameMode m_currentMode;
    
    /** The m_modes. */
    private final EnumMap<GameMode, RobotMode> m_modes;
    //private final RobotMode m_modes[];
    /** The m_scheduler. */
//    private final Scheduler m_scheduler;
    
    /** The Constant m_instance. */
    private static GameManager m_instance = null;
    
    private final ILogger m_log;
    
    private Thread m_thread;
//    private final Object m_dataSem;
    private volatile boolean m_thread_keepAlive;
//    private volatile boolean m_thread_exit_error;
    
    /**
     * Returns the ModeSwitcher, creating it if one does not exists.
     * @return the ModeSwitcher
     */
    public static GameManager getInstance(){
        return m_instance == null ? m_instance = new GameManager() : m_instance;
    }
    
    /**
     * Instantiates a new mode switcher.
     */
    private GameManager(){
//        m_dataSem = new Object();
        m_currentMode = GameMode.NONE;
        m_modes = new EnumMap<GameMode, RobotMode>(GameMode.class);
        m_log = Logger.get(RoboLibBot.class);
        
        m_thread = new Thread(() -> task(), "GameManager");
        m_thread.setPriority(((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2) + 1);
        
    }
    
    /**
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
     */
    private void task(){
        DriverStation ds = DriverStation.getInstance();
        RobotMode rMode;
        GameMode gMode;
        try{
            while(m_thread_keepAlive){
                rMode = getRobotMode();
                gMode = DriverStation.getGameMode();
                
                if(m_currentMode != gMode){
                    m_log.info("Switching to " + gMode.getName());

                    try{
                        rMode.modeEnd();
                    }catch(Throwable e){
                        Logger.get(rMode).fatal("Fatal error in RobotMode end method", e);
                    }
                    
                    m_currentMode = gMode;
                    rMode = getRobotMode();
                    RoboLibBot.getRobotTable().putNumber(NETTABLE_CURRENT_MODE, gMode.ordinal());
                    RoboLibBot.getRobotTable().putString(NETTABLE_CURRENT_MODE_STRING, rMode.getName());
                    System.gc();
                    
                    try{
                        rMode.modeInit();
                    }catch(Throwable e){
                        Logger.get(rMode).fatal("Fatal error in RobotMode init method", e);
                    }
                }
                
                if(ds.isNewControlData()){
                    rMode.run();
                    Scheduler.getInstance().run();
                }
                ds.waitForData();
            }
        }catch(Throwable t){
            m_log.fatal("Error in Main Loop. Something should have caught this!!!", t);
        }finally{
            m_log.fatal("ROBOTS DON'T QUIT!!!", "Exited Main Loop");
            RoboLibBot.wake();
        }
    }
    
    protected void startThread(){
        if(!m_thread.isAlive()){
            m_thread_keepAlive = true;
//            m_thread_exit_error = true;
            m_thread.start();
        }
    }
    
    public void exitNoError(){
        m_thread_keepAlive = false;
//        m_thread_exit_error = false;
    }
    
    public void exit(){
        m_thread_keepAlive = false;
    }
    
    /**
     * Debug.
     *
     * @param msg the msg
     */
    private void debug(String msg){
        m_log.debug(msg);
    }
    
    /**
     * Add the a {@link RobotMode} to the ModeSwitcher this will overwrite any previous
     * mode that has the same {@link GameMode}.
     * @param gMode the {@link GameMode} that this RobotMode will be run at
     * @param rMode the {@link RobotMode} to add
     * @see GameMode
     * @see RobotMode
     */
    protected void set(GameMode gMode, RobotMode rMode){
        if(!getGameMode().equals(gMode)){
            m_modes.put(gMode, rMode);
        }
    }
    
    
public static void registerAutonomous(AutonMode aMode, String name){
        
    }
    
    /**
     * Initializes the Game Mode Switcher.
     * This method must be called when ready to start the robot.
     * 
     * Checks each Mode to see if there is code to run, if not, a Default class
     * will be added.
     *
     * @see DisabledMode
     * @see TestMode
     * @see AutonMode
     * @see TeleopMode
     */
    protected void init(){
        if(!m_modes.containsKey(GameMode.DISABLED)){
            debug("No Disabled Robot Mode Defined");
            debug("Creating Empty Disabled Mode");
            new DisabledMode(){};
        }
        
        if(!m_modes.containsKey(GameMode.TEST)){
            debug("No Test Robot Mode Defined");
            debug("Creating Empty Test Mode");
            new TestMode(){};
        }
        
        if(!m_modes.containsKey(GameMode.AUTON)){
            debug("No Autonomous Robot Mode Defined");
            debug("Creating Empty Autonomous Mode");
            new AutonMode(){};
        }
        
        if(!m_modes.containsKey(GameMode.TELEOP)){
            debug("No Teleop Robot Mode Defined");
            debug("Creating Empty Teleop Mode");
            new TeleopMode(){};
        }
        m_currentMode = GameMode.DISABLED;
        RoboLibBot.getRobotTable().putNumber(NETTABLE_CURRENT_MODE, m_currentMode.ordinal());
        RoboLibBot.getRobotTable().putString(NETTABLE_CURRENT_MODE_STRING, getRobotMode().getName());
    }
    
    /**
     * Gets the current {@link GameMode}.
     * 
     * @return the current {@link GameMode} 
     */
    public GameMode getGameMode(){
        return m_currentMode;
    }
    
    /**
     * Gets the current {@link RobotMode}.
     * 
     * If there is no current Mode, then something went wrong,
     * and an error mode is returned that will cause the robot to quit.
     * 
     * @return the current {@link RobotMode}
     */
    public RobotMode getRobotMode(){
        if(!m_modes.containsKey(m_currentMode)){
            return new RobotMode(){
                public void init(){
                    throw new RobotException("No Robot CounterMode");
                }
            };
        }
        return m_modes.get(m_currentMode);
    }
    
    /**
     * Get the {@link RobotMode} for the given {@link GameMode}.
     *
     * @param mode the mode
     * @return {@link RobotMode}
     */
    public RobotMode getRobotMode(GameMode mode){
        if(!m_modes.containsKey(mode))
            return new RobotMode(){};

        return m_modes.get(mode);
    }

    /**
     * Do we have a {@link RobotMode} for this {@link GameMode}.
     *
     * @param mode the mode
     * @return boolean do we have it?
     */
    public boolean hasMode(GameMode mode){
        return m_modes.containsKey(mode);
    }
    
    /**
     * An approxamation of the time left in the current period
     * 
     * @return Time remaining in current match period (auto or teleop) in seconds 
     */
    public static double getMatchTime() {
        return NetworkCommunications.HALGetMatchTime();
    }
    
    public static Alliance getAlliance(){
        int sID = NetworkCommunications.HALGetAllianceStation();
        if(!MathUtils.inBounds(sID, 0, 5)){
            return Alliance.NONE;
        }
        return Alliance.values()[sID / 3];
    }
    
    public static StationID getStation(){
        int sID = NetworkCommunications.HALGetAllianceStation();
        if(!MathUtils.inBounds(sID, 0, 5)){
            return StationID.NONE;
        }
        return StationID.values()[sID];
    }
}
