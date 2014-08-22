/*
 * Copyright (c) 2014 noriah <vix@noriah.dev>.
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

package org.wwr.robolib.robot;

import edu.wpi.first.wpilibj.communication.FRCControl;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class TestMode extends RobotMode {
    
    protected TestMode(){
        super(ModeSwitcher.GameMode.kTest);
    }
    
    /**
     * {@inheritDoc}
     */
    protected final void modeInit(){
        LiveWindow.setEnabled(true);
        init();
    }
    
    /**
     * {@inheritDoc}
     */
    protected final void modeRun(){
        FRCControl.observeUserProgramTest();
        run();
    }
    
    /**
     * {@inheritDoc}
     */
    protected final void modeEnd(){
        LiveWindow.setEnabled(false);
        end();
    }

}