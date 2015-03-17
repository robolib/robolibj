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

package io.github.robolib.modes;

import io.github.robolib.RobotMode;
import io.github.robolib.command.Scheduler;
import io.github.robolib.jni.NetworkCommunications;

/**
 * The Class TeleopMode.
 *
 * @author noriah Reuland <vix@noriah.dev>
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
    @Override
    protected final void modeRun(){
        NetworkCommunications.ObserveUserProgramTeleop();
        run();
        Scheduler.run();
    }
}