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

/**
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 *
 */
public class Serial extends Interface {

    public enum Port {
        kOnboard,
        kMXP,
        kUSB;
    };

    /**
     * @param port 
     */
    public Serial(Port port) {
        super(InterfaceType.SERIAL);
        if(port.equals(Port.kMXP)){
            allocateMXPPin(14);
            allocateMXPPin(10);
        }
        UsageReporting.report(UsageReporting.kResourceType_SerialPort, port.ordinal());
    }

}
