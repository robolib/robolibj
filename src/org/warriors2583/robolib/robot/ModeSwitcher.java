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
import java.util.HashMap;
import java.util.Map;
import org.warriors2583.robolib.Robot;

/**
 * Handles the Switching of the Game mode and the Execution of the mode.
 * @author Austin Reuland
 */
public class ModeSwitcher {
    
    public static class GameMode{
        
        private final int value;
        private final String name;
        private GameMode(int value, String name){
            this.value = value;
            this.name = name;
        }
        public int getValue(){ return value; }
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
    
    private GameMode currentMode;
    
    private final Map m_modes = new HashMap();
    private final Scheduler m_scheduler;
    
    private static final ModeSwitcher m_instance = new ModeSwitcher();
    
    public static ModeSwitcher getInstance(){
        return m_instance;
    }
    
    private ModeSwitcher(){
        currentMode = GameMode.kNoMode;
        m_scheduler = Scheduler.getInstance();
    }
    
    public void add(GameMode mType, RobotMode rMode){
        m_modes.put(rMode, mType);
    }
    
    public void init(){
        if(!m_modes.containsKey(GameMode.kDisabled)){
            Robot.debug("No Disabled Robot Mode Defined");
            Robot.debug("Creating Default Disabled Mode");
            new DisabledMode(){
                public void init(){}
                public void run(){ Timer.delay(0.001); }
                public void end(){}  
            };
        }
        
        if(!m_modes.containsKey(GameMode.kTest)){
            Robot.debug("No Test Robot Mode Defined");
            Robot.debug("Creating Default Test Mode");
            new TestMode(){
                public void init(){}
                public void run(){ Timer.delay(0.001); }
                public void end(){}  
            };
        }
        
        if(!m_modes.containsKey(GameMode.kAuton)){
            Robot.debug("No Autonomous Robot Mode Defined");
            Robot.debug("Creating Default Autonomous Mode");
            new AutonMode(){
                public void init(){}
                public void run(){ Timer.delay(0.001); }
                public void end(){}  
            }; 
        }
        
        if(!m_modes.containsKey(GameMode.kTeleop)){
            Robot.debug("No Teleop Robot Mode Defined");
            Robot.debug("Creating Default Teleop Mode");
            new TeleopMode(){
                public void init(){}
                public void run(){ Timer.delay(0.001); }
                public void end(){}  
            };
        }        
    }
    
    public void run(){
        getRobotMode()._run();
        m_scheduler.run();
    }
    
    public GameMode getGameMode(){
        return currentMode;
    }
    
    public RobotMode getRobotMode(){
        if(m_modes.size() <= 0){
            return new RobotMode(){
                public void init(){
                    Robot.error("No Modes Defined. Somthing Messed Up");
                }
                public void run(){}
                public void end(){}    
            };
        }
        return (RobotMode) m_modes.get(currentMode);
    }
    
    public void switchMode(int value){
        switchMode(GameMode.kModes[value]);
    }
    
    public void switchMode(GameMode mode){
        getRobotMode()._end();
        currentMode = mode;
        Robot.getRobotTable().putNumber(NETTABLE_CURRENT_MODE, mode.getValue());
        Robot.getRobotTable().putString(NETTABLE_CURRENT_MODE_STRING, mode.getName());
        System.gc();
        getRobotMode()._init();
    }
    
    public boolean inNewMode(int mode){
        return currentMode.getValue() != mode;
    }
    
    public boolean inNewMode(GameMode mode){
        return currentMode != mode;
    }
}