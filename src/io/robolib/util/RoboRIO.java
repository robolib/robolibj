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

package io.robolib.util;

import static io.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.IntBuffer;

import io.robolib.hal.HALUtil;
import io.robolib.hal.PowerJNI;
import io.robolib.robot.RoboLibBot;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The Class RoboRIO.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class RoboRIO {
    
    private ITable m_table;
    
    private static RoboRIO m_instance;
    
    public static final RoboRIO getInstance(){
        return m_instance == null ? m_instance = new RoboRIO() : m_instance;
    }
    
    /**
     * Instantiates a new robo rio.
     */
    private RoboRIO(){
        m_table = RoboLibBot.getRobotTable().getSubTable("Power").getSubTable("RIO");
        updateTable();
    }
    
    public static int getFPGAVersion(){
        IntBuffer status = getLE4IntBuffer();
        int value = HALUtil.getFPGAVersion(status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    public static long getFPGARevision(){
        IntBuffer status = getLE4IntBuffer();
        int value = HALUtil.getFPGARevision(status);
        HALUtil.checkStatus(status);
        return (long) value;
    }
    
    public static long getFPGATime(){
        IntBuffer status = getLE4IntBuffer();
        long value = HALUtil.getFPGATime(status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    public static double getFPGATimestamp(){
        return getFPGATime() / 1000000.0;
    }

    /**
     * Return the status of the user button on the RoboRIO.
     *
     * @return The User Button status
     */
    public static boolean getUserButton(){
        IntBuffer status = getLE4IntBuffer();
        boolean value = HALUtil.getFPGAButton(status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Return the Voltage into the RoboRIO.
     *
     * @return The Vin Voltage
     */
    public static double getVoltage(){
        IntBuffer status = getLE4IntBuffer();
        double retVal = PowerJNI.getVinVoltage(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Return the Current into the RoboRIO.
     *
     * @return The Vin Current
     */
    public static double getCurrent(){
        IntBuffer status = getLE4IntBuffer();
        double retVal = PowerJNI.getVinCurrent(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Return the Voltage used by the 3.3 Volt rail.
     * This rail is used for I2C, SPI, Serial, and CAN?
     * @return The 3.3 rail voltage
     */
    public static double getCommsVoltage(){
        IntBuffer status = getLE4IntBuffer();
        double retVal = PowerJNI.getUserCurrent3V3(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Return the Current used by the 3.3 Volt rail.
     * This rail is used for I2C, SPI, Serial, and CAN?
     * @return The 3.3 rail current
     */
    public static double getCommsCurrent(){
        IntBuffer status = getLE4IntBuffer();
        double retVal = PowerJNI.getUserCurrent3V3(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the comms power enabled.
     *
     * @return the comms power enabled
     */
    public static boolean getCommsPowerEnabled(){
        IntBuffer status = getLE4IntBuffer();
        boolean retVal = PowerJNI.getUserActive3V3(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the comms fault count.
     *
     * @return the comms fault count
     */
    public static int getCommsFaultCount(){
        IntBuffer status = getLE4IntBuffer();
        int retVal = PowerJNI.getUserCurrentFaults3V3(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the IO voltage.
     *
     * @return the IO voltage
     */
    public static double getIOVoltage(){
        IntBuffer status = getLE4IntBuffer();
        double retVal = PowerJNI.getUserVoltage5V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the IO current.
     *
     * @return the IO current
     */
    public static double getIOCurrent(){
        IntBuffer status = getLE4IntBuffer();
        double retVal = PowerJNI.getUserCurrent5V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the IO power enabled.
     *
     * @return the IO power enabled
     */
    public static boolean getIOPowerEnabled(){
        IntBuffer status = getLE4IntBuffer();
        boolean retVal = PowerJNI.getUserActive5V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the IO fault count.
     *
     * @return the IO fault count
     */
    public static int getIOFaultCount(){
        IntBuffer status = getLE4IntBuffer();
        int retVal = PowerJNI.getUserCurrentFaults5V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the servo voltage.
     *
     * @return the servo voltage
     */
    public static double getPWMVoltage(){
        IntBuffer status = getLE4IntBuffer();
        double retVal = PowerJNI.getUserVoltage6V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the servo current.
     *
     * @return the servo current
     */
    public static double getPWMCurrent(){
        IntBuffer status = getLE4IntBuffer();
        double retVal = PowerJNI.getUserCurrent6V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the servo power enabled.
     *
     * @return the servo power enabled
     */
    public static boolean getPWMPowerEnabled(){
        IntBuffer status = getLE4IntBuffer();
        boolean retVal = PowerJNI.getUserActive6V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Gets the servo fault count.
     *
     * @return the servo fault count
     */
    public static int getPWMFaultCount(){
        IntBuffer status = getLE4IntBuffer();
        int retVal = PowerJNI.getUserCurrentFaults6V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Update the table for this object with the latest values.
     */
    public void updateTable(){
        m_table.putNumber("RIO Voltage", getVoltage());
        m_table.putNumber("RIO Current", getCurrent());
        
        m_table.putNumber("3v3 Rail Voltage", getCommsVoltage());
        m_table.putNumber("3v3 Rail Current", getCommsCurrent());
        
        m_table.putNumber("5v Rail Voltage", getIOVoltage());
        m_table.putNumber("5v Rail Current", getIOCurrent());
        
        m_table.putNumber("6v Rail Voltage", getPWMVoltage());
        m_table.putNumber("6v Rail Current", getPWMCurrent());
    }
    
    

}
