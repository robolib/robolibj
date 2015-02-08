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

import io.github.robolib.util.log.Logger;

/**
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PrintCommand extends Command{
    
    private final String m_message;
    
    public PrintCommand(String message){
        super("Print(\"" + message + "\"");
        m_message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize() {
        Logger.get(Command.class).info(m_message);
    }

    protected void execute() {}
    protected boolean isFinished() {return true;}
    protected void end() {}
    protected void interrupted() {}

}
