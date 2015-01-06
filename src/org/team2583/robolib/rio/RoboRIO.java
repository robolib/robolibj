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

package org.team2583.robolib.rio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

//import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.hal.HALUtil;
import edu.wpi.first.wpilibj.hal.PowerJNI;

/**
 * The Class RoboRIO.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class RoboRIO {
    
    /** The m_status. */
    private static IntBuffer m_status;
    
    static {
        ByteBuffer b = ByteBuffer.allocateDirect(4);
        b.order(ByteOrder.LITTLE_ENDIAN);
        m_status = b.asIntBuffer();
    }
    
    /**
     * Instantiates a new robo rio.
     */
    private RoboRIO(){}

    /**
     * Return the status of the user button on the RoboRIO.
     *
     * @return The User Button status
     */
    public static boolean getUserButton(){
        boolean value = HALUtil.getFPGAButton(m_status);
        HALUtil.checkStatus(m_status);
        return value;
    }
    
    /**
     * Return the Voltage into the RoboRIO.
     *
     * @return The Vin Voltage
     */
    public static double getVoltage(){
        double retVal = PowerJNI.getVinVoltage(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Return the Current into the RoboRIO.
     *
     * @return The Vin Current
     */
    public static double getCurrent(){
        double retVal = PowerJNI.getVinCurrent(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Return the Voltage used by the 3.3 Volt rail.
     * This rail is used for I2C, SPI, Serial, and CAN?
     * @return The 3.3 rail voltage
     */
    public static double getCommsVoltage(){
        double retVal = PowerJNI.getUserCurrent3V3(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Return the Current used by the 3.3 Volt rail.
     * This rail is used for I2C, SPI, Serial, and CAN?
     * @return The 3.3 rail current
     */
    public static double getCommsCurrent(){
        double retVal = PowerJNI.getUserCurrent3V3(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the comms power enabled.
     *
     * @return the comms power enabled
     */
    public static boolean getCommsPowerEnabled(){
        boolean retVal = PowerJNI.getUserActive3V3(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the comms fault count.
     *
     * @return the comms fault count
     */
    public static int getCommsFaultCount(){
        int retVal = PowerJNI.getUserCurrentFaults3V3(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the IO voltage.
     *
     * @return the IO voltage
     */
    public static double getIOVoltage(){
        double retVal = PowerJNI.getUserVoltage5V(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the IO current.
     *
     * @return the IO current
     */
    public static double getIOCurrent(){
        double retVal = PowerJNI.getUserCurrent5V(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the IO power enabled.
     *
     * @return the IO power enabled
     */
    public static boolean getIOPowerEnabled(){
        boolean retVal = PowerJNI.getUserActive5V(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the IO fault count.
     *
     * @return the IO fault count
     */
    public static int getIOFaultCount(){
        int retVal = PowerJNI.getUserCurrentFaults5V(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the servo voltage.
     *
     * @return the servo voltage
     */
    public static double getServoVoltage(){
        double retVal = PowerJNI.getUserVoltage6V(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the servo current.
     *
     * @return the servo current
     */
    public static double getServoCurrent(){
        double retVal = PowerJNI.getUserCurrent6V(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the servo power enabled.
     *
     * @return the servo power enabled
     */
    public static boolean getServoPowerEnabled(){
        boolean retVal = PowerJNI.getUserActive6V(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    /**
     * Gets the servo fault count.
     *
     * @return the servo fault count
     */
    public static int getServoFaultCount(){
        int retVal = PowerJNI.getUserCurrentFaults6V(m_status);
        HALUtil.checkStatus(m_status);
        return retVal;
    }
    
    

}
