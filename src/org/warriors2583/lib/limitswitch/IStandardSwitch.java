package org.warriors2583.lib.limitswitch;

import org.warriors2583.lib.limitswitch.LimitSwitch.SwitchType;

/**
 * Interface for a Limit Switch
 * @author Austin Reuland
 */
public interface IStandardSwitch {
    
    public boolean state();
    
    public int getChannel();
    
    public SwitchType getType();
}
