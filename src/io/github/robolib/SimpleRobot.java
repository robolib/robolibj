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

package io.github.robolib;

import io.github.robolib.modes.AutonMode;
import io.github.robolib.modes.DisabledMode;
import io.github.robolib.modes.GameMode;
import io.github.robolib.modes.TeleopMode;
import io.github.robolib.modes.TestMode;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
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

    private void initModes(){
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
    
    protected abstract void disabledInit();

    protected abstract void disabledPeriodic();

    protected abstract void disabledEnd();

    protected abstract void testInit();

    protected abstract void testPeriodic();

    protected abstract void testEnd();

    protected abstract void autonomousInit();

    protected abstract void autonomousPeriodic();

    protected abstract void autonomousEnd();

    protected abstract void teleopInit();

    protected abstract void teleopPeriodic();

    protected abstract void teleopEnd();
}
