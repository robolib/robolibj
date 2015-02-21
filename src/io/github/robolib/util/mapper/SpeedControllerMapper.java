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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import io.github.robolib.module.PDP.PowerChannel;
import io.github.robolib.module.controller.Jaguar;
import io.github.robolib.module.controller.SpeedController;
import io.github.robolib.module.controller.Talon;
import io.github.robolib.module.controller.TalonSRX;
import io.github.robolib.module.controller.Victor;
import io.github.robolib.module.iface.PWM.PWMChannel;
import io.github.robolib.util.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class SpeedControllerMapper implements ModuleMapper<SpeedController> {

    private static final Map<String, Class<? extends SpeedController>> m_controllMap =
            new HashMap<String, Class<? extends SpeedController>>();
    
    static{        
        registerController(Talon.class, "talon", "talonsr");
        registerController(TalonSRX.class, "talonsrx");
        registerController(Victor.class, "victor", "victorsp");
        registerController(Jaguar.class, "jaguar", "jag");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SpeedController createModule(String key, JSONObject data) {
        try {
            
            String type = data.getString("type").toLowerCase();
            boolean invert = data.getBoolean("inverted");
            
            SpeedController s = m_controllMap.get(type)
                    .getConstructor(
                            PWMChannel.class,
                            String.class,
                            PowerChannel.class)
                    .newInstance(
                            PWMChannel.values()[data.getInt("device_channel")],
                            data.getString("description"),
                            PowerChannel.values()[data.getInt("power_channel")]);
            s.setInverted(invert);
            return s;
            
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | JSONException e) {
            Logger.get(RobotMap.class).fatal("Unable to create SpeedController for key '" + key + "'", e);
        }
        return null;
    }
    
    public static final void registerController(Class<? extends SpeedController> con, String ... keys){
        for(String key : keys){
            if(m_controllMap.containsKey(key.toLowerCase())){
                Logger.get(RobotMap.class).warn(
                        "SpeedControllerBuilder already contains a an item for key '" + key + "'.");
            }else{
                m_controllMap.put(key.toLowerCase(), con);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getModuleIdentifiers() {
        return m_controllMap.keySet().toArray(new String[m_controllMap.size()]);
    }

}
