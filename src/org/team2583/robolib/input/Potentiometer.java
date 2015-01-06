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

package org.team2583.robolib.input;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Potentiometer Class for use with the WPILib Code.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class Potentiometer implements IRoboSensor, IRoboAnalogSensor, PIDSource, LiveWindowSendable {
    
    /** The m_pot. */
    private AnalogInput m_pot;
    
    /**
     * Instantiates a new potentiometer.
     *
     * @param channel Analog Channel of the Potentiometer
     * @param avgBits Number of Averaging Bits
     * @param ovrBits Number of Oversample Bits
     */
    public Potentiometer(int channel, int avgBits, int ovrBits){
        m_pot = new AnalogInput(channel);
        m_pot.setAverageBits(avgBits);
        m_pot.setOversampleBits(ovrBits);
    }

    /**
     * Get raw value.
     *
     * @return Potentiometer raw value
     */
    public int getValue() {return m_pot.getValue(); }
    
    /**
     * Get raw voltage.
     *
     * @return Potentiometer raw voltage
     */
    public double getVoltage() { return m_pot.getVoltage(); }
        
    /** The last raw. */
    private double lastRaw = 0;
    
    /**
     * Get an Averaged value from the pot.
     *
     * @return The average of the current and last values
     */
    public double getAverage(){
        lastRaw = (lastRaw + getVoltage())/2;
        return lastRaw;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getChanel(){ return m_pot.getChannel(); }
    
    /**
     * Gets the average bits.
     *
     * @return the average bits
     */
    public int getAverageBits() { return m_pot.getAverageBits(); }
    
    /**
     * Gets the oversample bits.
     *
     * @return the oversample bits
     */
    public int getOversampleBits() { return m_pot.getOversampleBits(); }
    
    /**
     * {@inheritDoc}
     */
    public double pidGet() { return m_pot.pidGet(); }

    /**
     * {@inheritDoc}
     */
    public void updateTable() { m_pot.updateTable(); }

    /**
     * {@inheritDoc}
     */
    public void startLiveWindowMode() { m_pot.startLiveWindowMode(); }

    /**
     * {@inheritDoc}
     */
    public void stopLiveWindowMode() { m_pot.stopLiveWindowMode(); }

    /**
     * {@inheritDoc}
     */
    public void initTable(ITable arg0) { m_pot.initTable(arg0); }

    /**
     * {@inheritDoc}
     */
    public ITable getTable() { return m_pot.getTable(); }

    /**
     * {@inheritDoc}
     */
    public String getSmartDashboardType() { return m_pot.getSmartDashboardType(); }

}