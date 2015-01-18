/*
 * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package io.robolib.framework;

import java.util.EnumMap;
//import edu.wpi.first.wpilibj.command.Scheduler;=


import io.robolib.exception.RobotException;
import io.robolib.robot.AutonMode;
import io.robolib.robot.DisabledMode;
import io.robolib.robot.GameMode;
import io.robolib.robot.TeleopMode;
import io.robolib.robot.TestMode;
import io.robolib.util.log.ILogger;
import io.robolib.util.log.Logger;

/**
 * Handles the Switching of the Game mode and the Execution of the mode.
 * This controls what Mode the robot should run.
 * It handles feeding the Watchdog and running the Scheduler.
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class ModeSwitcher {
    
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
    //private final Scheduler m_scheduler;
    
    /** The Constant m_instance. */
    private static ModeSwitcher m_instance = null;
    
    private final ILogger m_log;
    
    /**
     * Returns the ModeSwitcher, creating it if one does not exists.
     * @return the ModeSwitcher
     */
    public static ModeSwitcher getInstance(){
        return m_instance == null ? m_instance = new ModeSwitcher() : m_instance;
    }
    
    /**
     * Instantiates a new mode switcher.
     */
    private ModeSwitcher(){
        m_currentMode = GameMode.NONE;
        m_modes = new EnumMap<GameMode, RobotMode>(GameMode.class);
        m_log = Logger.get(RoboLibBot.class);
//        m_scheduler = Scheduler.getInstance();
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
     * Runs the Current RobotMode.
     * 
     * Catches any Throwable objects that may be thrown.
     * Any caught Throwable Object is treated as fatal and will kill the RoboLibBot.
     * They are treated as fatal because any uncaught Throwables can only be 
     * RuntimeExceptions or Errors, which are Fatal
     * 
     */
    protected void run(){
        //try{
            getRobotMode().modeRun();
        //}catch(Throwable e){
            //Logger.get(getRobotMode()).fatal("Fatal action in RobotMode run method", e);
        //}
//        m_scheduler.run();
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
                    throw new RobotException("No Robot Mode");
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
     * @return boolean - do we have it?
     */
    public boolean hasMode(GameMode mode){
        return m_modes.containsKey(mode);
    }
    
    /**
     * Switch the current {@link GameMode}.
     * 
     * This will first call the end() Method of the current {@link RobotMode},
     * then set the current {@link GameMode}, call the System Garbae Collector,
     * and then call the new RobotMode init() method.
     * 
     * @param mode the {@link GameMode} number to switch to.
     */
    public void switchMode(int mode){
        switchMode(GameMode.values()[mode]);
    }
    
    /**
     * Switch the current {@link GameMode}.
     * 
     * This will first call the end() Method of the current {@link RobotMode},
     * then set the current {@link GameMode}, call the System Garbace Collector,
     * and then call the new RobotMode init() method.
     * 
     * Catches any Throwable objects that may be thrown.
     * Any caught Throwable Object is treated as fatal and will kill the RoboLibBot.
     * They are treated as fatal because any uncaught Throwables can only be 
     * RuntimeExceptions or Errors, which are Fatal
     * 
     * @param mode the {@link GameMode} to switch to.
     */
    public void switchMode(GameMode mode){
        m_log.info("Switching to " + mode.getName());
        
        RobotMode rMode = getRobotMode();
        try{
            rMode.modeEnd();
        }catch(Throwable e){
            Logger.get(rMode).fatal("Fatal error in RobotMode end method", e);
        }
        m_currentMode = mode;
        rMode = getRobotMode();
        RoboLibBot.getRobotTable().putNumber(NETTABLE_CURRENT_MODE, mode.ordinal());
        RoboLibBot.getRobotTable().putString(NETTABLE_CURRENT_MODE_STRING, rMode.getName());
        System.gc();
        try{
            rMode.modeInit();
        }catch(Throwable e){
            Logger.get(rMode).fatal("Fatal error in RobotMode init method", e);
        }
    }
    
    /**
     * Gets whether the mode given is different to the current running mode.
     *
     * @param mode the mode number of the comparing {@link GameMode}
     * @return a boolean value, true if we are in a new mode or false if we are in the same mode
     */
    public boolean inNewMode(int mode){
        return m_currentMode.ordinal() != mode;
    }
    
    /**
     * Gets whether the mode given is different to the current running mode.
     *
     * @param mode the {@link GameMode} we are comparing
     * @return a boolean value, true if we are in a new mode or false if we are in the same mode
     */
    public boolean inNewMode(GameMode mode){
        return m_currentMode != mode;
    }
}