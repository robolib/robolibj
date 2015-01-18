/*
 * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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
import io.github.robolib.communication.FRCNetworkCommunicationsLibrary.tResourceType;

/**
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 *
 */
public class SPI extends Interface {

    
    public static enum Port {
        kOnboardCS0,
        kOnboardCS1,
        kOnboardCS2,
        kOnboardCS3,
        kMXP;
    };
    
    /**
     * @param port 
     */
    public SPI(Port port) {
        super(InterfaceType.SPI);
        if(port.equals(Port.kMXP)){
            allocateMXPPin(19);
            allocateMXPPin(21);
            allocateMXPPin(23);
            allocateMXPPin(25);
        }
        UsageReporting.report(tResourceType.kResourceType_SPI, port.ordinal());
    }

}