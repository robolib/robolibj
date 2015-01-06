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
public class Serial extends Interface {

    public enum Port {
        kOnboard,
        kMXP,
        kUSB;
    };

    /**
     * @param iType
     * @param address
     */
    protected Serial(Port port, byte address) {
        super(InterfaceType.SERIAL, 0);
        if(port.equals(Port.kMXP)){
            checkMXPPin(InterfaceType.SERIAL, 14);
            checkMXPPin(InterfaceType.SERIAL, 10);
        }
    }

}
