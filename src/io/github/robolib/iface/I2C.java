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

package io.github.robolib.iface;

import io.github.robolib.communication.UsageReporting;


/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public class I2C extends Interface {

    public static enum Port {
        kOnboard,
        kMXP;
    };
    
    /**
     * @param port
     * @param address
     */
    protected I2C(Port port, int address) {
        super(InterfaceType.I2C);
        if(port.equals(Port.kMXP)){
            allocateMXPPin(32);
            allocateMXPPin(34);
        }
        UsageReporting.report(UsageReporting.kResourceType_I2C, address);
    }

}
