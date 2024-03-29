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

package io.github.robolib.modes;

import io.github.robolib.RobotMode;
import io.github.robolib.command.Scheduler;
import io.github.robolib.jni.NetworkCommunications;

/**
 * The Class AutonMode.
 *
 * @author noriah <vix@noriah.dev>
 */
public abstract class AutonMode extends RobotMode {

    /**
     * Constructor for a Autonomous Robot mode.
     */
    protected AutonMode() {
        super(GameMode.AUTON);
    }

    /**
     * Constructor for a Autonomous Robot mode.
     *
     * @param name The name for this Auton mode
     */
    protected AutonMode(String name) {
        super(GameMode.AUTON, name);
    }

    /**
     * Constructor for a Autonomous Robot mode.
     *
     * @param name   The name for this Auton mode
     * @param active Set this mode as the active mode by default
     */
    protected AutonMode(String name, boolean active) {
        super(GameMode.AUTON, name, active);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void modeRun() {
        NetworkCommunications.ObserveUserProgramAutonomous();
        run();
        Scheduler.run();
    }
}
