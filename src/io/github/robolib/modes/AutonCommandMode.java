/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

import io.github.robolib.command.Command;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class AutonCommandMode extends AutonMode {
    
    private Command m_command;
    
    /**
     * Constructor for a Autonomous Robot mode.
     */
    public AutonCommandMode(Command command) {
        super();
        m_command = command;
    }
    
    /**
     * Constructor for a Autonomous Robot mode.
     *
     * @param name The name for this Auton mode
     */
    public AutonCommandMode(Command command, String name){
        super(name);
        m_command = command;
    }
    
    /**
     * Constructor for a Autonomous Robot mode.
     *
     * @param name The name for this Auton mode
     * @param active Set this mode as the active mode by default
     */
    public AutonCommandMode(Command command, String name, boolean active){
        super(name, active);
        m_command = command;
    }
    
    @Override
    protected void init(){
        m_command.start();
    }
    
    @Override
    protected void end(){
        m_command.cancel();
    }
    
}
