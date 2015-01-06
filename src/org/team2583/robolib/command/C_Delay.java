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

package org.team2583.robolib.command;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Delays a Command Group.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class C_Delay extends Command {

    /**
     * Instantiates a new c_ delay.
     *
     * @param sec the sec
     */
    public C_Delay(double sec) {

        super("C_Delay");
        setTimeout(sec);
    }

    /**
     * {@inheritDoc}
     */
    protected void initialize() {
    }

    /**
     * {@inheritDoc}
     */
    protected void execute() {
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isFinished() {
        return isTimedOut();
    }

    /**
     * {@inheritDoc}
     */
    protected void end() {
    }

    /**
     * {@inheritDoc}
     */
    protected void interrupted() {
    }
}
