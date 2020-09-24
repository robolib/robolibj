/*
+ * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
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
import io.github.robolib.util.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public class LimitSystemMapper implements ModuleMapper<LimitSystem> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LimitSystem createModule(String key, JSONObject data) {
        String type = data.getString("type");
        switch(type){
        case "singlelimitsystem":
        case "single_limit_system": {
            BooleanSource limit;

            Object o = data.get("limit");

            if(o instanceof JSONObject){
                limit = RobotMap.getModule(key, (JSONObject) o);
            }else{
                limit = RobotMap.getModule((String)o);
            }

            return new LimitSystem(limit,
                    LimitSystem.SystemType.valueOf(data.getString("system_type")));
        }
        case "doublelimitsystem":
        case "double_limit_system": {
            BooleanSource topLimit;
            BooleanSource bottomLimit;

            Object o = data.get("forward_limit");
            Object p = data.get("reverse_limit");

            if(o instanceof JSONArray){
                topLimit = RobotMap.getModule(key, (JSONObject) o);
            }else{
                topLimit = RobotMap.getModule((String)o);
            }

            if(p instanceof JSONArray){
                bottomLimit = RobotMap.getModule(key, (JSONObject)p);
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
