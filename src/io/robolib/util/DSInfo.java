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

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Returns Useful Data from the Driver Station.
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class DSInfo {
    
    
    /** The Constant m_ds. */
    private static final DriverStation m_ds = DriverStation.getInstance();
    
    /**
     * Instantiates a new DS info.
     *
     * @throws IllegalAccessException the illegal access exception
     */
    private DSInfo() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
    
    /**
     * Gets the batt voltage.
     *
     * @return the batt voltage
     */
    public static double getBattVoltage(){
        return m_ds.getBatteryVoltage();
    }

    /**
     * Gets the alliance.
     *
     * @return the alliance
     */
    public static DriverStation.Alliance getAlliance(){
        return m_ds.getAlliance();
    }

    /**
     * Gets the stick axis.
     *
     * @param port the port
     * @param channel the channel
     * @return the stick axis
     */
    public static double getStickAxis(int port, int channel){
        return m_ds.getStickAxis(port, channel);
    }

    /**
     * Gets the stick buttons.
     *
     * @param port the port
     * @return the stick buttons
     */
    public static int getStickButtons(int port){
        return m_ds.getStickButtons(port);
    }
    
    /**
     * Enabled.
     *
     * @return true, if successful
     */
    public static boolean enabled(){
        return m_ds.isEnabled();
    }
    
    /**
     * Disabled.
     *
     * @return true, if successful
     */
    public static boolean disabled(){
        return !enabled();
    }
    
    /**
     * Gets the team location.
     *
     * @return the team location
     */
    public static int getTeamLocation(){
        return m_ds.getLocation();
    }

    /**
     * On field.
     *
     * @return true, if successful
     */
    public static boolean onField(){
        return isFMSAttached();
    }
    
    /**
     * Checks if is FMS attached.
     *
     * @return true, if is FMS attached
     */
    public static boolean isFMSAttached(){
        return m_ds.isFMSAttached();
    }
    
    /**
     * Gets the match time.
     *
     * @return the match time
     */
    public static double getMatchTime(){
        return m_ds.getMatchTime();
    }
}