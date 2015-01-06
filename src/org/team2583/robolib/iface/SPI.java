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

package org.team2583.robolib.iface;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
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
    
    private static int devices = 0;
    /**
     * @param iType
     * @param address
     */
    protected SPI(Port port, InterfaceType iType, byte address) {
        super(InterfaceType.SPI, ++devices);
        if(port.equals(Port.kMXP)){
            checkMXPPin(InterfaceType.SPI, 19);
            checkMXPPin(InterfaceType.SPI, 21);
            checkMXPPin(InterfaceType.SPI, 23);
            checkMXPPin(InterfaceType.SPI, 25);
        }
    }

}
