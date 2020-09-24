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

package io.github.robolib;

import io.github.robolib.modes.AutonMode;
import io.github.robolib.modes.DisabledMode;
import io.github.robolib.modes.TeleopMode;
import io.github.robolib.modes.TestMode;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public abstract class SimpleRobot extends RoboLib {

    protected SimpleRobot(){
        super();
        initModes();
    }

    protected SimpleRobot(String name){
        super(name);
        initModes();
    }

    protected SimpleRobot(String name, String version){
        super(name, version);
        initModes();
    }

    private final void initModes(){
        new DisabledMode(){
            protected void init(){ disabledInit(); }
            protected void run(){ disabledPeriodic(); }
            protected void end(){ disabledEnd(); }
        };

        new TestMode(){
            protected void init(){ testInit(); }
            protected void run(){ testPeriodic(); }
            protected void end(){ testEnd(); }
        };

        new AutonMode(){
            protected void init(){ autonomousInit(); }
            protected void run(){ autonomousPeriodic(); }
            protected void end(){ autonomousEnd(); }
        };

        new TeleopMode(){
            protected void init(){ teleopInit(); }
            protected void run(){ teleopPeriodic(); }
            protected void end(){ teleopEnd(); }
        };
    }

    /**
     * Run every time the robot enter disabled mode.
     */
    protected abstract void disabledInit();

    /**
     * Run every 20ms during disabled period.
     */
    protected abstract void disabledPeriodic();

    /**
     * Run every time the robot leaves disabled mode.
     */
    protected abstract void disabledEnd();

    /**
     * Run every time the robot enter test mode.
     */
    protected abstract void testInit();

    /**
     * Run every 20ms during test period.
     */
    protected abstract void testPeriodic();

    /**
     * Run every time the robot leaves test mode.
     */
    protected abstract void testEnd();

    /**
     * Run every time the robot enter autonomous mode.
     */
    protected abstract void autonomousInit();

    /**
     * Run every 20ms during autonomous period.
     */
    protected abstract void autonomousPeriodic();

    /**
     * Run every time the robot leaves autonomous mode.
     */
    protected abstract void autonomousEnd();

    /**
     * Run every time the robot enter teleop mode.
     */
    protected abstract void teleopInit();

    /**
     * Run every 20ms during teleop period.
     */
    protected abstract void teleopPeriodic();

    /**
     * Run every time the robot leaves teleop mode.
     */
    protected abstract void teleopEnd();
}
