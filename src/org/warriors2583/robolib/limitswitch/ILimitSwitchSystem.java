package org.warriors2583.robolib.limitswitch;

/**
 * The Limit Switch System Interface
 * @author Austin Reuland
 */
public interface ILimitSwitchSystem {
    
    /**
     * 
     * @return Can we go Up
     */
    public boolean canUp();
    
    /**
     * 
     * @return Can we go Down 
     */ 
    public boolean canDown();
    
}
