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
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import io.github.robolib.util.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * The Class RMap.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class RobotMap {

    private static final Map<String, ModuleBuilder<?>> m_builderMap = new HashMap<String, ModuleBuilder<?>>();
    
    private static JSONObject m_jMap;
    
    private static String m_mapFile;
    
    private static boolean m_enabled = false;
    
    public static final void setMapFile(String file){
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
        registerModuleBuilder(new SpeedControllerBuilder());
        registerModuleBuilder(new SolenoidBuilder());
    }
    
    public static final void registerModuleBuilder(ModuleBuilder<?> builder){
        for(String s : builder.getStringIdentifiers()){
            if(m_builderMap.containsKey(s.toLowerCase()))
                throw new IllegalArgumentException("A module builder under the key '" + s + "' already exists.");
            m_builderMap.put(s.toLowerCase(), builder);
        }
    }

    public static final Object get(String key){
        if(!m_enabled)
            throw new IllegalStateException("You must set the map file in the robot constructor before anything else.");
        JSONObject data = m_jMap.getJSONObject(key);
        ModuleBuilder<?> builder = m_builderMap.get(data.getString("type").toLowerCase());
        return builder.createModule(key, data.getJSONArray("data"));
    }
    
    @SuppressWarnings("unchecked")
    public static final <T> T get(String key, Type a){
        
        return (T) get(key);
    }
}