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

package org.team2583.robolib.robot;

import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The Class TestMode.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public abstract class TestMode extends RobotMode {
    
    /**
     * Constructor for a Test Robot mode.
     */
    protected TestMode(){
        super(GameMode.TEST);
    }
    
    /**
     * Constructor for a Test Robot mode.
     *
     * @param name The name for this Test mode
     */
    protected TestMode(String name){
        super(GameMode.TEST, name);
    }
    
    /**
     * Constructor for a Test Robot mode.
     *
     * @param name The name for this Test mode
     * @param active Set this mode as the active mode by default
     */
    protected TestMode(String name, boolean active){
        super(GameMode.TEST, name, active);
    }
    
    /**
     * {@inheritDoc}
     */
    protected final void modeInit(){
        LiveWindow.setEnabled(true);
        init();
    }
    
    /**
     * {@inheritDoc}
     */
    protected final void modeRun(){
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramTest();
        run();
    }
    
    /**
     * {@inheritDoc}
     */
    protected final void modeEnd(){
        LiveWindow.setEnabled(false);
        end();
    }

}