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

import io.github.robolib.identifier.BooleanSource;
import io.github.robolib.module.LimitSystem;
import io.github.robolib.module.LimitSystem.SystemType;
import io.github.robolib.util.log.Logger;

import org.json.JSONArray;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitSystemMapper implements ModuleMapper<LimitSystem> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LimitSystem createModule(String key, JSONArray arrayData) {
        String type = arrayData.getString(0);
        switch(type){
        case "singlelimitsystem":
        case "single_limit_system": {
            BooleanSource limit;

            Object o = arrayData.get(1);
            
            if(o instanceof JSONArray){
                limit = RobotMap.getModule(key, (JSONArray) o);
            }else{
                limit = RobotMap.getModule((String)o);
            }
            
            return new LimitSystem(limit,
                    SystemType.valueOf(arrayData.getString(2)));
        }
        case "doublelimitsystem":
        case "double_limit_system": {
            BooleanSource topLimit;
            BooleanSource bottomLimit;

            Object o = arrayData.get(1);
            Object p = arrayData.get(2);
            
            if(o instanceof JSONArray){
                topLimit = RobotMap.getModule(key, (JSONArray) o);
            }else{
                topLimit = RobotMap.getModule((String)o);
            }
            
            if(p instanceof JSONArray){
                bottomLimit = RobotMap.getModule(key, (JSONArray)p);
            }else{
                bottomLimit = RobotMap.getModule((String)p);
            }
            
            return new LimitSystem(topLimit, bottomLimit);
        }
        default:
            Logger.get(RobotMap.class).fatal("No such type '" + type + "' for limit systems. Key: " + key);
            break;
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getModuleIdentifiers() {
        return new String[]{
                "limitsystem",
                "limit_system",
                "singlelimitsystem",
                "single_limit_system",
                "doublelimitsystem",
                "double_limit_system"
        };
    }

}
