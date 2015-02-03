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

package io.github.robolib.module;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.IntBuffer;

import io.github.robolib.control.ButtonTrigger;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.hal.PowerJNI;
import io.github.robolib.identifier.Sendable;
import io.github.robolib.util.StringUtils;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The Class RoboRIO.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class RoboRIO implements Sendable {
    
    private ITable m_table;
    
    private static RoboRIO m_instance;
    
    private static ButtonTrigger m_userButton;
    
    public static final RoboRIO getInstance(){
        return m_instance == null ? m_instance = new RoboRIO() : m_instance;
    }
    
    /**
     * Instantiates a new robo rio.
     */
    private RoboRIO(){}
    
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
        return value;
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
    
    public static ButtonTrigger getUserButtonAsButton(){
        return m_userButton == null ? m_userButton = new ButtonTrigger(){
            @Override
            public boolean get(){return getUserButton();}
            } : m_userButton;
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
    public static double get3V3Voltage(){
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
    public static double get3V3Current(){
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
    public static boolean get3V3PowerEnabled(){
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
    public static int get3V3FaultCount(){
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
    public static double get5VVoltage(){
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
    public static double get5VCurrent(){
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
    public static boolean get5VPowerEnabled(){
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
    public static int get5VFaultCount(){
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
    public static double get6VVoltage(){
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
    public static double get6VCurrent(){
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
    public static boolean get6VPowerEnabled(){
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
    public static int get6VFaultCount(){
        IntBuffer status = getLE4IntBuffer();
        int retVal = PowerJNI.getUserCurrentFaults6V(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /*
     * Live Window code, only does anything if live window is activated.
     */
    @Override
    public String getSmartDashboardType() {
        return "Solenoid";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }
    
    /**
     * Update the table for this object with the latest values.
     */
    @Override
    public void updateTable(){
        m_table.putString("RIO Voltage", StringUtils.getNumber2DWithUnits(getVoltage(), "V"));
        m_table.putString("RIO Current", StringUtils.getNumber2DWithUnits(getCurrent(), "A"));
        
        m_table.putString("3v3 Voltage", StringUtils.getNumber2DWithUnits(get3V3Voltage(), "V"));
        m_table.putString("3v3 Current", StringUtils.getNumber2DWithUnits(get3V3Current(), "A"));
        
        m_table.putString("5v Voltage", StringUtils.getNumber2DWithUnits(get5VVoltage(), "V"));
        m_table.putString("5v Current", StringUtils.getNumber2DWithUnits(get5VCurrent(), "A"));
        
        m_table.putString("6v Voltage", StringUtils.getNumber2DWithUnits(get6VVoltage(), "V"));
        m_table.putString("6v Current", StringUtils.getNumber2DWithUnits(get6VCurrent(), "A"));
    }
    
    

}
