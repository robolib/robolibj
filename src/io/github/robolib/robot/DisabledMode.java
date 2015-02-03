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

package io.github.robolib.robot;

import io.github.robolib.RobotMode;
import io.github.robolib.communication.NetworkCommunications;


/**
 * The Class DisabledMode.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class DisabledMode extends RobotMode {
    
    /**
     * Constructor for a Disable Robot mode.
     */
    protected DisabledMode(){
        super(GameMode.DISABLED);
    }
    
    /**
     * Constructor for a Disable Robot mode
     * Although, why would you be naming this,
     * You should really only have one.
     * @param name The name for this Disable mode
     */
    protected DisabledMode(String name){
        super(GameMode.DISABLED, name);
    }
    
    /**
     * Constructor for a Disable Robot mode
     * Although, why would you be naming this?
     * And why are you setting this as active?
     * Do you really have more than one disabled mode?.
     *
     * @param name The name for this Disable mode
     * @param active Set this mode as the active mode by default
     */
    protected DisabledMode(String name, boolean active){
        super(GameMode.DISABLED, name, active);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected final void modeRun(){
        NetworkCommunications.ObserveUserProgramDisabled();
        run();
    }
}