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

import io.github.robolib.RoboLib;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public final class WaitUntilCommand extends Command {

    private final double m_time;

    public WaitUntilCommand(double time) {
        this("WaitUntil " + time, time);
    }

    public WaitUntilCommand(String name, double time) {
        super(name);
        m_time = time;
    }

    public void initialize() {
    }

    public void execute() {
    }

    public boolean isFinished() {
        return RoboLib.getMatchTime() >= m_time;
    }

    public void end() {
    }

    public void interrupted() {
    }

}
