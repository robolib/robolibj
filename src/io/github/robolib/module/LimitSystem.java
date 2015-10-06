/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

package io.github.robolib.module;

import io.github.robolib.identifier.BooleanSource;


/**
 * The Limit Switch System Interface.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitSystem {

    /**
     * Type of limit system
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum SystemType {
        /** System limited at a front limit. */
        SINGLE_FORWARD,
        FORWARD,
        /** System limited at a back limit. */
        SINGLE_REVERSE,
        REVERSE,
        /** System limited by back and front limits. */
        DUAL;
    }

    private SystemType m_systemType;

    private BooleanSource m_frontLimit;

    private BooleanSource m_backLimit;

    public LimitSystem(BooleanSource limit, SystemType type){
        switch(type){
        case FORWARD:
        case SINGLE_FORWARD:
            m_frontLimit = limit;
        case REVERSE:
        case SINGLE_REVERSE:
            m_backLimit = limit;
        case DUAL:
            throw new IllegalArgumentException("Wrong constructor for a Dual Limit System.");
        }
        m_systemType = type;
    }

    public LimitSystem(BooleanSource frontLimit, BooleanSource backLimit){
        m_frontLimit = frontLimit;
        m_backLimit = backLimit;
        m_systemType = SystemType.DUAL;
    }

    /**
     * Can up.
     *
     * @return Can we go Up
     */
    public boolean canForward(){
        if(m_systemType == SystemType.SINGLE_REVERSE) return true;
        return !m_frontLimit.getState();
    }
    
    /**
     * Can down.
     *
     * @return Can we go Down
     */ 
    public boolean canReverse(){
        if(m_systemType == SystemType.SINGLE_FORWARD) return true;
        return !m_backLimit.getState();
    }
    
}
