/*
1 * Copyright (c) 2015 noriah <vix@noriah.dev>.
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
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class WaitCommand extends Command {
    
    public WaitCommand(double timeout){
        this("Wait " + timeout, timeout);
    }
    
    public WaitCommand(String name, double timeout){
        super(name, timeout);
    }
    
    protected void initialize() {}

    protected void execute() {}

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {}

    protected void interrupted() {}

}
