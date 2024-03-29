/*
 * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
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
 * A command that runs continuously, until canceled.
 *
 * @author noriah <vix@noriah.dev>
 */
public abstract class ContinuousCommand extends Command {

    public ContinuousCommand() {
        super();
    }

    public ContinuousCommand(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFinished() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void end() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void interrupted() {
    }

}
