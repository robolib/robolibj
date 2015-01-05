/*
 * Copyright (c) 2014 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;

import org.team2583.robolib.util.log.Logger;

/**
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public abstract class RobotMode {
    
    private final GameMode m_mode;
    private final String m_name;
    
    protected RobotMode(){
        this(GameMode.NONE);
    }
    
    protected RobotMode(GameMode mode){
        this(mode, mode.getName(), false);
    }
    
    protected RobotMode(GameMode mode, String name){
        this(mode, mode.getAbbreviation() + ":" + name, false);
    }
    
    protected RobotMode(GameMode mode, String name, boolean active){
        m_mode = mode;
        m_name = name;
        if(active || !ModeSwitcher.getInstance().hasMode(mode))
            setActive();
    }
    
    public final String getName(){
        return m_name;
    }
    
    /**
     * Get the mode type for this robot mode
     * 
     * @return GameMode type
     */
    public GameMode getModeType(){
        return m_mode;
    }
    
    /**
     * Get if this RobotMode is the active one for its {@link GameMode}
     * 
     * @return is this the active RobotMode for its {@link GameMode}
     */
    public final boolean getActive(){
        return ModeSwitcher.getInstance().getRobotMode(m_mode).equals(this);
    }
    
    /**
     * Set this RobotMode as the active robot mode for its {@link GameMode}.
     */
    public final void setActive(){
        ModeSwitcher.getInstance().set(m_mode, this);
    }
    
    /**
     * Mode Specific init code.
     * 
     * This calls the {@code init()} method when the mode changes.
     */
    protected void modeInit(){
        init();
    }
    
    /**
     * The user initialization code for this mode.
     * 
     * This is called every time the mode changes.
     * It is called before anything in {@code run()} is run.
     */
    public void init(){
        Logger.get(this).debug("Default init() Method... Overload me!");
    }
    
    /**
     * Mode Specific run code.
     * 
     * This calls the {@code run()} method once every period.
     */
    protected void modeRun(){
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramDisabled();
        run();
    }
    
    /**
     * The user run code for this mode.
     * 
     * This is called every period.
     * Every time the robot runs through a loop, this is called.
     */
    public void run(){}
    
    /**
     * Mode Specific end code.
     * 
     * This calls the {@code end()} method when the mode changes.
     */
    protected void modeEnd(){
        end();
    }
    
    /**
     * The user end code for this mode.
     * 
     * This is called every time the mode changes.
     * It is called before the mode changes.
     * 
     * Overload this to write your own end method.
     */
    public void end(){
        Logger.get(this).debug("Default end() Method... Overload me!");
    }
    
}