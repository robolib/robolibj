package org.warriors2583.lib.limitswitch;

import org.warriors2583.lib.limitswitch.LimitSwitch.SwitchType;

/**
 * A Limit Switch System using two Switches
 * @author Austin Reuland
 */
public class DualLimitSwitchSystem implements ILimitSwitchSystem{
    
    private LimitSwitch m_topSwitch, m_bottomSwitch;

    public DualLimitSwitchSystem(int topChannel, SwitchType topType, int bottomChannel, SwitchType bottomType){
        this(new LimitSwitch(topChannel, topType), new LimitSwitch(bottomChannel, bottomType));
    }
    
    public DualLimitSwitchSystem(int topModule, int topChannel, SwitchType topType,
            int bottomModule, int bottomChannel, SwitchType bottomType){
        this(new LimitSwitch(topModule, topChannel, topType),
                new LimitSwitch(bottomModule, bottomChannel, bottomType));
    }
    
    public DualLimitSwitchSystem(LimitSwitch top, LimitSwitch bottom){
        m_topSwitch = top;
        m_bottomSwitch = bottom;
    }
    
    public boolean canUp() {
        return !m_topSwitch.state();
    }

    public boolean canDown() {
        return !m_bottomSwitch.state();
    }

}