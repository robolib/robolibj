/*
 * Copyright (c) 2014 noriah <vix@noriah.dev>.
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

package org.team2583.robolib.util;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Returns Useful Data from the Driver Station.
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class DSInfo {
    
    
    private static final DriverStation m_ds = DriverStation.getInstance();
    
    private DSInfo() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
    
    public static double getBattVoltage(){
        return m_ds.getBatteryVoltage();
    }

    public static DriverStation.Alliance getAlliance(){
        return m_ds.getAlliance();
    }

    public static double getStickAxis(int port, int channel){
        return m_ds.getStickAxis(port, channel);
    }

    public static int getStickButtons(int port){
        return m_ds.getStickButtons(port);
    }
    
    public static boolean enabled(){
        return m_ds.isEnabled();
    }
    
    public static boolean disabled(){
        return !enabled();
    }
    
    public static int getTeamLocation(){
        return m_ds.getLocation();
    }

    public static boolean onField(){
        return isFMSAttached();
    }
    
    public static boolean isFMSAttached(){
        return m_ds.isFMSAttached();
    }
    
    public static double getMatchTime(){
        return m_ds.getMatchTime();
    }
}