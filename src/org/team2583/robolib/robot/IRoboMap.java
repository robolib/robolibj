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

package org.team2583.robolib.robot;

/**
 * The Interface IRoboMap.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public interface IRoboMap {
    
    /**
     * Define string.
     *
     * @param name the name
     * @param vlaue the vlaue
     */
    public void defineString(String name, String vlaue);
    
    /**
     * Define boolean.
     *
     * @param name the name
     * @param value the value
     */
    public void defineBoolean(String name, boolean value);
    
    /**
     * Define number.
     *
     * @param name the name
     * @param value the value
     */
    public void defineNumber(String name, double value);
    
    /**
     * Define object.
     *
     * @param name the name
     * @param value the value
     */
    public void defineObject(String name, Object value);

    /**
     * Gets the string.
     *
     * @param name the name
     * @return the requested {@code String} property or {@code ""} if not found
     */
    public String getString(String name);
    
    /**
     * Gets the string.
     *
     * @param name the name
     * @param def the def
     * @return the requested {@code String} property or {@code def} if not found
     */
    public String getString(String name, String def);
    
    /**
     * Gets the boolean.
     *
     * @param name the name
     * @return the requested {@code boolean} property or {@code false} if not found
     */
    public boolean getBoolean(String name);

    /**
     * Gets the boolean.
     *
     * @param name the name
     * @param def the def
     * @return the requested {@code boolean} property or {@code def} if not found
     */
    public boolean getBoolean(String name, boolean def);
    
    /**
     * Gets the number.
     *
     * @param name the name
     * @return the requested {@code double} property or {@code 0.00} if not found
     */
    public double getNumber(String name);
    
    /**
     * Gets the number.
     *
     * @param name the name
     * @param def the def
     * @return the requested {@code double} property or {@code def} if not found
     */
    public double getNumber(String name, double def);
    
    /**
     * Gets the object.
     *
     * @param name the name
     * @return the requested {@code Object} property or {@code null} if not found
     */
    public Object getObject(String name);
    
    /**
     * Gets the object.
     *
     * @param name the name
     * @param def the def
     * @return the requested {@code Object} property or {@code def} if not found
     */
    public Object getObject(String name, Object def);

}