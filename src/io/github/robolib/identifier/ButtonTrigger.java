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

package io.github.robolib.identifier;

import io.github.robolib.command.Command;
import io.github.robolib.module.RoboRIO;

/**
 *
 * @author noriah <vix@noriah.dev>
 */
public interface ButtonTrigger extends Trigger {

    public default void runWhenPressed(final Command command){
        runWhenActive(command);
    }

    public default void runWhileHeld(final Command command){
        runWhileActive(command);
    }

    public default void runWhenReleased(final Command command){
        runWhenInactive(command);
    }

    public default void runWhileReleased(final Command command){
        runWhileInactive(command);
    }

    public default void togglenWhenPressed(final Command command){
        toggleWhenActive(command);
    }

    public default void cancelWhenPressed(final Command command){
        cancelWhenActive(command);
    }

    public default void runWhenDoublePressed(final Command command, final double timeout){
        new ButtonScheduler(){
            boolean pressed = false;
            boolean pressedFirst = false;
            double time = 0;
            @Override
            public void execute() {
                if(getState()){
                    if(!pressed){
                        if(!pressedFirst){
                            pressedFirst = true;
                            time = RoboRIO.getFPGATimestamp();
                        }else{
                            if(RoboRIO.getFPGATimestamp() - time <= timeout){
                                command.start();
                                pressedFirst = false;
                            }
                        }
                    }
                    pressed = true;
                }else{
                    pressed = false;
                    if(pressedFirst){
                        if(RoboRIO.getFPGATimestamp() - time > timeout){
                            pressedFirst = false;
                        }
                    }
                }
            }
        };
    }
}
