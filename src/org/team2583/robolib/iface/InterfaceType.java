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

import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public enum InterfaceType {
    
    ANALOG(tResourceType.kResourceType_AnalogChannel),
    CAN(tResourceType.kResourceType_PWM),
    DIGITALIO(tResourceType.kResourceType_DigitalInput),
    I2C(tResourceType.kResourceType_I2C),
    PWM(tResourceType.kResourceType_PWM),
    SERIAL(tResourceType.kResourceType_SerialPort),
    SPI(tResourceType.kResourceType_SPI);
    
    public final int m_resource;
    
    InterfaceType(int type){
        m_resource = type;
    }

}
