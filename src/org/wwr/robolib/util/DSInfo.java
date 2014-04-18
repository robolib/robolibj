/*
 * Copyright (c) 2014 Westwood Robotics code.westwoodrobotics@gmail.com.
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

package org.wwr.robolib.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 * Returns Useful Data from the Driver Station.
 * 
 * @author Austin Reuland
 */
public final class DSInfo {
    
    
    private static final DriverStation m_ds = DriverStation.getInstance();
    private static final DriverStationEnhancedIO m_dsio = m_ds.getEnhancedIO();
    private static final DriverStationLCD m_dslcd = DriverStationLCD.getInstance();
    
    private DSInfo() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
    
    public static double getBattVoltage(){
        return m_ds.getBatteryVoltage();
    }

    public static DriverStation.Alliance getAlliance(){
        return m_ds.getAlliance();
    }
        
    public static double getAnalogIn(int channel){
        return m_ds.getAnalogIn(channel);
    }
    
    public static boolean getDigitalIn(int channel){
        return m_ds.getDigitalIn(channel);
    }
    
    public static boolean getDigitalOut(int channel){
        return m_ds.getDigitalOut(channel);
    }
    
    public static void setDigitalOut(int channel, boolean value){
        m_ds.setDigitalOut(channel, value);
    }
    
    public static boolean enabled(){
        return m_ds.isEnabled();
    }
    
    public static boolean disabled(){
        return !enabled();
    }
    
    public static int getPacketNumber(){
        return m_ds.getPacketNumber();
    }
    
    public static int getTeamLocation(){
        return m_ds.getLocation();
    }
    
    public static int getTeamNumber(){
        return m_ds.getTeamNumber();
    }
    
    public static boolean onField(){
        return m_ds.isFMSAttached();
    }
    
    public static boolean isFMSAttached(){
        return onField();
    }
    
    public static double getMatchTime(){
        return m_ds.getMatchTime();
    }
    
    public static void printLCDLine1(Object x){
        printLCDLine1(String.valueOf(x));
    }
    
    public static void printLCDLine1(double x){
        printLCDLine1(String.valueOf(x));
    }
    
    public static void printLCDLine1(String msg){
        m_dslcd.println(DriverStationLCD.Line.kUser1, 1, msg);
        m_dslcd.updateLCD();
    }
    
    public static void printLCDLine2(Object x){
        printLCDLine2(String.valueOf(x));
    }
    
    public static void printLCDLine2(double x){
        printLCDLine2(String.valueOf(x));
    }
    
    public static void printLCDLine2(String msg){
        m_dslcd.println(DriverStationLCD.Line.kUser2, 1, msg);
        m_dslcd.updateLCD();
    }
    
    public static void printLCDLine3(Object o){
        printLCDLine3(o.toString());
    }
    
    public static void printLCDLine3(String msg){
        m_dslcd.println(DriverStationLCD.Line.kUser3, 1, msg);
        m_dslcd.updateLCD();
    }
    
    public static void printLCDLine4(Object o){
        printLCDLine4(o.toString());
    }
    
    public static void printLCDLine4(String msg){
        m_dslcd.println(DriverStationLCD.Line.kUser4, 1, msg);
        m_dslcd.updateLCD();
    }
    
    public static void printLCDLine5(Object o){
        printLCDLine5(o.toString());
    }
    
    public static void printLCDLine5(String msg){
        m_dslcd.println(DriverStationLCD.Line.kUser5, 1, msg);
        m_dslcd.updateLCD();        
    }
    
    public static void printLCDLine6(Object o){
        printLCDLine6(o.toString());
    }
    
    public static void printLCDLine6(String msg){
        m_dslcd.println(DriverStationLCD.Line.kUser6, 1, msg);
        m_dslcd.updateLCD();
    }
    
    public static void clearLCD(){
        m_dslcd.clear();
    }

}