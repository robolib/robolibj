/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

import io.github.robolib.module.LimitSystem;
import io.github.robolib.module.controller.LimitedController;
import io.github.robolib.module.controller.SpeedController;

import org.json.JSONObject;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitedControllerMapper implements ModuleMapper<LimitedController> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LimitedController createModule(String key, JSONObject data) {
        SpeedController motor;
        LimitSystem system;

        Object o = data.get("motor");
        Object p = data.get("system");
        
        if(o instanceof JSONObject){
            motor = RobotMap.getModule(key, (JSONObject) o);
        }else{
            motor = RobotMap.getModule((String) o);
        }
        
        if(p instanceof JSONObject){
            system = RobotMap.getModule(key, (JSONObject) p);
        }else{
            system = RobotMap.getModule((String) p);
        }
        
        return new LimitedController(motor, system);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getModuleIdentifiers() {
        return new String[]{
                "limitedcontroller",
                "limited_controller",
                "limitcontroller",
                "limit_controller"
        };
    }

}
