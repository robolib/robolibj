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

import edu.wpi.first.wpilibj.communication.FRCControl;
import org.team2583.robolib.robot.ModeSwitcher.GameMode;
import org.team2583.robolib.util.Logger;

/**
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public abstract class RobotMode {
    
    protected RobotMode(){
        this(GameMode.kNoMode);
    }
    
    protected RobotMode(GameMode mode){
        ModeSwitcher.getInstance().add(mode, this);
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
        FRCControl.observeUserProgramDisabled();
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