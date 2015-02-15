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

package io.github.robolib.module.sensor;

import io.github.robolib.module.iface.DigitalInput;

/**
 * Limit Switch Class.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitSwitch extends DigitalInput {
    
    /**
     * The type of switch this is
     * Normally open is a switch that is normally false when not pushed
     * Normally closed is a switch that is normally true when not pushed
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum SwitchType {
        
        /** Normally Open Switch. */
        OPEN(true),
        
        /** Normally Closed Switch. */
        CLOSED(false);
        
        /** The open. */
        public final boolean value;
        
        /**
         * Instantiates a new e switch type.
         *
         * @param open the open
         */
        SwitchType(boolean open){
            value = open;
        }
    }
    
    /** The m_type. */
    private SwitchType m_type;
    
    public LimitSwitch(DigitalChannel channel){
        this(channel, SwitchType.OPEN);
    }
    
    /**
     * Instantiates a new limit switch.
     *
     * @param channel the channel
     * @param type the type
     */
    public LimitSwitch(DigitalChannel channel, SwitchType type){
        super(channel);
        this.m_type = type;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getState() {
        return m_type.value == super.getState();
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public SwitchType getType() {
        return m_type;
    }
}
