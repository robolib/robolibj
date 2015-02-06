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

package io.github.robolib.module.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import io.github.robolib.module.ModuleBuilder;
import io.github.robolib.module.PDP.PowerChannel;
import io.github.robolib.module.RobotMap;
import io.github.robolib.module.iface.PWM.PWMChannel;
import io.github.robolib.util.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class SpeedControllerBuilder implements ModuleBuilder<SpeedController> {

    private static final Map<String, Class<? extends SpeedController>> m_classMap = new HashMap<>();
    
    static{        
        registerController("Talon", Talon.class);
        registerController("TalonSRX", TalonSRX.class);
        registerController("Victor", Victor.class);
        registerController("VictorSP", VictorSP.class);
        registerController("Jaguar", Jaguar.class);
        registerController("CANJaguar", CANJaguar.class);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public SpeedController createModule(String key, JSONArray arrayData) {
        try {
            
            String type = arrayData.getString(0).toLowerCase();
            Object devChan = arrayData.getInt(1);
            if(!(type == "CANJaguar" || type == "CANTalon"))
                devChan = PWMChannel.values()[(int)devChan];
            
            Class[] classes = null;
            Object[] args = null;
            boolean invert = false;
            
            switch(arrayData.length()){
            case 2:
                classes = new Class[]{devChan.getClass()};
                args = new Object[]{devChan};
                break;
            case 3:
                classes = new Class[]{devChan.getClass(), String.class};
                args = new Object[]{devChan, arrayData.getString(2)};
                break;
            case 4:
                classes = new Class[]{devChan.getClass(), String.class, PowerChannel.class};
                args = new Object[]{devChan, arrayData.getString(2),
                        PowerChannel.values()[arrayData.getInt(3)]};
                break;
            case 5:
            default:
                classes = new Class[]{devChan.getClass(), String.class, PowerChannel.class};
                args = new Object[]{devChan, arrayData.getString(2),
                        PowerChannel.values()[arrayData.getInt(3)]};
                invert = arrayData.getBoolean(4);
                break;
                    
            }
            
            SpeedController s = m_classMap.get(type).getConstructor(classes).newInstance(args);
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
    
    public static final void registerController(String key, Class<? extends SpeedController> con){
        if(m_classMap.containsKey(key.toLowerCase())){
            Logger.get(RobotMap.class).warn(
                    "SpeedControllerBuilder already contains a an item for key '" + key + "'.");
        }else{
            m_classMap.put(key.toLowerCase(), con);
        }
            
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getStringIdentifiers() {
        return new String[]{
                "speed_controller",
                "speedcontroller",
                "motor_controller",
                "motorcontroller"
        };
    }

}
