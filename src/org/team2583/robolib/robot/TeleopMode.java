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

/**
 * The Class TeleopMode.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public abstract class TeleopMode extends RobotMode {
    
    /**
     * Constructor for a Tele-Operative Robot mode.
     */
    protected TeleopMode(){
        super(GameMode.TELEOP);
    }
    
    /**
     * Constructor for a Tele-Operative Robot mode.
     *
     * @param name The name for this Teleop mode
     */
    protected TeleopMode(String name){
        super(GameMode.TELEOP, name);
    }
    
    /**
     * Constructor for a Tele-Operative Robot mode.
     *
     * @param name The name for this Teleop mode
     * @param active Set this mode as the active mode by default
     */
    protected TeleopMode(String name, boolean active){
        super(GameMode.TELEOP, name, active);
    }
    
    /**
     * {@inheritDoc}
     */
    protected final void modeRun(){
        FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramTeleop();;
        run();
    }
}