package org.team2583.robolib.robot;

public enum GameMode {
	
	
    NONE("NoMode"),
    DISABLED("Disabled"),
    TEST("Test"),
    AUTON("Autonomous"),
    TELEOP("Teleop");
	
    private final String m_name;
    
    private GameMode(String modeName){
    	m_name = modeName;
    }
    
    public String getName(){
    	return m_name;
    }
}
