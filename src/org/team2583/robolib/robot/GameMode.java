/*
 * Copyright (c) 2014 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package org.team2583.robolib.robot;

/**
 * @since 0.1.0
 * @author Austin Reuland <amreuland@gmail.com>
 */
public enum GameMode {
	
	
    NONE("NoMode", "N"),
    DISABLED("Disabled", "D"),
    TEST("Test", "T"),
    AUTON("Autonomous", "A"),
    TELEOP("Teleop", "U");
	
    private final String m_name;
    private final String m_abbr;
    
    private GameMode(String modeName, String abbr){
    	m_name = modeName;
    	m_abbr = abbr;
    }
    
    /**
     * Get the name of the GameMode
     * @return the GameMode Name
     */
    public String getName(){
    	return m_name;
    }
    
    /**
     * Get the abbreviation of the GameMode
     * @return the GameMode abbreviation
     */
    public String getAbbreviation(){
        return m_abbr;
    }
}
