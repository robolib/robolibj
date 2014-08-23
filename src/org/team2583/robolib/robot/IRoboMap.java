/*
 * Copyright (c) 2014 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package org.team2583.robolib.robot;

/**
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public interface IRoboMap {
    
    /**
     * 
     * @param name
     * @param vlaue 
     */
    public void defineString(String name, String vlaue);
    
    /**
     * 
     * @param name
     * @param value 
     */
    public void defineBoolean(String name, boolean value);
    
    /**
     * 
     * @param name
     * @param value 
     */
    public void defineNumber(String name, double value);
    
    /**
     * 
     * @param name
     * @param value 
     */
    public void defineObject(String name, Object value);

    /**
     * 
     * @param name
     * @return the requested {@code String} property or {@code ""} if not found
     */
    public String getString(String name);
    
    /**
     * 
     * @param name
     * @param def
     * @return the requested {@code String} property or {@code def} if not found
     */
    public String getString(String name, String def);
    
    /**
     * 
     * @param name
     * @return the requested {@code boolean} property or {@code false} if not found
     */
    public boolean getBoolean(String name);

    /**
     * 
     * @param name
     * @param def
     * @return the requested {@code boolean} property or {@code def} if not found
     */
    public boolean getBoolean(String name, boolean def);
    
    /**
     * 
     * @param name
     * @return the requested {@code double} property or {@code 0.00} if not found
     */
    public double getNumber(String name);
    
    /**
     * 
     * @param name
     * @param def
     * @return the requested {@code double} property or {@code def} if not found
     */
    public double getNumber(String name, double def);
    
    /**
     * 
     * @param name
     * @return the requested {@code Object} property or {@code null} if not found
     */
    public Object getObject(String name);
    
    /**
     * 
     * @param name
     * @param def
     * @return the requested {@code Object} property or {@code def} if not found
     */
    public Object getObject(String name, Object def);

}