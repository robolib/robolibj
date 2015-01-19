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

package io.github.robolib.input.limitswitch;

import io.github.robolib.iface.DigitalIO.DigitalChannel;
import io.github.robolib.iface.DigitalInput;

/**
 * Limit Switch Class.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitSwitch implements StandardSwitch {
    
    public enum SwitchType {
        
        /** Normally Open Switch. */
        NO(true),
        
        /** Normally Closed Switch. */
        NC(false);
        
        /** The open. */
        private final boolean open;
        
        /**
         * Instantiates a new e switch type.
         *
         * @param open the open
         */
        SwitchType(boolean open){
            this.open = open;
        }
        
        /**
         * Gets the value.
         *
         * @param value the value
         * @return the value
         */
        public boolean getValue(boolean value){
            return this.open && value;
        }
    }
    
    /** The m_limit switch. */
    private DigitalInput m_limitSwitch;
    
    /** The m_type. */
    private SwitchType m_type;
    
    /**
     * Instantiates a new limit switch.
     *
     * @param channel the channel
     * @param type the type
     */
    public LimitSwitch(DigitalChannel channel, SwitchType type){
        m_limitSwitch = new DigitalInput(channel);
        this.m_type = type;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean state() {
        return m_type.getValue(m_limitSwitch.get());
    }

    /**
     * {@inheritDoc}
     */
    public SwitchType getType() {
        return m_type;
    }
}
