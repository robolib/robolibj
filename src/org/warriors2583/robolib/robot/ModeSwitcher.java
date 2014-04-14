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

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import java.util.Hashtable;
import org.warriors2583.robolib.robot.Robot.RobotException;
import org.warriors2583.robolib.util.Logger;

/**
 * Handles the Switching of the Game mode and the Execution of the mode.
 * This controls what Mode the robot should run.
 * It handles feeding the Watchdog and running the Scheduler.
 * 
 * @author Austin Reuland
 */
public class ModeSwitcher {
    
    /**
     * 
     */
    public static class GameMode{
        
        private final int value;
        private final String name;
        private GameMode(int value, String name){
            this.value = value;
            this.name = name;
        }
        /**
         * Get the value of the GameMode
         * @return the GameMode Value
         */
        public int getValue(){ return value; }
        /**
         * Get then name of the GameMode
         * @return the GameMode Name
         */
        public String getName(){ return name; }
        
        public static final GameMode kNoMode = new GameMode(0, "NoMode");
        public static final GameMode kDisabled = new GameMode(1, "Disabled");
        public static final GameMode kTest = new GameMode(2, "Test");
        public static final GameMode kAuton = new GameMode(3, "Autonomous");
        public static final GameMode kTeleop = new GameMode(4, "Teleop");
        
        public static final GameMode kModes[] = {kNoMode, kDisabled, kTest, kAuton, kTeleop};
        
    }
    
    private static final String NETTABLE_CURRENT_MODE = "mode";
    private static final String NETTABLE_CURRENT_MODE_STRING = "mode-string";
    
    private static GameMode m_currentMode;
    
    private boolean m_initialized;
    private final Hashtable m_modes = new Hashtable();
    private final Scheduler m_scheduler;
    
    private static final ModeSwitcher m_instance = new ModeSwitcher();
    
    public static ModeSwitcher getInstance(){
        return m_instance;
    }
    
    private ModeSwitcher(){
        m_initialized = false;
        m_currentMode = GameMode.kNoMode;
        m_scheduler = Scheduler.getInstance();
    }
    
    private void debug(String msg){
        Logger.get(this).info(msg);
    }
    
    /**
     * Add the a {@link RobotMode} to the ModeSwitcher this will overwrite any previous
     * mode that has the same {@link GameMode}.
     * @param gMode the {@link GameMode} that this RobotMode will be run at
     * @param rMode the {@link RobotMode} to add
     * @see GameMode
     * @see RobotMode
     */
    protected void add(GameMode gMode, RobotMode rMode){
        if(!m_initialized){
            m_modes.put(rMode, gMode);
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
        if(!m_modes.containsKey(GameMode.kDisabled)){
            debug("No Disabled Robot Mode Defined");
            debug("Creating Default Disabled Mode");
            new DisabledMode(){
                public void init(){}
                public void run(){ Timer.delay(0.001); }
                public void end(){}  
            };
        }
        
        if(!m_modes.containsKey(GameMode.kTest)){
            debug("No Test Robot Mode Defined");
            debug("Creating Default Test Mode");
            new TestMode(){
                public void init(){}
                public void run(){ Timer.delay(0.001); }
                public void end(){}  
            };
        }
        
        if(!m_modes.containsKey(GameMode.kAuton)){
            debug("No Autonomous Robot Mode Defined");
            debug("Creating Default Autonomous Mode");
            new AutonMode(){
                public void init(){}
                public void run(){ Timer.delay(0.001); }
                public void end(){}  
            }; 
        }
        
        if(!m_modes.containsKey(GameMode.kTeleop)){
            debug("No Teleop Robot Mode Defined");
            debug("Creating Default Teleop Mode");
            new TeleopMode(){
                public void init(){}
                public void run(){ Timer.delay(0.001); }
                public void end(){}  
            };
        }
        m_currentMode = GameMode.kDisabled;
        m_initialized = true;
    }
    
    /**
     * Runs the Current RobotMode.
     * 
     * Catches any Throwable objects that may be thrown.
     * Any caught Throwable Object is treated as fatal and will kill the Robot.
     * They are treated as fatal because any uncaught Throwables can only be 
     * RuntimeExceptions or Errors, which are Fatal
     * 
     */
    protected void run(){
        try{
            getRobotMode().modeRun();
        }catch(Throwable e){
            Logger.get(getRobotMode()).fatal("Fatal action in RobotMode run method", e);
        }
        m_scheduler.run();
    }
    
    /**
     * Gets the current {@link GameMode}.
     * 
     * @return the current {@link GameMode} 
     */
    public static GameMode getGameMode(){
        return m_currentMode;
    }
    
    public static String getGameModeName(){
        return getGameMode().getName();
    }
    
    /**
     * Gets the current {@link RobotMode}.
     * 
     * If there is no current Mode, then something went wrong,
     * and an error mode is returned that will cause the robot to quit.
     * 
     * 
     * @return the current {@link RobotMode}
     */
    public RobotMode getRobotMode(){
        if(m_modes.size() <= 0){
            return new RobotMode(){
                public void init(){
                    throw new RobotException("No Robot Mode");
                }
                public void run(){}
                public void end(){}    
            };
        }
        return (RobotMode) m_modes.get(m_currentMode);
    }
    
    /**
     * Switch the current {@link GameMode}.
     * 
     * This will first call the end() Method of the current {@link RobotMode},
     * then set the current {@link GameMode}, call the System Garbace Collector,
     * and then call the new RobotMode init() method.
     * 
     * @param mode the {@link GameMode} number to switch to.
     */
    public void switchMode(int mode){
        switchMode(GameMode.kModes[mode]);
    }
    
    /**
     * Switch the current {@link GameMode}.
     * 
     * This will first call the end() Method of the current {@link RobotMode},
     * then set the current {@link GameMode}, call the System Garbace Collector,
     * and then call the new RobotMode init() method.
     * 
     * Catches any Throwable objects that may be thrown.
     * Any caught Throwable Object is treated as fatal and will kill the Robot.
     * They are treated as fatal because any uncaught Throwables can only be 
     * RuntimeExceptions or Errors, which are Fatal
     * 
     * @param mode the {@link GameMode} to switch to.
     */
    public void switchMode(GameMode mode){
        try{
            getRobotMode().modeEnd();
        }catch(Throwable e){
            Logger.get(getRobotMode()).fatal("Fatal action in RobotMode end method", e);
        }
        m_currentMode = mode;
        Robot.getRobotTable().putNumber(NETTABLE_CURRENT_MODE, mode.getValue());
        Robot.getRobotTable().putString(NETTABLE_CURRENT_MODE_STRING, mode.getName());
        System.gc();
        try{
            getRobotMode().modeInit();
        }catch(Throwable e){
            Logger.get(getRobotMode()).fatal("Fatal action in RobotMode init method", e);
        }
    }
    
    /**
     * Gets whether the mode given is different to the current running mode
     * @param mode the mode number of the comparing {@link GameMode}
     * @return a boolean value, true if we are in a new mode or false if we are in the same mode
     */
    public boolean inNewMode(int mode){
        return m_currentMode.getValue() != mode;
    }
    
    /**
     * Gets whether the mode given is different to the current running mode
     * @param mode the {@link GameMode} we are comparing
     * @return a boolean value, true if we are in a new mode or false if we are in the same mode
     */
    public boolean inNewMode(GameMode mode){
        return m_currentMode != mode;
    }
}