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

package io.github.robolib.command;

/**
 * A {@link StartCommand} will call the {@link Command#start() start()}
 * method of another command when it is initialized and will finish immediately.
 * 
 * This is analogous to forking
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class StartCommand extends Command {
    
    private final Command m_command;
    
    /**
     * Instantiates a {@link StartCommand} which will start the
     * given command whenever its {@link Command#initialize() initialize()} is called.
     * @param commandToStart the {@link Command} to start
     */
    public StartCommand(Command command){
        m_command = command;
    }
    
    protected void initialize() {m_command.start();}
    protected void execute() {}
    protected boolean isFinished() { return true; }
    protected void end() {}
    protected void interrupted() {}

}
