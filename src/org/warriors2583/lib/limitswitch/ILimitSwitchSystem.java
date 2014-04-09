package org.warriors2583.lib.limitswitch;

/**
 * The Limit Switch System Interface
 * @author noriah Reuland
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
