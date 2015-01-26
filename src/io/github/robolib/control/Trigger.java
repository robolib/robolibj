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

package io.github.robolib.control;

import io.github.robolib.command.Command;
import io.github.robolib.command.Scheduler;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface Trigger {
    
    public abstract class ButtonScheduler{
        
        protected ButtonScheduler(){
            Scheduler.getInstance().addButton(this);
        }
        public abstract void execute();
    }

    public boolean get();
    
    public default void runWhenActive(final Command command){
        new ButtonScheduler(){
            boolean pressed = false;
            public void execute() {
                if(get()){
                    if(!pressed){
                        pressed = true;
                        command.start();
                    }
                }else{
                    pressed = false;
                }
            }
        };
    }
    
    public default void runWhileActive(final Command command){
        new ButtonScheduler(){
            boolean pressed = false;
            public void execute() {
                if(get()){
                    command.start();
                    pressed = true;
                }else if(pressed){
                    command.cancel();
                    pressed = false;
                }
            }
        };
    }
    
    public default void runWhenInactive(final Command command){
        new ButtonScheduler(){
            boolean pressed = false;
            public void execute() {
                if(get()){
                    pressed = true;
                }else if(pressed){
                    command.start();
                    pressed = false;
                }
            }
        };
    }
    
    public default void runWhileInactive(final Command command){
        new ButtonScheduler(){
            boolean pressed = false;
            public void execute() {
                if(get()){
                    if(!pressed){
                        command.cancel();
                        pressed = true;
                    }
                }else{
                    command.start();
                    pressed = false;
                }
            }
        };
    }
    
    public default void toggleWhenActive(final Command command){
        new ButtonScheduler(){
            boolean pressed = false;
            public void execute() {
                if(get()){
                    if(!pressed){
                        if(command.isRunning())
                            command.cancel();
                        else
                            command.start();
                    }
                }else{
                    pressed = false;
                }
            }
        };
    }
    
    public default void cancelWhenActive(final Command command){
        new ButtonScheduler(){
            boolean pressed = false;
            public void execute() {
                if(get()){
                    if(!pressed){
                        command.cancel();
                        pressed = true;
                    }
                }else{
                    pressed = false;
                }
            }
        };
    }
    
}
