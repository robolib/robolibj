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

package io.github.robolib.util.mapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import io.github.robolib.util.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The Class RMap.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
@SuppressWarnings("unchecked")
public final class RobotMap {

    private static final Map<String, ModuleMapper<?>> m_builderMap =
            new HashMap<String, ModuleMapper<?>>();
    
    private static final Map<String, Object> m_objectMap =
            new HashMap<String, Object>();
    
    private static JSONObject m_jMap;
    
    private static String m_mapFile;
    
    private static boolean m_enabled = false;
    
    public static void setMapFile(String file){
        m_mapFile = file;
        m_enabled = true;
        File f = new File(m_mapFile);
        try {
            m_jMap = new JSONObject(new String(Files.readAllBytes(f.toPath()), "UTF-8"));
        } catch (JSONException | IOException e) {
            Logger.get(RobotMap.class).fatal("Failed to load config file", e);
        }
    }
    
    static{
        registerModuleBuilder(new SpeedControllerMapper());
        registerModuleBuilder(new CANJaguarMapper());
        registerModuleBuilder(new SolenoidMapper());
        registerModuleBuilder(new LimitSwitchMapper());
        registerModuleBuilder(new LimitedControllerMapper());
        registerModuleBuilder(new LimitSystemMapper());
        registerModuleBuilder(new DigitalIOMapper());
    }
    
    private RobotMap(){}
    
    public static void registerModuleBuilder(ModuleMapper<?> builder){
        for(String s : builder.getModuleIdentifiers()){
            if(m_builderMap.containsKey(s.toLowerCase()))
                throw new IllegalArgumentException("A module builder under the key '"
                        + s + "' already exists.");
            m_builderMap.put(s.toLowerCase(), builder);
        }
    }
    
    public static boolean getBoolean(String key){
        return m_jMap.getBoolean(key);
    }
    
    public static boolean getBoolean(String key, boolean def){
        try{
            return m_jMap.getBoolean(key);
        }catch(JSONException e){
            return def;
        }
    }
    
    public static String getString(String key){
        return m_jMap.getString(key);
    }
    
    public static String getString(String key, String def){
        try{
            return m_jMap.getString(key);
        }catch(JSONException e){
            return def;
        }
    }
    
    public static int getInt(String key){
        return m_jMap.getInt(key);
    }
    
    public static int getInt(String key, int def){
        try{
            return m_jMap.getInt(key);
        }catch(JSONException e){
            return def;
        }
    }
    
    public static double getNumber(String key){
        return m_jMap.getDouble(key);
    }
    
    public static double getNumber(String key, double def){
        try{
            return m_jMap.getDouble(key);
        }catch(JSONException e){
            return def;
        }
    }
        
    protected static <T> T getModule(String key, JSONObject data){
        ModuleMapper<?> builder = m_builderMap.get(data.getString("type"));
        if(builder == null)
            throw new RuntimeException("Unknown Module builder for type '" + data.getString("type") + "'.");
        return (T) builder.createModule(key, data);
    }

    public static <T> T getModule(String key){
        if(!m_enabled)
            throw new IllegalStateException(
                    "You must set the map file in the robot constructor before anything else.");
        if(m_objectMap.containsKey(key)){
            return (T) m_objectMap.get(key);
        }else{
            Object x = getModule(key, m_jMap.getJSONObject(key));
            m_objectMap.put(key, x);
            return (T) x;
        }
    }
}